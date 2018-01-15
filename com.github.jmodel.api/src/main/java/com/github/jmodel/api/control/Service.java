package com.github.jmodel.api.control;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.github.jmodel.ModelException;
import com.github.jmodel.config.Configurable;
import com.github.jmodel.config.ConfigurationLoader;

/**
 * Service interface.
 * 
 * @author jianni@hotmail.com
 * 
 * @param <T>
 *            value type of return
 * @param <R>
 *            value type of request
 *
 */
public abstract class Service<T, R> implements Configurable {

	private static Properties properties = new Properties();

	private static Map<String, Path> pathMap = new ConcurrentHashMap<String, Path>();

	public T serve(ServiceContext<?> ctx, R request) throws ModelException {
		return serve(ctx, request, null);
	}

	/**
	 * Perform service work.
	 * 
	 * @param ctx
	 *            service context
	 * @param request
	 *            request
	 * @param path
	 *            path
	 * @return arbitrary object
	 * @throws ModelException
	 *             ModelException
	 */
	public T serve(ServiceContext<?> ctx, R request, String path) throws ModelException {

		ctx.setConf(ConfigurationLoader.getInstance().getConfiguration());

		return perform(ctx, request, path);
	}

	protected abstract T perform(ServiceContext<?> ctx, R request, String path) throws ModelException;

	public Properties getProperties() {
		return properties;
	}

	public String getProperty(String key) {
		return (String) properties.get(key);
	}

	public Map<String, Path> getPathMap() {
		return pathMap;
	}

	public Path getPath(String key) {
		return pathMap.get(key);
	}

	public static String getRegionId() {
		return "Service";
	}

}
