package com.github.jmodel.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

import com.github.jmodel.FormatEnum;
import com.github.jmodel.spi.ModelEngineFactory;

/**
 * Model engine factory service is used to create/get model engine factory
 * implementation.
 * 
 * @author jianni@hotmail.com
 *
 */
public class ModelEngineFactoryService {

	private final static Logger logger = Logger.getLogger(ModelEngineFactoryService.class.getName());

	private static ModelEngineFactoryService service;

	private ServiceLoader<ModelEngineFactory> loader;

	private Map<ModelEngineFactory, GenericKeyedObjectPool<String, ModelEngine>> poolMap;

	private ModelEngineFactoryService() {
		loader = ServiceLoader.load(ModelEngineFactory.class);
		poolMap = new HashMap<ModelEngineFactory, GenericKeyedObjectPool<String, ModelEngine>>();
	}

	public static synchronized ModelEngineFactoryService getInstance() {

		if (service == null) {
			service = new ModelEngineFactoryService();
		}
		return service;
	}

	public ModelEngine getModelEngine(FormatEnum format, Class<?> extFormatClz) {

		ModelEngine modelEngine = null;

		try {

			Iterator<ModelEngineFactory> modelEngineFactorys = loader.iterator();
			while (modelEngine == null && modelEngineFactorys.hasNext()) {
				ModelEngineFactory modelEngineFactory = modelEngineFactorys.next();
				GenericKeyedObjectPool<String, ModelEngine> pool = poolMap.get(modelEngineFactory);
				if (pool == null) {
					pool = new GenericKeyedObjectPool<String, ModelEngine>(modelEngineFactory);
					poolMap.put(modelEngineFactory, pool);
				}
				if (extFormatClz != null) {
					modelEngine = pool.borrowObject(format + "_" + extFormatClz.getName());
				} else {
					modelEngine = pool.borrowObject(String.valueOf(format));
				}
				modelEngine.setModelEngineFactory(modelEngineFactory);
			}

		} catch (ServiceConfigurationError serviceError) {
			modelEngine = null;
			serviceError.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.WARNING, e, () -> "Failed to get model engine from pool");
		}

		return modelEngine;
	}

	public void releaseModelEngine(FormatEnum format, Class<?> extFormatClz, ModelEngine modelEngine) {
		try {
			if (extFormatClz != null) {
				poolMap.get(modelEngine.getModelEngineFactory()).returnObject(format + "_" + extFormatClz.getName(),
						modelEngine);
			} else {
				poolMap.get(modelEngine.getModelEngineFactory()).returnObject(String.valueOf(format), modelEngine);
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, e, () -> "Failed to release model engine to pool");
		}
	}

}
