package com.yaobing.module_common_view.contract;

/**
 * @author : yaobing
 * @date : 2020/10/26 15:42
 * @desc :
 */
public interface IBasePresenter<V>{

    void attachView(V v);
    V getView();
    void detachView();

}