package com.github.jmodel.impl.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jmodel.FormatEnum;
import com.github.jmodel.ModelException;
import com.github.jmodel.api.analyzer.Analyzer;
import com.github.jmodel.api.analyzer.AnalyzerFactoryService;
import com.github.jmodel.api.domain.Model;
import com.github.jmodel.impl.AbstractModelEngine;

/**
 * Json model engine.
 * 
 * @author jianni@hotmail.com
 *
 */
public class JsonModelEngine extends AbstractModelEngine {

	private Analyzer<JsonNode> analyzer;

	@SuppressWarnings("unchecked")
	public JsonModelEngine() {
		analyzer = (Analyzer<JsonNode>) AnalyzerFactoryService.getInstance().getAnalyzer(FormatEnum.JSON, null);
	}

	@Override
	public <T> Model build(T sourceObj, String modelName) throws ModelException {

		JsonNode jsonNode = null;
		try {
			jsonNode = (JsonNode) sourceObj;
		} catch (ClassCastException e) {
			throw new ModelException("JsonNode object is required", e);
		}
		return getAnalyzer().build(jsonNode, modelName);
	}

	@Override
	public <T> void update(Model model, T sourceObj) throws ModelException {

		JsonNode jsonNode = null;
		try {
			jsonNode = (JsonNode) sourceObj;
		} catch (ClassCastException e) {
			throw new ModelException("JsonNode object is required", e);
		}
		getAnalyzer().update(model, jsonNode);
	}

	@Override
	public Analyzer<JsonNode> getAnalyzer() throws ModelException {
		return analyzer;
	}

}
