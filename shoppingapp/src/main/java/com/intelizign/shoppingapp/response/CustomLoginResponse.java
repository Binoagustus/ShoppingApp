package com.intelizign.shoppingapp.response;

import java.util.List;

import org.springframework.http.ResponseCookie;

public class CustomLoginResponse {

	private Long userId;
	private String username;
	private String email;
	private String password;
	private ResponseCookie jwtCookie;
	private List<String> roles;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ResponseCookie getJwtCookie() {
		return jwtCookie;
	}

	public void setJwtCookie(ResponseCookie jwtCookie) {
		this.jwtCookie = jwtCookie;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public CustomLoginResponse(Long userId, String username, String email, String password, ResponseCookie jwtCookie, List<String> roles) {

		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.jwtCookie = jwtCookie;
		this.roles = roles;
	}
}
