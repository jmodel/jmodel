package com.github.jmodel.impl;

import com.github.jmodel.ModelException;
import com.github.jmodel.api.ModelEngine;
import com.github.jmodel.spi.ModelEngineFactory;

/**
 * Abstract model engine.
 * 
 * @author jianni@hotmail.com
 *
 */
public abstract class AbstractModelEngine implements ModelEngine {

	protected ModelEngineFactory modelEngineFactory;

	@Override
	public void setModelEngineFactory(ModelEngineFactory modelEngineFactory) throws ModelException {
		this.modelEngineFactory = modelEngineFactory;
	}

	@Override
	public ModelEngineFactory getModelEngineFactory() throws ModelException {
		return modelEngineFactory;
	}

}
