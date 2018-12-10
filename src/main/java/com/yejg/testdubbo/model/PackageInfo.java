package com.yejg.testdubbo.model;

import java.util.Map;

public class PackageInfo {
    private String myInterface;
    //Key:method value:String[] params
    private Map<String, String[]> myMethods;

    public String getMyInterface() {
        return myInterface;
    }

    public void setMyInterface(String myInterface) {
        this.myInterface = myInterface;
    }

    public Map<String, String[]> getMyMethods() {
        return myMethods;
    }

    public void setMyMethods(Map<String, String[]> myMethods) {
        this.myMethods = myMethods;
    }
}
