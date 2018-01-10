package com.github.jmodel.api.control;

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
	public T serve(ServiceContext<?> ctx, R request, String... path) throws ModelException {

		ctx.setConf(ConfigurationLoader.getInstance().getConfiguration());

		return perform(ctx, request, path);
	}

	protected abstract T perform(ServiceContext<?> ctx, R request, String... path) throws ModelException;

	public static String getRegionId() {
		return "Service";
	}

}
