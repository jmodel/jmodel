package com.github.jmodel.config;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class ConfigurationLoader {

	private static ConfigurationLoader configurationLoader;

	private static String _filePath;

	private Configuration configuration;

	private ConfigurationLoader() {

	}

	public static ConfigurationLoader getInstance() {

		if (configurationLoader == null) {
			configurationLoader = new ConfigurationLoader();
		}
		return configurationLoader;
	}

	public static synchronized void setPath(String filePath) {

		_filePath = filePath;
	}

	public synchronized Configuration reLoad() {

		try {

			if (_filePath == null) {
				_filePath = "config.xml";
			}

			InputStream cfis = this.getClass().getClassLoader().getResourceAsStream(_filePath);
			JAXBContext jc = JAXBContext.newInstance(Configuration.class);
			Unmarshaller u = jc.createUnmarshaller();
			configuration = (Configuration) u.unmarshal(cfis);

			return configuration;
		} catch (Exception e) {
			
			return null;
		}
	}

	public Configuration getConfiguration() {

		if (configuration == null) {
			synchronized (ConfigurationLoader.class) {
				if (configuration != null) {
					return configuration;
				}

				// load default config.xml
				configuration = reLoad();
			}
		}
		return configuration;
	}
}
