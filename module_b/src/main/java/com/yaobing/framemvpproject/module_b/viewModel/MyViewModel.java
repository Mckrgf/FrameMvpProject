package com.yaobing.framemvpproject.module_b.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.god.yb.testgitdemo.item.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : yaobing
 * @date : 2022/12/14 10:58
 * @desc :
 */
public class MyViewModel extends ViewModel {
    int i = 0;
    // Expose screen UI state
    private MutableLiveData<List<User>> users;
    private MutableLiveData<String> testString;
    private MutableLiveData<String> testString_plus;
    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<List<User>>();
            loadUsers();
        }
        return users;
    }
    public void changeString(String string) {
        testString.setValue(testString.getValue());
    }
    public LiveData<String> getString() {
        if (testString == null) {
            testString = new MutableLiveData<>();
        }
        testString.setValue("姚冰");
        return testString;
    }
    public void changeStringPlus() {
        i++;
        if (testString_plus == null) {
            testString_plus = new MutableLiveData<>();
        }
        testString_plus.setValue("姚冰" + i);
    }
    public LiveData<String> getStringPlus() {
        i++;
        if (testString_plus == null) {
            testString_plus = new MutableLiveData<>();
        }
        testString_plus.setValue("姚冰" + i);
        return testString_plus;
    }
    public LiveData<String> getTestStringPlus() {
        return testString_plus;
    }

    // Handle business logic
    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
        ArrayList<User> userList= new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("编号" + i);
            user.setAge(i+ "");
            user.setSex(1);
            userList.add(user);

        }
        users.setValue(userList);
    }
}
