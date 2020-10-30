package com.yaobing.framemvpproject;

import java.util.ArrayList;

/**
 * @author : yaobing
 * @date : 2020/10/30 15:21
 * @desc :
 */
//@ContractFactory(entites = {ArrayList.class})
    
public interface GithubRepoAPI {
    void getAllRepoByName(String name);
}
