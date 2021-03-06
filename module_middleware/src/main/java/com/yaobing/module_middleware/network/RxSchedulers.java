package com.yaobing.module_middleware.network;

/**
 * Create by 姚冰
 * on 2020/10/29
 */

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulers {
    public static final FlowableTransformer<?, ?> mTransformer = new FlowableTransformer<Object, Object>() {
        public Publisher<Object> apply(@NonNull Flowable<Object> observable) {
            return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
    };

    public RxSchedulers() {
    }

    public static <T> FlowableTransformer<T, T> io_main() {
        return new FlowableTransformer<T, T>() {
            public Publisher<T> apply(@NonNull Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    public static <T> FlowableTransformer<T, T> main_io() {
        return new FlowableTransformer<T, T>() {
            public Publisher<T> apply(@NonNull Flowable<T> observable) {
                return observable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> main_main() {
        return new FlowableTransformer<T, T>() {
            public Publisher<T> apply(@NonNull Flowable<T> observable) {
                return observable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> new_new() {
        return new FlowableTransformer<T, T>() {
            public Publisher<T> apply(@NonNull Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread());
            }
        };
    }
    public static <T> FlowableTransformer<T, T> new_main() {
        return new FlowableTransformer<T, T>() {
            public Publisher<T> apply(@NonNull Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}