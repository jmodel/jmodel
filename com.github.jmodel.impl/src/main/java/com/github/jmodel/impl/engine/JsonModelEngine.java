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

		return getAnalyzer().build((JsonNode) sourceObj, modelName);

		// throw new ModelException("not json node type");

	}

	@Override
	public <T> void update(Model model, T sourceObj) throws ModelException {

		getAnalyzer().update(model, (JsonNode) sourceObj);

		// throw new ModelException("not json node type");

	}

	@Override
	public Analyzer<JsonNode> getAnalyzer() throws ModelException {
		return analyzer;
	}

}
