package com.oracle.storage.helper;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

	/*
	 * @Override public void filter(ContainerRequestContext request,
	 * ContainerResponseContext response) throws IOException {
	 * response.getHeaders().add("Access-Control-Allow-Origin", "*");
	 * response.getHeaders().add("Access-Control-Allow-Headers",
	 * "origin, content-type, accept, authorization");
	 * response.getHeaders().add("Access-Control-Allow-Credentials", "true");
	 * response.getHeaders().add("Access-Control-Allow-Methods",
	 * "GET, POST, PUT, DELETE, OPTIONS, HEAD"); }
	 */
	final static Logger logger = Logger.getLogger(CORSFilter.class.getSimpleName());

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		logger.info("Filter Activated");

		HttpServletRequest request = (HttpServletRequest) req;
		logger.info("CORSFilter HTTP Request: " + request.getMethod());

		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, HEAD");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
		chain.doFilter(request, response);

		logger.info("Filter Deactivated");
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}

}
