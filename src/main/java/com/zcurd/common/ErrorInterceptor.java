package com.zcurd.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 *  错误消息
 * @author 钟世云 2017年1月28日 上午10:59:52
 */
public class ErrorInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		HttpServletRequest request = c.getRequest();
		
		try {
			inv.invoke();
		} catch (ErrorMsgException e) {
			//返回失败结果。（页面会显示错误消息）
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("result", "fail");
			result.put("msg", e.getMessage());
			c.renderJson(result);
		}
	}

}
