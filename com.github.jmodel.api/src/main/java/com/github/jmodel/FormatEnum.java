package com.github.jmodel;

/**
 * Format enumeration is used to find appropriate analyzer. XML and JSON is
 * build-in format because the both are very popular.
 * <p>
 * Actually, the input object could be in various format, e.g. build generic
 * model from JSON, the data format could be file, string, inputstream, etc. For
 * avoiding to handle these data format which has been considered by third party
 * lib like jackson, JsonNode is only format allowed. The third party lib manage
 * the pre converting.
 * 
 * @author jianni@hotmail.com
 *
 */
public enum FormatEnum {

	XML/* org.w3c.dom.Element */, JSON/* com.fasterxml.jackson.databind.JsonNode */, OTHER
}
