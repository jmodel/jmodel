package com.github.jmodel.api.control;

import java.util.Properties;

import com.github.jmodel.config.Configurable;

/**
 * 
 * 
 * @author jianni@hotmail.com
 *
 */
public abstract class AbstractAction implements Configurable {

	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getProperty(String key) {
		return (String) properties.get(key);
	}

	public static String getRegionId() {
		return "Action";
	}
}
