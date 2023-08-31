package com.hakan.test.config;

public class TestConfig {

	private final String itemName;
	private final String message1;
	private final String message2;

	public TestConfig(String itemName,
					  String message1,
					  String message2) {
		this.itemName = itemName;
		this.message1 = message1;
		this.message2 = message2;
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getMessage1() {
		return this.message1;
	}

	public String getMessage2() {
		return this.message2;
	}
}
