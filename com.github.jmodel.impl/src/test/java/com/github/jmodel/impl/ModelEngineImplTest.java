package com.github.jmodel.impl;

import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jmodel.FormatEnum;
import com.github.jmodel.ModelBuilder;
import com.github.jmodel.api.domain.Model;

public class ModelEngineImplTest {

	private Model model;

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testConstruct() {

		InputStream sourceObj = null;

		try {

			sourceObj = getClass().getResourceAsStream("data.json");
			model = ModelBuilder.build(FormatEnum.JSON, mapper.readTree(sourceObj), "jmodelx");
			if (model == null) {
				fail("Not yet implemented");
			}

			System.out.println(model.getName());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sourceObj != null) {
				try {
					sourceObj.close();
				} catch (Exception e) {

				}
			}
		}

		try {

			sourceObj = getClass().getResourceAsStream("data.json");
			model = ModelBuilder.build(FormatEnum.JSON, mapper.readTree(sourceObj), "jmodel");
			if (model == null) {
				fail("Not yet implemented");
			}

			System.out.println(model.getName());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sourceObj != null) {
				try {
					sourceObj.close();
				} catch (Exception e) {

				}
			}
		}
	}
}
