package com.github.jmodel.api.control;

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

	public T serve(I inputObject, Object... args) throws ModelException {

		return perform(inputObject, args);
	}

	protected abstract T perform(I inputObject, Object... args) throws ModelException;

	public static String getRegionId() {
		return "Feature";
	}

}
