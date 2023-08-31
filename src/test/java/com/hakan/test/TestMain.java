package com.hakan.test;

import com.hakan.basicdi.Injector;
import com.hakan.test.module.TestModule;
import com.hakan.test.service.TestService;

public class TestMain {

	public static void main(String[] args) {
		Injector injector = Injector.of(new TestModule()).create();
		injector.getInstance(TestService.class).send();
	}
}
