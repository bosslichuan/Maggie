package com.zcurd.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 刷新表单CurdHandle
 * @author 钟世云 2016.11.3
 */
public class FlushFormCurdHandle implements CurdHandle {

	private static final Logger logger = LoggerFactory.getLogger(FlushFormCurdHandle.class);


	@Override
	public void add(int headId, HttpServletRequest req, Map<String, String[]> paraMap) {
		logger.info("------------------CurdHandle to add!");
		
	}

	@Override
	public void update(int headId, HttpServletRequest req, Map<String, String[]> paraMap) {
		logger.info("------------------CurdHandle to update!");
		
	}

	@Override
	public void delete(int headId, HttpServletRequest req, Map<String, String[]> paraMap) {
		logger.info("------------------CurdHandle to delete!");
		
	}

}
