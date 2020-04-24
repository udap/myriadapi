package io.chainmind.myriadapi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.chainmind.myriadapi.domain.RequestOrg;
import io.chainmind.myriadapi.domain.entity.AppRegistration;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AppRegistrationService;

@Component
public class WebInterceptor implements HandlerInterceptor {
	private static final Logger LOG = LoggerFactory.getLogger(WebInterceptor.class);
	@Autowired
	private RequestOrg requestOrg;
	@Autowired
	private AppRegistrationService appRegistrationService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		LOG.debug("[preHandle]" + "[" + request.getMethod() + "]" + request.getRequestURI());
		if (request.getRequestURI().equals("/favicon.ico")){
			return false;
		}
		//registration
		String appId = request.getHeader("x-app-id");
		LOG.debug("appId: " + appId);
//		if (!StringUtils.hasText(appId)){
//			throw new ApiException(HttpStatus.UNAUTHORIZED,"header 中没有 appId");
//		}
		requestOrg.setAppId(appId);
		AppRegistration registration = appRegistrationService.findWithOrgByAppId(appId);
		if (registration == null){
			throw new ApiException(HttpStatus.UNAUTHORIZED,"此appId="+appId+" 没有注册");
		}
		// set the organization
		requestOrg.setAppOrg(registration.getOrg());

		LOG.debug("current org id: " + requestOrg.getAppOrg().getId());

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		LOG.debug("[postHandle][" + request + "]");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		/*if (ex != null) {
			ex.printStackTrace();
		}*/
		LOG.debug("[afterCompletion][exception: " + ex + "]");
	}

	/*private String getParameters(HttpServletRequest request) {
		StringBuffer posted = new StringBuffer();
		Enumeration<String> e = request.getParameterNames();
		if (e != null) {
			posted.append("?");
		}
		while (e.hasMoreElements()) {
			if (posted.length() > 1) {
				posted.append("&");
			}
			String curr = e.nextElement();
			posted.append(curr + "=");
			if (curr.contains("password")) {
				posted.append("*****");
			} else {
				posted.append(request.getParameter(curr));
			}
		}
		String ip = request.getHeader("X-FORWARDED-FOR");
		String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
		if (ipAddr != null && !ipAddr.equals("")) {
			posted.append("&_psip=" + ipAddr);
		}
		return posted.toString();
	}

	private String getRemoteAddr(HttpServletRequest request) {
		String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
		if (ipFromHeader != null && ipFromHeader.length() > 0) {
			LOG.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
			return ipFromHeader;
		}
		return request.getRemoteAddr();
	}*/

}
