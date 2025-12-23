package com.yaobing.module_middleware.activity;

import com.blankj.utilcode.util.LogUtils;

import com.yaobing.module_apt.Presenter;
import com.yaobing.module_common_view.base.presenter.BasePresenter;
import com.yaobing.module_common_view.contract.IBaseView;
import com.yaobing.module_middleware.PresenterRouter;
import com.yaobing.module_middleware.Utils.InstanceUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenterActivity extends BaseActivity {
    protected CompositeDisposable mCompositeSubscription = new CompositeDisposable();
//    @Override
//    protected int getLayoutID() {
//        return 0;
//    }

    @Override
    protected void onInit() {
        super.onInit();
        initPresenter();
    }

    protected PresenterRouter presenterRouter;
    private List<BasePresenter> mPresenters = new ArrayList<>();

    protected void initPresenter() {
        Map  a = new HashMap();
        presenterRouter = new PresenterRouter();
        Annotation[] annotations = getClass().getAnnotations();
        for (Annotation annotation:annotations){

            if(annotation instanceof Presenter){
                Class[] presenters = ((Presenter) annotation).value();

                for(Class presenter : presenters){

                    if(this instanceof IBaseView){

//                        if(mPresenterRouter.hasPresenter(presenter)){
//                            BasePresenter basePresenter = (BasePresenter) mPresenterRouter.getPresenter(presenter);
//                            addPresenter(basePresenter);
//                            LogUtil.d("presenter " + presenter.getName() + " got!");
//                        }
//                        else {
                        BasePresenter basePresenter = (BasePresenter) InstanceUtil.getInstance(presenter);
                        presenterRouter.register(basePresenter);
                        addPresenter(basePresenter);
                        LogUtils.d("presenter " + presenter.getName() + " added!");
//                        }
                    }

                }

            }
        }
    }

    private void addPresenter(BasePresenter basePresenter) {
        basePresenter.attachView((IBaseView) this);
        mPresenters.add(basePresenter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.dispose();
        for(BasePresenter presenter: mPresenters){

            if(presenter!=null){
                presenter.detachView();
            }

        }

    }

}