package com.hakan.test.module;

import com.hakan.basicdi.annotations.Provide;
import com.hakan.basicdi.module.Module;
import com.hakan.test.config.TestConfig;
import com.hakan.test.item.TestItem;
import com.hakan.test.service.TestService;

public class TestModule extends Module {

	@Override
	public void configure() {
		this.bind(TestItem.class);
		this.bind(TestService.class);
	}

	@Provide
	public TestConfig testConfig() {
		return new TestConfig("Test Item", "Test Message 1", "Test Message 2");
	}
}
