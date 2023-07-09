package com.hakan.test.service;

import com.hakan.basicdi.annotations.Autowired;
import com.hakan.basicdi.annotations.PostConstruct;
import com.hakan.basicdi.annotations.Service;
import com.hakan.test.config.TestConfig;
import com.hakan.test.item.TestItem;

@Service
public class TestService {

    @Autowired
    private TestConfig config;

    @Autowired
    private TestItem item;


    @PostConstruct
    public void init() {
        System.out.println(this.item.getName());
        System.out.println(this.config.getMessage1());
        System.out.println(this.config.getMessage2());
        System.out.println("TestService is initialized.");
    }


    public void send() {
        System.out.println(this.item.getName());
        System.out.println(this.config.getMessage1());
        System.out.println(this.config.getMessage2());
        System.out.println("TestService#send is called.");
    }
}
