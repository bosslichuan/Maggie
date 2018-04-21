package com.zcurd.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.zcurd.common.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 权限处理拦截器
 * @author 钟世云 2016.2.5
 */
public class AuthInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	@Override
	public void intercept(Invocation inv) {
		//TODO 注解标识不拦截
		//Annotation[] as = inv.getMethod().getAnnotations();
		//logger.info("********Annotation: " + as.length);
		
		Controller c = inv.getController();
		HttpServletRequest request = c.getRequest();
		
		int contextLength = request.getContextPath().length();
		String currUrl = request.getRequestURI().substring(contextLength);
		String aurhUrl = UrlUtil.formatBaseUrl(currUrl);
		logger.info("[请求url]:{}", currUrl);
		
		List<String> noAuthUrl = c.getSessionAttr("noAuthUrl");
		if(noAuthUrl != null) {
			//页面权限处理，拦截action/method链接的所有/action/*页面
			for (String url : noAuthUrl) {
				if(aurhUrl.equals(UrlUtil.formatBaseUrl(url))) {
					c.renderText("error-4413:没有权限访问该页面！");
					return;
				}
			}
			//按钮权限
			Map<String, Object> authBtn = c.getSessionAttr("noAuthBtnUrl");
			List<String> noAuthBtnUrl = (List<String>) authBtn.get("btnUrlList");
			Map<String, String> noAuthBtnMap = (Map<String, String>) authBtn.get("pageBtnMap");
			request.setAttribute("noAuthBtn", noAuthBtnMap.get(currUrl));
			for (String btnUrl : noAuthBtnUrl) {
				if(currUrl.equals(btnUrl)) {
					c.renderText("error-4415:没有权限访问该页面！");
					return;
				}
			}
		}
		inv.invoke();
	}
}
