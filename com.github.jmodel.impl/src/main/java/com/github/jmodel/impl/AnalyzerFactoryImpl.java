package com.github.jmodel.impl;

import com.github.jmodel.FormatEnum;
import com.github.jmodel.api.analyzer.Analyzer;
import com.github.jmodel.impl.analyzer.JsonAnalyzer;
import com.github.jmodel.impl.analyzer.XmlAnalyzer;
import com.github.jmodel.spi.AnalyzerFactory;

/**
 * Analyzer factory implementation.
 * 
 * @author jianni@hotmail.com
 *
 */
public class AnalyzerFactoryImpl implements AnalyzerFactory {

	@Override
	public Analyzer<?> getAnalyzer(FormatEnum format, String extendAnalyzerName) {
		
		switch (format) {
		case JSON:
			return new JsonAnalyzer();
		case XML:
			return new XmlAnalyzer();
		default:
			return null;
		}
	}

}
