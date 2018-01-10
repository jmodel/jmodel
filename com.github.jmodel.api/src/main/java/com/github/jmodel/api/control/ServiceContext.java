package com.github.jmodel.api.control;

import com.github.jmodel.config.Configuration;

/**
 * Service context
 * 
 * @author jianni@hotmail.com
 *
 */
public class ServiceContext<D> {

	private Configuration conf;

	private Long ownerId;

	private Long traceId;

	private D session;

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getTraceId() {
		return traceId;
	}

	public void setTraceId(Long traceId) {
		this.traceId = traceId;
	}

	public D getSession() {
		return session;
	}

	public void setSession(D session) {
		this.session = session;
	}

}
