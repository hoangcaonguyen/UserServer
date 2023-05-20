package com.example.userserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String id;
	private String refreshToken;
	private List<String> roles;

	public JwtResponse(String accessToken,  String id, String refreshToken, List<String> roles) {
		this.token = accessToken;
		this.id =id;
		this.refreshToken = refreshToken;
		this.roles = roles;
	}
}
