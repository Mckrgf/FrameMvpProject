package com.yaobing.module_middleware.Utils;

import android.net.Uri;

/**
 * Created by yaobing on 2019/1/10
 * Email:yaobing@supcom.com
 */
public class UrlUtil {

    public static long parseIdFormUrl(String url, String key){

        Uri uri = Uri.parse(url);
        String parameter = uri.getQueryParameter(key);
        try {
            if (parameter != null) {

                return Long.parseLong(parameter);
            }
        }
        catch (Exception e){
            return 0;
        }

        return 0;
    }

    public static String parseParamFormUrl(String url, String key){

        Uri uri = Uri.parse(url);
        String parameter = uri.getQueryParameter(key);
        try {
            if (parameter != null) {

                return parameter;
            }
        }
        catch (Exception e){
            return null;
        }

        return null;
    }
}
