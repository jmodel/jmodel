package com.github.jmodel.api.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Array.
 * 
 * @author jianni@hotmail.com
 *
 */
public class Array extends Model {

	private static final long serialVersionUID = 8954603537368690081L;

	@Override
	public Model clone() {
		
		Array clonedArray = new Array();
		clonedArray.setName(this.getName() + "");
		clonedArray.setRecursive(this.isRecursive());

		List<Model> subModels = this.getSubModels();
		if (subModels != null) {
			clonedArray.setSubModels(new ArrayList<Model>());

			for (Model subModel : subModels) {
				Model clonedSubModel = subModel.clone();
				clonedArray.getSubModels().add(clonedSubModel);
				clonedSubModel.setParentModel(clonedArray);
			}
		}

		return clonedArray;
	}
}
