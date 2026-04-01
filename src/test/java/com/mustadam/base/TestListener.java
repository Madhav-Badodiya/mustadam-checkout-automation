package com.mustadam.base;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("❌ FAILED: " 
            + result.getName());
        System.out.println("REASON: " 
            + result.getThrowable().getMessage());
        System.out.println("CAUSE: " 
            + result.getThrowable().toString());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ PASSED: " 
            + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭ SKIPPED: " 
            + result.getName());
    }
}
