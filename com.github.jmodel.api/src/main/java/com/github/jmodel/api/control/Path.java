package com.github.jmodel.api.control;

import java.util.Properties;

public class Path {

	private static Properties properties = new Properties();

	public Properties getProperties() {
		return properties;
	}

	public String getProperty(String key) {
		return (String) properties.get(key);
	}
}
