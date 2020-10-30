package com.yaobing.module_middleware.Utils;

import java.util.ArrayList;

public class FakeData {
    public static ArrayList<String> fakeNamesForSearch() {
        //首字母转换
        final ArrayList<String> names = new ArrayList<>();
        String name = "费振华";
        names.add(name);
        name = "赵蔚文";
        names.add(name);
        name = "12456";
        names.add(name);
        name = "7890";
        names.add(name);
        name = "abc";
        names.add(name);
        name = "DEF";
        names.add(name);
        name = "abcDEF123456";
        names.add(name);
        return names;
    }
}
