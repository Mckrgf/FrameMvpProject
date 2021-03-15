package com.yaobing.module_middleware;

import com.google.gson.Gson;

/**
 * @author : yaobing
 * @date : 2021/3/15 14:10
 * @desc :
 */
public class BaseEntity {
    static final long serialVersionUID=536871008L;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
