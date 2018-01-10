package com.github.jmodel;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.jmodel.api.ModelEngine;
import com.github.jmodel.api.ModelEngineFactoryService;
import com.github.jmodel.api.entity.Model;

/**
 * Public API for generic model builder.
 * 
 * @author jianni@hotmail.com
 *
 */
public class ModelBuilder {

	/**
	 * JDK Logger
	 */
	private final static Logger logger = Logger.getLogger(ModelBuilder.class.getName());

	/**
	 * Build generic model from the scratch. The input object should be in the
	 * format specified by the first parameter.
	 * 
	 * @param format
	 *            format of input object
	 * @param inputObj
	 *            input object
	 * @param modelName
	 *            model name
	 * @return generic model
	 * @throws ModelException
	 *             model exception
	 */
	public static <T> Model build(FormatEnum format, T inputObj, String modelName) throws ModelException {
		return build(format, inputObj, null, modelName);
	}

	/**
	 * Build generic model from the scratch. The input object should be in the
	 * format specified by the third parameter named extFormatClz. The first
	 * parameter always be FormatEnum.OTHER. The extention format class is provided
	 * by customization.
	 * 
	 * @param format
	 *            format of input object
	 * @param inputObj
	 *            input object
	 * @param extFormatClz
	 *            extention format class
	 * @param modelName
	 *            model name
	 * @return generic model
	 * @throws ModelException
	 *             model exception
	 */
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

	/**
	 * Update the exsiting generic model instance by input object.The input object
	 * should be in the format specified by the first parameter.
	 * 
	 * @param format
	 *            format of input object
	 * @param model
	 *            the existing generic model instance
	 * @param inputObj
	 *            input object
	 * @throws ModelException
	 *             model exception
	 */
	public static <T> void update(FormatEnum format, Model model, T inputObj) throws ModelException {
		update(format, model, inputObj, null);
	}

	/**
	 * Update the exsiting generic model instance by input object.The input object
	 * should be in the format specified by the fouth parameter named extFormatClz.
	 * 
	 * @param format
	 *            format of input object
	 * @param model
	 *            the existing generic model instance
	 * @param inputObj
	 *            input object
	 * @param extFormatClz
	 *            extention format class
	 * @throws ModelException
	 *             model exception
	 */
	public static <T> void update(FormatEnum format, Model model, T inputObj, Class<T> extFormatClz)
			throws ModelException {

		checkParameter(format, inputObj, extFormatClz);

		if (model == null) {
			throw new ModelException("The generic model is null");
		}

		logger.log(Level.INFO, () -> "Try to update the generic model");

		ModelEngine modelEngine = null;

		try {
			modelEngine = ModelEngineFactoryService.getInstance().getModelEngine(format, extFormatClz);
			modelEngine.update(model, inputObj);
		} catch (Exception e) {
			throw new ModelException("Failed to update the generic model", e);
		} finally {
			ModelEngineFactoryService.getInstance().releaseModelEngine(format, extFormatClz, modelEngine);
		}
	}

	/**
	 * Check the parameters is legal or not.
	 * 
	 * @param format
	 *            format of input object
	 * @param inputObj
	 *            input object
	 * @param extFormatClz
	 *            extention format class
	 * @throws ModelException
	 *             model exception
	 */
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
