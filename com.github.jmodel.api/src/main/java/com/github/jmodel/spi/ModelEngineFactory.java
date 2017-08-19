package com.github.jmodel.spi;

import org.apache.commons.pool2.KeyedPooledObjectFactory;

import com.github.jmodel.api.ModelEngine;

/**
 * Model engine factory is a keyed pooled object factory.
 * 
 * @author jianni@hotmail.com
 *
 */
public interface ModelEngineFactory extends KeyedPooledObjectFactory<String, ModelEngine> {

}
