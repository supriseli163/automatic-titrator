package com.jh.automatic_titrator.entity.common;

/**
 * Created by apple on 2016/12/18.
 */

public class SettingConfig {

    private TestMethod testMethod;

    private String testName;

    private String testNo;

    private String testId;

    public TestMethod getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(TestMethod testMethod) {
        this.testMethod = testMethod;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }
}
