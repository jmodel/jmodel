package com.github.jmodel.impl.analyzer;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jmodel.ModelException;
import com.github.jmodel.api.analyzer.Analyzer;
import com.github.jmodel.api.domain.Array;
import com.github.jmodel.api.domain.Entity;
import com.github.jmodel.api.domain.Field;
import com.github.jmodel.api.domain.Model;

/**
 * Json analyzer.
 * 
 * @author jianni@hotmail.com
 *
 */
public class JsonAnalyzer extends Analyzer<JsonNode> {

	@Override
	public Model build(JsonNode sourceObject, String modelName) throws ModelException {
		Model model = new Entity();
		model.setName(modelName);
		buildModel(model, new HashMap<String, Field>(), new HashMap<String, Model>(), "", sourceObject);
		return model;
	}

	@Override
	protected void buildModel(final Model sourceModel, final Map<String, Field> fieldPathMap,
			final Map<String, Model> modelPathMap, final String nodeName, final JsonNode node) {

		if (node.isObject()) {
			final ObjectNode objectNode = (ObjectNode) node;

			if (nodeName.trim().length() == 0) {
				sourceModel.setModelPathMap(modelPathMap);
				sourceModel.setFieldPathMap(fieldPathMap);
				sourceModel.setName(sourceModel.getName());
				sourceModel.setModelPath(sourceModel.getName());
				sourceModel.getModelPathMap().put(sourceModel.getName(), sourceModel);

				objectNode.fields().forEachRemaining((subNode) -> {
					if (subNode.getValue().isObject() || subNode.getValue().isArray()) {
						buildModel(sourceModel, sourceModel.getFieldPathMap(), sourceModel.getModelPathMap(),
								subNode.getKey(), subNode.getValue());
					} else {
						Field field = new Field();
						field.setName(subNode.getKey());
						field.setValue(subNode.getValue().asText());
						field.setParentEntity((Entity) sourceModel);
						((Entity) sourceModel).getFields().add(field);
						sourceModel.getFieldPathMap().put(sourceModel.getModelPath() + '.' + field.getName(), field);
					}
				});
			} else {
				final Entity entity = new Entity();
				entity.setName(nodeName);
				if (sourceModel instanceof Array) {
					int subModelsCount = sourceModel.getSubModels().size();
					if (subModelsCount > 0) {
						entity.setModelPath(
								sourceModel.getModelPath() + "." + entity.getName() + "[" + (subModelsCount - 1) + "]");
					} else {
						entity.setModelPath(
								sourceModel.getModelPath() + "." + entity.getName() + "[" + subModelsCount + "]");
					}
				} else {
					entity.setModelPath(sourceModel.getModelPath() + "." + entity.getName());
				}
				entity.setParentModel(sourceModel);
				entity.setFieldPathMap(sourceModel.getFieldPathMap());
				entity.setModelPathMap(sourceModel.getModelPathMap());
				sourceModel.getSubModels().add(entity);
				sourceModel.getModelPathMap().put(entity.getModelPath(), entity);

				// process child nodes
				objectNode.fields().forEachRemaining((subNode) -> {
					if (subNode.getValue().isObject() || subNode.getValue().isArray()) {
						buildModel(entity, entity.getFieldPathMap(), entity.getModelPathMap(), subNode.getKey(),
								subNode.getValue());
					} else {
						Field field = new Field();
						field.setName(subNode.getKey());
						field.setValue(subNode.getValue().asText());
						field.setParentEntity(entity);

						entity.getFields().add(field);
						sourceModel.getFieldPathMap().put(entity.getModelPath() + '.' + field.getName(), field);
					}
				});
			}
		} else if (node.isArray()) {
			ArrayNode arrayNode = (ArrayNode) node;

			final Array array = new Array();
			array.setName(nodeName);
			array.setModelPath(sourceModel.getModelPath() + "." + array.getName() + "[]");
			array.setParentModel(sourceModel);
			array.setFieldPathMap(sourceModel.getFieldPathMap());
			array.setModelPathMap(sourceModel.getModelPathMap());
			sourceModel.getSubModels().add(array);
			sourceModel.getModelPathMap().put(array.getModelPath(), array);

			arrayNode.forEach((subNode) -> {
				buildModel(array, array.getFieldPathMap(), array.getModelPathMap(), nodeName, subNode);
			});
		}
	}

	@Override
	protected void setFieldValue(JsonNode jsonNode, Field field) {
		JsonNode node = jsonNode.get(field.getName());
		if (node == null) {
			field.setValue(null);
		} else {
			field.setValue(node.asText());
		}

	}

	@Override
	protected JsonNode getSubNode(JsonNode node, String subNodeName) {
		return node.path(subNodeName);
	}

	@Override
	protected void populateSubModel(JsonNode subNode, Model subModel, Model subSubModel) throws ModelException {

		for (int i = 0; i < subNode.size(); i++) {
			JsonNode subSubJsonNode = subNode.get(i);

			Model clonedSubSubModel = null;
			if (i == 0) {
				clonedSubSubModel = subSubModel;
			} else {
				try {
					clonedSubSubModel = subModel.getSubModels().get(i);
				} catch (Exception e) {
				}
				if (clonedSubSubModel == null) {
					clonedSubSubModel = subSubModel.clone();
					subModel.getSubModels().add(clonedSubSubModel);
				} else {
					clonedSubSubModel.setParentModel(subModel);
				}

			}
			clonedSubSubModel.setModelPath(subModel.getModelPath() + "." + clonedSubSubModel.getName() + "[" + i + "]");
			clonedSubSubModel.setFieldPathMap(subModel.getFieldPathMap());

			setValueOfFields(clonedSubSubModel, subModel.getFieldPathMap(), subModel.getModelPathMap(), subSubJsonNode);
		}
	}

}
