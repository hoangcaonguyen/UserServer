package com.example.userserver.controller;


import com.example.userserver.dto.*;
import com.example.userserver.jwt.JwtUtils;
import com.example.userserver.repository.RoleRepository;
import com.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

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

	@PostMapping("/signin")
	@PermitAll
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {
//		accService.authenticateUser(loginRequest);
		return accService.authenticateUser(loginRequest);
	}

	@PostMapping("/signup")
	public ResponseDTO registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		return accService.registerUser(signUpRequest);
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		return accService.refreshToken(request);
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser(@RequestPart("id") String id) {
		return accService.logoutUser(id);
	}
}
