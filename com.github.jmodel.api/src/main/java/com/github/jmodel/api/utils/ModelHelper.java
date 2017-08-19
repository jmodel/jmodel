package com.github.jmodel.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.jmodel.ModelException;
import com.github.jmodel.api.domain.Array;
import com.github.jmodel.api.domain.Entity;
import com.github.jmodel.api.domain.Field;
import com.github.jmodel.api.domain.Model;

public class ModelHelper {

	private final static SimpleDateFormat dateFormat = new SimpleDateFormat();

	public static Date getDate(String fieldValue) throws ModelException {
		return getDate(fieldValue, null);
	}

	public static Date getDate(String fieldValue, String pattern) throws ModelException {

		try {
			if (pattern == null) {
				return dateFormat.parse(fieldValue);
			}
			return new SimpleDateFormat(pattern).parse(fieldValue);
		} catch (ParseException e) {
			throw new ModelException("Failed to parse date string", e);
		}
	}

	public static String getFieldValue(Field field) {
		if (field == null) {
			return null;
		}
		return field.getValue();
	}

	/**
	 * Populate a model by configured field paths
	 * 
	 * @param root
	 *            target generic model
	 * @param fieldPaths
	 *            field path list
	 * @param modelRecursiveMap
	 *            model recursive map
	 */
	public static void populateModel(final Model root, final List<String> fieldPaths,
			final Map<String, Boolean> modelRecursiveMap) {
		final Map<String, Object> modelOrFieldMap = new HashMap<String, Object>();

		for (String fieldPath : fieldPaths) {

			String[] paths = fieldPath.split("\\.");

			String currentPath = "";
			String parentPath = "";

			for (int i = 0; i < paths.length - 1; i++) {

				if (parentPath.equals("")) {
					currentPath = paths[i];
				} else {
					currentPath = parentPath.replace("[]", "[0]") + "." + paths[i];
				}
				Model currentModel = (Model) modelOrFieldMap.get(currentPath);
				if (currentModel == null) {
					// create model object
					if (parentPath.equals("")) {
						currentModel = root;
					} else {
						if (paths[i].indexOf("[]") != -1) {
							currentModel = new Array();
							currentModel.setName(StringUtils.substringBefore(paths[i], "[]"));
						} else {
							currentModel = new Entity();
							currentModel.setName(paths[i]);
						}
					}
					if (currentModel.getName() == null) {
						currentModel.setName(paths[i]);
					}
					Boolean recursive = modelRecursiveMap.get(currentPath.replaceAll("\\[0\\]", "\\[\\]"));
					if (recursive != null && recursive) {
						currentModel.setRecursive(true);
					}

					currentModel.setModelPath(currentPath);
					modelOrFieldMap.put(currentPath, currentModel);

					// if current Model is Array,create a existing entity
					if (currentModel instanceof Array) {
						Entity subEntity = new Entity();
						String entityName = StringUtils.substringBefore(paths[i], "[]");
						subEntity.setName(entityName);
						currentPath = currentPath.replace("[]", "[0]");
						subEntity.setModelPath(currentPath);
						subEntity.setParentModel(currentModel);
						currentModel.getSubModels().add(subEntity);
						modelOrFieldMap.put(currentPath, subEntity);

					}

					// maintenence parent model relation
					Model parentModel = (Model) modelOrFieldMap.get(parentPath.replaceAll("\\[\\]", "\\[0\\]"));
					if (parentModel != null) {
						currentModel.setParentModel(parentModel);
						List<Model> subModelList = parentModel.getSubModels();
						if (subModelList == null) {
							subModelList = new ArrayList<Model>();
							parentModel.setSubModels(subModelList);
						}
						subModelList.add(currentModel);

					}

				}
				// set parentPath
				parentPath = currentPath;
			}

			// set field list
			String fieldName = paths[paths.length - 1];
			if (!fieldName.equals("_")) {
				currentPath = currentPath + "." + fieldName;
				Field currentField = (Field) modelOrFieldMap.get(currentPath);
				if (currentField == null) {
					currentField = new Field();
					currentField.setName(fieldName);
					modelOrFieldMap.put(currentPath, currentField);
					Entity currentModel = null;
					Object model = modelOrFieldMap.get(parentPath);
					if (model instanceof Entity) {
						currentModel = (Entity) model;
					} else if (model instanceof Array) {
						Array aModel = (Array) model;
						currentModel = (Entity) aModel.getSubModels().get(0);
					}
					List<Field> fieldList = currentModel.getFields();
					if (fieldList == null) {
						fieldList = new ArrayList<Field>();
						currentModel.setFields(fieldList);
					}
					fieldList.add(currentField);
				}
			}
		}
	}

	public static <T1, T2> String calc(final T1 leftOperant, final T2 rightOperant, final OperationEnum operation)
			throws ModelException {

		switch (operation) {
		case PLUS: {
			if (leftOperant instanceof String || rightOperant instanceof String) {
				return String.valueOf(leftOperant) + String.valueOf(rightOperant);
			} else if (leftOperant instanceof Integer && rightOperant instanceof Integer) {
				return String.valueOf((Integer) leftOperant + (Integer) rightOperant);
			}
			return null;
		}
		case MULTIPLY: {
			return String.valueOf(Integer.valueOf((String) leftOperant) * Integer.valueOf((String) rightOperant));
		}
		default:
			throw new ModelException("The operation is not support : " + operation);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Boolean predict(final Comparable leftOperant, final Comparable rightOperant,
			final OperationEnum operation) throws ModelException {

		if (leftOperant == null || rightOperant == null) {
			switch (operation) {
			case EQUALS: {
				return leftOperant == rightOperant;
			}
			case NOTEQUALS: {
				return leftOperant != rightOperant;
			}
			default:
				throw new ModelException("The operation is not support : " + operation);
			}
		}
		int compareResult = leftOperant.compareTo(rightOperant);
		switch (operation) {
		case EQUALS: {
			return compareResult == 0 ? true : false;
		}
		case NOTEQUALS: {
			return compareResult != 0 ? true : false;
		}
		case GT: {
			return compareResult > 0 ? true : false;
		}
		case GTE: {
			return (compareResult > 0 || compareResult == 0) ? true : false;
		}
		case LT: {
			return compareResult < 0 ? true : false;
		}
		case LTE: {
			return (compareResult < 0 || compareResult == 0) ? true : false;
		}
		default:
			throw new ModelException("The operation is not support : " + operation);
		}
	}

	public static Boolean predict(final String leftOperant, final List<String> rightOperant,
			final OperationEnum operation) throws ModelException {

		switch (operation) {
		case IN: {
			return rightOperant.contains(leftOperant);
		}
		case NOTIN: {
			return !rightOperant.contains(leftOperant);
		}
		default:
			throw new ModelException("The operation is not support : " + operation);
		}

	}

	public static Boolean predict(final Boolean leftOperant, final Boolean rightOperant, final OperationEnum operation)
			throws ModelException {

		switch (operation) {
		case OR: {
			return Boolean.logicalOr(leftOperant, rightOperant);
		}
		case AND: {
			return Boolean.logicalAnd(leftOperant, rightOperant);
		}
		default:
			throw new ModelException("The operation is not support : " + operation);
		}
	}
}
