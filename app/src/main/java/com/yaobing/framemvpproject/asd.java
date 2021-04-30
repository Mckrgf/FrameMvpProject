package com.yaobing.framemvpproject;

import androidx.annotation.NonNull;

import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;

/**
 * @author : yaobing
 * @date : 2021/4/19 9:55
 * @desc :
 */
public class asd {
    public static void main(String[] args) {
        Flowable.create(new FlowableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {

                    }
                }, BackpressureStrategy.ERROR)
                .subscribe(new FlowableSubscriber<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
