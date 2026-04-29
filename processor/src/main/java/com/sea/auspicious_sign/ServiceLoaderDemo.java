package com.sea.auspicious_sign;

import com.sea.auspicious_sign.spi.GreetingService;
import java.util.ServiceLoader;

public class ServiceLoaderDemo {
    public static void main(String[] args) {
        ServiceLoader<GreetingService> loader = ServiceLoader.load(GreetingService.class);
        for (GreetingService service : loader) {
            service.greet();
        }
    }
}