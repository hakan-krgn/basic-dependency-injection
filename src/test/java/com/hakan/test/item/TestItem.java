package com.hakan.test.item;

import com.hakan.injection.annotations.Autowired;
import com.hakan.injection.annotations.Component;
import com.hakan.test.config.TestConfig;

@Component
public class TestItem {

    private String name;

    @Autowired
    public TestItem(TestConfig config) {
        this.name = config.getItemName();
    }

    public String getName() {
        return this.name;
    }
}
