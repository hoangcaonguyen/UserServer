package com.example.userserver.controller;


import com.example.userserver.dto.*;
import com.example.userserver.jwt.JwtUtils;
import com.example.userserver.repository.RoleRepository;
import com.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;


	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private final UserService accService;

	public AuthController(UserService accService) {
		this.accService = accService;
	}

	@PostMapping("/signIn")
	@PermitAll
	public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginRequest) {
//		accService.authenticateUser(loginRequest);
		return accService.authenticateUser(loginRequest);
	}

	@PostMapping("/signup")
	public ResponseDTO registerUser(@RequestBody SignupRequest signUpRequest) {
		return accService.registerUser(signUpRequest);
	}

	@PostMapping("/refreshToken")
	public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
		return accService.refreshToken(request);
	}

	@PostMapping("/signOut")
	public ResponseEntity<?> logoutUser(@RequestPart("refreshToken") String refreshToken) {
		return accService.logoutUser(refreshToken);
	}
}
