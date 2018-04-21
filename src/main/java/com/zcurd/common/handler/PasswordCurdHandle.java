package com.zcurd.common.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zcurd.common.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码CurdHandle
 * @author 钟世云 2017.1.27
 */
public class PasswordCurdHandle implements CurdHandle {

	private static final Logger logger = LoggerFactory.getLogger(PasswordCurdHandle.class);

	@Override
	public void add(int headId, HttpServletRequest req, Map<String, String[]> paraMap) {
		String password = PasswordUtil.defaultPassword;
		String[] passwordPara = paraMap.get("model.password");
		if(passwordPara != null && passwordPara.length > 0) {
			password = passwordPara[0];
		}
		paraMap.put("model.password", new String[]{PasswordUtil.encodePassword(password)});
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
