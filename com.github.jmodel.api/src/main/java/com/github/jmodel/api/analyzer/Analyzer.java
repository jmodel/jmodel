package com.github.jmodel.api.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.jmodel.ModelException;
import com.github.jmodel.api.entity.Array;
import com.github.jmodel.api.entity.Entity;
import com.github.jmodel.api.entity.Field;
import com.github.jmodel.api.entity.Model;

/**
 * Abstract class for {@code Analyzer}. An input object will be transformed to
 * generic model by specified analyzer.
 * <p>
 * Analyzer can build the generic model from the scratch, or update the existing
 * generic model.
 * 
 * @author jianni@hotmail.com
 *
 */
public abstract class Analyzer<T> {

	/**
	 * Build generic model instance from the scratch.
	 * 
	 * @param inputObject
	 *            input object
	 * @param modelName
	 *            root model name
	 * @return Model the generic model
	 * @throws ModelException
	 *             model exception
	 */
	public abstract Model build(T inputObject, String modelName) throws ModelException;

	/**
	 * Update generic model instance.
	 * 
	 * @param model
	 * @param inputObject
	 * @throws ModelException
	 */
	public void update(Model model, T inputObject) throws ModelException {
		setValueOfFields(model, new HashMap<String, Field>(), new HashMap<String, Model>(), inputObject);
	}

	protected abstract void buildModel(final Model model, final Map<String, Field> fieldPathMap,
			final Map<String, Model> modelPathMap, final String nodeName, final T node) throws ModelException;

	/**
	 * Set the value of fields for a model.
	 * 
	 * @param model
	 *            generic model instance
	 * @param fieldPathMap
	 *            help to set value
	 * @param modelPathMap
	 *            help to set value
	 * @param node
	 *            a node
	 */
	protected void setValueOfFields(final Model model, final Map<String, Field> fieldPathMap,
			final Map<String, Model> modelPathMap, final T node) throws ModelException {
		if (model != null) {
			model.setModelPathMap(modelPathMap);
			model.setFieldPathMap(fieldPathMap);
			if (model instanceof Entity) {
				// root
				if (model.getModelPath() == null) {
					model.setFieldPathMap(fieldPathMap);
					model.setModelPath(model.getName());
				}

				List<Field> fields = ((Entity) model).getFields();
				if (fields != null) {
					for (Field field : fields) {
						setFieldValue(node, field);
						model.getFieldPathMap().put(model.getModelPath() + "." + field.getName(), field);
					}
				}

				/*
				 * for handling recursive
				 */
				final Model parentModel = model.getParentModel();
				if (parentModel != null) {
					if (parentModel instanceof Entity && model.isRecursive()) {
						Entity clonedEntity = (Entity) model.clone();
						if (getSubNode(node, clonedEntity.getName()) != null) {
							clonedEntity.setRecursive(true);
							clonedEntity.setParentModel(model);
							model.getSubModels().add(clonedEntity);
						}
					} else if (parentModel instanceof Array && parentModel.isRecursive()) {
						Array clonedArray = (Array) parentModel.clone();
						if (getSubNode(node, clonedArray.getName()) != null) {
							clonedArray.setRecursive(true);
							clonedArray.setParentModel(model);
							model.getSubModels().add(clonedArray);
						}
					}
				}
			}

			// for entity or array model
			modelPathMap.put(model.getModelPath(), model);

			List<Model> subModels = model.getSubModels();
			if (subModels != null) {
				for (Model subModel : subModels) {
					T subNode = getSubNode(node, subModel.getName());
					if (subNode == null) {
						continue;
					}

					subModel.setFieldPathMap(fieldPathMap);
					if (subModel instanceof Array) {
						subModel.setModelPath(model.getModelPath() + "." + subModel.getName() + "[]");
					} else {
						subModel.setModelPath(model.getModelPath() + "." + subModel.getName());
					}

					if (subModel instanceof Entity) {
						setValueOfFields(subModel, fieldPathMap, modelPathMap, subNode);
					} else if (subModel instanceof Array) {
						modelPathMap.put(subModel.getModelPath(), subModel);
						subModel.setModelPathMap(modelPathMap);
						subModel.setFieldPathMap(fieldPathMap);

						List<Model> subSubModels = subModel.getSubModels();
						if (subSubModels != null) {
							Model subSubModel = subSubModels.get(0);
							populateSubModel(subNode, subModel, subSubModel);
						}
					}
				}
			}
		}
	}

	protected abstract void setFieldValue(T node, Field field) throws ModelException;

	protected abstract T getSubNode(T node, String subNodeName) throws ModelException;

	protected abstract void populateSubModel(T subNode, Model subModel, Model subSubModel) throws ModelException;
}
