package com.example.userserver.service;

import com.example.userserver.common.Const;
import com.example.userserver.common.DataUtils;
import com.example.userserver.common.MessageUtils;
import com.example.userserver.common.ULID;
import com.example.userserver.dto.*;
import com.example.userserver.entity.ERole;
import com.example.userserver.entity.RefreshToken;
import com.example.userserver.entity.Role;
import com.example.userserver.entity.User;
import com.example.userserver.exception.TokenRefreshException;
import com.example.userserver.jwt.JwtUtils;
import com.example.userserver.repository.RoleRepository;
import com.example.userserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public static Const c;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RefreshTokenService refreshTokenService;


    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    //    public ResponseDTO addAccount(UserDTO userDTO){
//        User acc = new User();
//        checkData(userDTO);
//        User user1 = userRepository.findByUserName(userDTO.getUserName());
//        Assert.isNull(user1, MessageUtils.getMessage("userName.not.valid", userDTO.getUserName()));
//        acc.setUserName(userDTO.getUserName());
//        String encodedString = Base64.getEncoder().encodeToString(userDTO.getPassWord().getBytes());
//        acc.setPassWord(encodedString);
//        acc.setPhoneNumber(userDTO.getPhoneNumber());
//        user1 = userRepository.findByEmail(userDTO.getEmail());
//        Assert.isNull(user1, MessageUtils.getMessage("success.found", userDTO.getEmail()));
//        acc.setEmail(userDTO.getEmail());
//        acc.setStatus(1);
//        acc.setUpDateTime(System.currentTimeMillis());
//        userRepository.save(acc);
//        ResponseDTO responseDTO = successResponse();
//        responseDTO.setResponse(userRepository.findByUserName(acc.getUserName()));
//        return responseDTO;
//    }
    @Transactional
    public ResponseDTO updateAccount(UserDTO userDTO) {
        checkData(userDTO);
        User acc = userRepository.findByUserName(userDTO.getUserName());
        Assert.notNull(acc, MessageUtils.getMessage("error.notfound", userDTO.getUserName()));
        Assert.notNull(acc, MessageUtils.getMessage("error.notfound", userDTO.getEmail()));
//        String encodedString = Base64.getEncoder().encodeToString(userDTO.getPassWord().getBytes());
        acc.setPassWord(encoder.encode(userDTO.getPassWord()));
        acc.setPhoneNumber(userDTO.getPhoneNumber());
        acc.setEmail(userDTO.getEmail());
        acc.setUpDateTime(System.currentTimeMillis());
        userRepository.save(acc);
        ResponseDTO responseDTO = successResponse();
        responseDTO.setResponse(userRepository.findByUserName(acc.getUserName()));
        return responseDTO;
    }

    public ResponseDTO getAllAccount() {
        ResponseDTO responseDTO = successResponse();
        responseDTO.setResponse(userRepository.findAllByStatus(1));
        return responseDTO;
    }

    public ResponseDTO deleteAccount(String id) {
        Assert.notNull(id, MessageUtils.getMessage("error.notfound", id));
        User acc = userRepository.getById(id);
        acc.setStatus(0);
        acc.setUpDateTime(System.currentTimeMillis());
        userRepository.save(acc);
        ResponseDTO responseDTO = successResponse();
        return responseDTO;
    }

    public ResponseDTO findAccount(String userName) {
        User acc = userRepository.findByUserName(userName);
        Assert.notNull(acc, MessageUtils.getMessage("error.notfound", userName));
        ResponseDTO responseDTO = successResponse();
        responseDTO.setResponse(acc);
        return responseDTO;
    }


    public ResponseDTO successResponse() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(200);
        responseDTO.setMessage(MessageUtils.getMessage("success"));
        return responseDTO;
    }

    public ResponseDTO failResponse(String message) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(400);
        responseDTO.setMessage(message);
        return responseDTO;
    }

    public void checkData(UserDTO userDTO) {
        Assert.isTrue(DataUtils.notNullOrEmpty(userDTO), MessageUtils.getMessage("error.input.null", userDTO));
        Assert.isTrue(DataUtils.validateData(userDTO.getUserName(), c.REGEX_INPUT), MessageUtils.getMessage("userName.not.valid", userDTO.getUserName()));
        Assert.isTrue(DataUtils.validateData(userDTO.getEmail(), c.REGEX_EMAIL), MessageUtils.getMessage("email.not.valid", userDTO.getEmail()));
        Assert.isTrue(DataUtils.validateData(userDTO.getPhoneNumber(), c.REGEX_PHONE_NUMBER), MessageUtils.getMessage("phoneNumber.not.valid", userDTO.getPhoneNumber()));
        Assert.isTrue(DataUtils.validateData(userDTO.getPassWord(), c.REGEX_PASS), MessageUtils.getMessage("passWord.not.valid", userDTO.getPassWord()));
    }

    public void checkData(SignupRequest signUpRequest) {
        Assert.isTrue(DataUtils.notNullOrEmpty(signUpRequest), MessageUtils.getMessage("error.input.null", signUpRequest));
        Assert.isTrue(DataUtils.validateData(signUpRequest.getUserName(), c.REGEX_INPUT), MessageUtils.getMessage("userName.not.valid", signUpRequest.getUserName()));
        Assert.isTrue(DataUtils.validateData(signUpRequest.getEmail(), c.REGEX_EMAIL), MessageUtils.getMessage("email.not.valid", signUpRequest.getEmail()));
        Assert.isTrue(DataUtils.validateData(signUpRequest.getPhoneNumber(), c.REGEX_PHONE_NUMBER), MessageUtils.getMessage("phoneNumber.not.valid", signUpRequest.getPhoneNumber()));
        Assert.isTrue(DataUtils.validateData(signUpRequest.getPassWord(), c.REGEX_PASS), MessageUtils.getMessage("passWord.not.valid", signUpRequest.getPassWord()));
    }

    @Transactional
    public ResponseEntity<?> authenticateUser(LoginDTO loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassWord()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), refreshToken.getToken(), roles));
    }

    public ResponseDTO registerUser(SignupRequest signUpRequest) {
        checkData(signUpRequest);
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            ResponseDTO responseDTO = failResponse("Error: Username is already taken!");
            return responseDTO;
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            ResponseDTO responseDTO = failResponse("Error: Email is already in use!");
            return responseDTO;
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User();
        user.setId(new ULID().nextULID());
        user.setUserName(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setPassWord(encoder.encode(signUpRequest.getPassWord()));
        user.setStatus(1);
        user.setUpDateTime(System.currentTimeMillis());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return successResponse();
    }

    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User user  = userRepository.getById(userId);
                    String token = jwtUtils.generateTokenFromUsername(user.getUserName());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    public ResponseEntity<?> logoutUser(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
