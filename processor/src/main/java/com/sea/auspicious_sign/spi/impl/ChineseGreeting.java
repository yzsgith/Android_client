package com.sea.auspicious_sign.spi.impl;

import com.sea.auspicious_sign.spi.GreetingService;

public class ChineseGreeting implements GreetingService {
    @Override
    public void greet() {
        System.out.println("你好!");
    }
}