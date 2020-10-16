package com.stackroute.springboot.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTFilter extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// api/v1/users -- GET
		//api/v1/users --> POST
		//Http REquest header -> {'Authorization','Bearer ${token}'}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String authHeader = httpRequest.getHeader("authorization");
		System.out.println("AuthHeader" +  authHeader);
		if(authHeader == null) {
			throw new ServletException("Missing Authentication Header");
		}
		 if(!authHeader.startsWith("Bearer")) {
			 throw new ServletException("Authorization header must starts with Bearer");
		}
			
		
		
		String jwtToken = authHeader.substring(7);
		try {
		Claims claims = Jwts.parser().setSigningKey("i@ieosow").parseClaimsJws(jwtToken).getBody();
		httpRequest.setAttribute("username", claims);
		chain.doFilter(request, response);
		}catch(Exception e) {
			throw new ServletException("Invalid Authentication Header");
		}
		
	}

}
