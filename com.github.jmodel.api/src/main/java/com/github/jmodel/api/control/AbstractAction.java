package com.github.jmodel.api.control;

import com.github.jmodel.config.Configurable;

/**
 * 
 * 
 * @author jianni@hotmail.com
 *
 */
public abstract class AbstractAction implements Configurable {

	public static String getRegionId() {
		return "Action";
	}
}
