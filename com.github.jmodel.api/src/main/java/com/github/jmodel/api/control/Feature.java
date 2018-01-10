package com.github.jmodel.api.control;

import com.github.jmodel.config.Configurable;

/**
 * Feature
 * 
 * @author jianni@hotmail.com
 *
 * @param <T>
 *            value type of return
 */
public abstract class Feature<T> implements Configurable {

	public abstract T perform(ServiceContext<?> ctx, Object... args);

	public static String getRegionId() {
		return "Feature";
	}

}
