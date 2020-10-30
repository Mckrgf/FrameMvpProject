package com.yaobing.module_middleware.activity;

import com.blankj.utilcode.util.LogUtils;

import com.yaobing.module_apt.Presenter;
import com.yaobing.module_common_view.base.presenter.BasePresenter;
import com.yaobing.module_common_view.contract.IBaseView;
import com.yaobing.module_middleware.PresenterRouter;
import com.yaobing.module_middleware.Utils.InstanceUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class BasePresenterActivity extends BaseActivity {

    @Override
    protected void onInit() {
        super.onInit();
        initPresenter();
    }

    protected PresenterRouter presenterRouter;
    private List<BasePresenter> mPresenters = new ArrayList<>();

    protected void initPresenter() {
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

        for(BasePresenter presenter: mPresenters){

            if(presenter!=null){
                presenter.detachView();
            }

        }

    }

}