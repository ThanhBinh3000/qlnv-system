package com.tcdt.qlnvsystem.jwt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcdt.qlnvsystem.table.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvsystem.RequestService;
import com.tcdt.qlnvsystem.service.UserService;
import com.tcdt.qlnvsystem.table.UserHistory;
import com.tcdt.qlnvsystem.util.MapAction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private RequestService requestService;

	@Autowired
	UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.debug("Filtering request for JWT header verification");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if ((request instanceof HttpServletRequest) && !(request instanceof ContentCachingRequestWrapper)) {
			request = new ContentCachingRequestWrapper((HttpServletRequest) request);
		}
		Authentication authentication = null;
		try {
			String jwt = getJwtFromRequest(httpServletRequest);
			String requestURI = httpServletRequest.getRequestURI();
			if (StringUtils.hasText(jwt) && TokenAuthenticationService.validateToken(jwt)) {
				String username = TokenAuthenticationService.getUsernameFromToken(jwt);
				// Get UserDetails by username
				UserDetails userDetails = userService.loadUserByUsername(username);
				authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) request,
						userDetails);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("set Authentication to security context for '{}', uri: {}", authentication.getName(),
						requestURI);
			} else {
				log.debug("no valid JWT token found, uri: {}", requestURI);
			}
			filterChain.doFilter(request, response);

		} catch (Exception ex) {
			log.error("Could not set user authentication in security context", ex);
			final Map<String, Object> body = new HashMap<>();
			body.put("statusCode", HttpServletResponse.SC_FORBIDDEN);
			body.put("msg", ex.getMessage());

			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), body);
		} finally {
			if (request instanceof HttpServletRequest && authentication != null) {
				performRequestAudit((HttpServletRequest) request, authentication);
			}
		}
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		log.debug("Attempting to get token from request header");
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public void performRequestAudit(HttpServletRequest req, Authentication authentication) {
		ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(req, ContentCachingRequestWrapper.class);
		String payload = "";
		if (wrapper != null) {
			byte[] requestBuffer = wrapper.getContentAsByteArray();
			if (requestBuffer.length > 0) {
				int length = Math.min(requestBuffer.length, 10000);
				try {
					payload = new String(requestBuffer, 0, length, wrapper.getCharacterEncoding());
				} catch (UnsupportedEncodingException unex) {
					payload = "[Unsupported-Encoding]";
				}
			}
		}
		/**
		 * luu lai lich su duyet web, tu url va tham so lay ra noi dung lich su phu hop
		 */
		try {
			UserHistory userHistory = new UserHistory();
			String path = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) + "-"
					+ req.getMethod().toLowerCase();
			if (getMapAction().containsKey(path) && userService != null) {
				String clientIp = requestService.getClientIp(req);
				String ct = getMapAction().get(path);
				userHistory.setNote(ct);
				userHistory.setAction(path);
				userHistory.setContent(payload.getBytes());
				userHistory.setIpaddress(clientIp);
				userHistory.setUsername(authentication.getName());
				userHistory.setOrganization(authentication.getDetails().toString());
				userHistory.setTimeaction(new Date());
				userService.saveUserHistory(userHistory);
			}
		} catch (Exception e) {
			log.error("saveHistory:log:" + e.getMessage());
		}
	}

	public Map<String, String> getMapAction() {
		if (MapAction.map == null && userService != null) {
			MapAction.map = new HashMap<>();
			Iterable<UserAction> list = userService.findAll();
			for (UserAction userAction : list) {
				MapAction.map.put(userAction.getAction(), userAction.getDescription());
			}
		}
		return MapAction.map;
	}

}
