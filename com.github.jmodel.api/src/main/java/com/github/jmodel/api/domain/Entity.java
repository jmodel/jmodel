package com.github.jmodel.api.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity
 * 
 * @author jianni@hotmail.com
 *
 */
public class Entity extends Model {

	private List<Field> fields = new ArrayList<Field>();

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Field getField(String fieldName) {
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	@Override
	public Model clone() {
		Entity clonedEntity = new Entity();

		clonedEntity.setName(this.getName() + "");
		clonedEntity.setRecursive(this.isRecursive());

		if (this.getFields() != null) {
			clonedEntity.setFields(new ArrayList<Field>());
			for (Field field : this.getFields()) {
				clonedEntity.getFields().add(field.clone());
			}
		}

		List<Model> subModels = this.getSubModels();
		if (subModels != null) {
			clonedEntity.setSubModels(new ArrayList<Model>());

			for (Model subModel : subModels) {
				Model clonedSubModel = subModel.clone();
				clonedEntity.getSubModels().add(clonedSubModel);
				clonedSubModel.setParentModel(clonedEntity);
			}
		}

		return clonedEntity;
	}

}
