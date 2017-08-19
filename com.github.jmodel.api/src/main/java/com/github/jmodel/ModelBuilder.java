package com.github.jmodel;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.jmodel.api.ModelEngine;
import com.github.jmodel.api.ModelEngineFactoryService;
import com.github.jmodel.api.domain.Model;

/**
 * ModelBuilder
 * 
 * @author jianni@hotmail.com
 *
 */
public class ModelBuilder {

	private final static Logger logger = Logger.getLogger(ModelBuilder.class.getName());

	public static <T> Model build(FormatEnum format, T inputObj, String modelName) throws ModelException {
		return build(format, inputObj, null, modelName);
	}

	public static <T> Model build(FormatEnum format, T inputObj, Class<T> extFormatClz, String modelName)
			throws ModelException {

		checkParameter(format, inputObj, extFormatClz);

		logger.log(Level.INFO, () -> "Try to build generic model");

		ModelEngine modelEngine = null;

		try {
			modelEngine = ModelEngineFactoryService.getInstance().getModelEngine(format, extFormatClz);
			return modelEngine.build(inputObj, (modelName == null ? "undefined" : modelName));
		} catch (Exception e) {
			throw new ModelException("Failed to build generic model", e);
		} finally {
			ModelEngineFactoryService.getInstance().releaseModelEngine(format, extFormatClz, modelEngine);
		}
	}

	public static <T> void update(FormatEnum format, Model model, T inputObj) throws ModelException {
		update(format, model, inputObj, null);
	}

	public static <T> void update(FormatEnum format, Model model, T inputObj, Class<T> extFormatClz)
			throws ModelException {

		checkParameter(format, inputObj, extFormatClz);

		if (model == null) {
			throw new ModelException("The generic model updated is null");
		}

		logger.log(Level.INFO, () -> "Try to update generic model");

		ModelEngine modelEngine = null;

		try {
			modelEngine = ModelEngineFactoryService.getInstance().getModelEngine(format, extFormatClz);
			modelEngine.update(model, inputObj);
		} catch (Exception e) {
			throw new ModelException("Failed to update generic model", e);
		} finally {
			ModelEngineFactoryService.getInstance().releaseModelEngine(format, extFormatClz, modelEngine);
		}
	}

	private static void checkParameter(FormatEnum format, Object inputObj, Class<?> extFormatClz)
			throws ModelException {

		if (format == null) {
			throw new ModelException("Format is not specified");
		} else if (format == FormatEnum.OTHER && extFormatClz == null) {
			throw new ModelException("Format extension class is not specified");
		}

		if (inputObj == null) {
			throw new ModelException("Input object is null");
		}

	}
}
