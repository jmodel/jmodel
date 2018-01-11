package com.github.jmodel.api.control;

import java.util.Properties;

import com.github.jmodel.ModelException;
import com.github.jmodel.config.Configurable;

/**
 * Feature
 * 
 * @author jianni@hotmail.com
 *
 * @param <T>
 *            value type of return
 */
public abstract class Feature<I, T> implements Configurable {

	private Properties properties;

	public T serve(ServiceContext<?> ctx, I inputObject, Object... args) throws ModelException {

		//TODO handle trace, security, ...
		
		return perform(inputObject, args);
	}

	protected abstract T perform(I inputObject, Object... args) throws ModelException;

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
		return "Feature";
	}

}
