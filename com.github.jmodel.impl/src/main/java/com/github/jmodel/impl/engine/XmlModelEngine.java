package com.github.jmodel.impl.engine;

import org.w3c.dom.Element;

import com.github.jmodel.FormatEnum;
import com.github.jmodel.ModelException;
import com.github.jmodel.api.analyzer.Analyzer;
import com.github.jmodel.api.analyzer.AnalyzerFactoryService;
import com.github.jmodel.api.entity.Model;
import com.github.jmodel.impl.AbstractModelEngine;

/**
 * Xml model engine.
 * 
 * @author jianni@hotmail.com
 *
 */
public class XmlModelEngine extends AbstractModelEngine {

	private Analyzer<Element> analyzer = null;

	@SuppressWarnings("unchecked")
	public XmlModelEngine() {
		analyzer = (Analyzer<Element>) AnalyzerFactoryService.getInstance().getAnalyzer(FormatEnum.XML, null);
	}

	@Override
	public <T> Model build(T sourceObj, String modelName) throws ModelException {
		return getAnalyzer().build((Element) sourceObj, modelName);
	}

	@Override
	public <T> void update(Model model, T sourceObj) throws ModelException {
		getAnalyzer().update(model, (Element) sourceObj);
	}

	@Override
	public Analyzer<Element> getAnalyzer() throws ModelException {
		return analyzer;
	}

}
