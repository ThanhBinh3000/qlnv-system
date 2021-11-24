package com.tcdt.qlnvsystem.jwt;

import java.util.Date;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenAuthenticationService {
	static final long EXPIRATIONTIME = 36_000_000; // 10 phut 36_000_000

	static final String SECRET = "Teca15DuyTan";

	static final String TOKEN_PREFIX = "Bearer";

	static final String HEADER_STRING = "Authorization";

	public static void addAuthentication(HttpServletResponse res, String username, Long issuer) {

		String JWT = Jwts.builder().setSubject(username).setIssuer(Long.toString(issuer))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		res.addHeader(HEADER_STRING, JWT);
	}

	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		System.out.println("token: " + token);
		if (token != null) {
			// parse the token.
			try {
				Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
						.getBody();
				String user = claims.getSubject();
				if (user == null)
					return null;

				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();

				UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user,
						null, userDetails.getAuthorities());
				newAuthentication.setDetails(claims.getIssuer());
				return newAuthentication;
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	public static Authentication getAuthentication(HttpServletRequest request, UserDetails userDetails) {
		String token = request.getHeader(HEADER_STRING);
		if (userDetails == null || StringUtils.isEmpty(token))
			return null;
		// parse the token.
		try {
			// Nếu người dùng hợp lệ, set thông tin cho Seturity Context
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//	            authentication.setDetails(claims.getIssuer());
			return authentication;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature.");
			log.trace("Invalid JWT signature trace: {}", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace: {}", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace: {}", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace: {}", e);
		}
		return false;
	}

	// retrieve username from jwt token
	public static String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private static Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

}
