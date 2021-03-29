package com.Assel.myapplication2;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UnitTestMyApp {
    private final CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void loginTest_1() throws InterruptedException {
        LogInTest.logIn(
                "aseelww@gmail.com",
                "Aseel#123");
        latch.await(5, TimeUnit.SECONDS);
        Assert.assertTrue(LogInTest.getStatus);
    }

    @Test
    public void loginTest_2() throws InterruptedException {
        LogInTest.logIn(
                "demo",
                "1");

        latch.await(5, TimeUnit.SECONDS);
        Assert.assertTrue(LogInTest.getStatus);
    }

    @Test
    public void getDataTest_1() throws InterruptedException {
        GetData.getData("aseelww@gmail.com");
        latch.await(5, TimeUnit.SECONDS);
        Assert.assertTrue(GetData.getStatus);

    }

    @Test
    public void getDataTest_2() throws InterruptedException {
        GetData.getData("");
        latch.await(5, TimeUnit.SECONDS);
        Assert.assertTrue(GetData.getStatus);

    }

}
