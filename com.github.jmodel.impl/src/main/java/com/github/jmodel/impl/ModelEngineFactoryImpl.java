package com.github.jmodel.impl;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.github.jmodel.api.ModelEngine;
import com.github.jmodel.impl.engine.JsonModelEngine;
import com.github.jmodel.spi.ModelEngineFactory;

/**
 * Model engine factory implementation.
 * 
 * @author jianni@hotmail.com
 *
 */
public class ModelEngineFactoryImpl extends BaseKeyedPooledObjectFactory<String, ModelEngine>
		implements ModelEngineFactory {

	@Override
	public ModelEngine create(String format) throws Exception {

		if (format.equals("JSON")) {
			return new JsonModelEngine();
		} else {
			return null;
		}
	}

	@Override
	public PooledObject<ModelEngine> wrap(ModelEngine value) {
		return new DefaultPooledObject<ModelEngine>(value);
	}
}
