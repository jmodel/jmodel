package com.github.jmodel.spi;

import com.github.jmodel.FormatEnum;
import com.github.jmodel.api.analyzer.Analyzer;

/**
 * Analyzer factory interface.
 * 
 * @author jianni@hotmail.com
 *
 */
public interface AnalyzerFactory {

	public Analyzer<?> getAnalyzer(FormatEnum fromFormat, String extendAnalyzerName);
}
