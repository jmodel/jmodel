package com.github.jmodel.api;

import com.github.jmodel.ModelException;
import com.github.jmodel.api.analyzer.Analyzer;
import com.github.jmodel.api.entity.Model;
import com.github.jmodel.spi.ModelEngineFactory;

/**
 * Model engine is used to analyze a input object and build/update a generic
 * model instance. The generic model is a POJO, does not require any runtime
 * container.
 * 
 * @author jianni@hotmail.com
 *
 */
public interface ModelEngine {

	/**
	 * Build a generic model from the scratch.
	 * 
	 * @param sourceObj
	 *            source object
	 * @param modelName
	 *            model name
	 * @param <T>
	 *            type of source object
	 * @return Model target generic model instance
	 * @throws ModelException
	 *             exception
	 */
	public <T> Model build(T sourceObj, String modelName) throws ModelException;

	/**
	 * Set the value of fields for an existing generic model.
	 * 
	 * @param model
	 *            existing generic model
	 * @param sourceObj
	 *            source object
	 * @param <T>
	 *            type of source object
	 * @throws ModelException
	 *             exception
	 */
	public <T> void update(Model model, T sourceObj) throws ModelException;

	/**
	 * Get specified analyzer.
	 * 
	 * @return specified analyzer
	 * @throws ModelException
	 *             exception
	 */
	public Analyzer<?> getAnalyzer() throws ModelException;

	/**
	 * Set model engine factory
	 * 
	 * @param modelEngineFactory
	 *            model engine factory
	 * @throws ModelException
	 *             exception
	 */
	public void setModelEngineFactory(ModelEngineFactory modelEngineFactory) throws ModelException;

	/**
	 * Get model engine factory
	 * 
	 * @return ModelEngineFactory
	 * @throws ModelException
	 *             exception
	 */
	public ModelEngineFactory getModelEngineFactory() throws ModelException;

}
