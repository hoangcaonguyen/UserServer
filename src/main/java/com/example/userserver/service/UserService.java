package com.example.userserver.service;

import com.example.userserver.common.Const;
import com.example.userserver.common.DataUtils;
import com.example.userserver.common.MessageUtils;
import com.example.userserver.dto.*;
import com.example.userserver.entity.ERole;
import com.example.userserver.entity.Role;
import com.example.userserver.entity.User;
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
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Base64;
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


    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    public ResponseDTO addAccount(UserDTO userDTO){
//        ResponseDTO responseDTO = new ResponseDTO();
        User acc = new User();
//        Assert.isTrue(DataUtils.notNullOrEmpty(userDTO), MessageUtils.getMessage("error.input.null", userDTO));
//        Assert.isTrue(DataUtils.validateEmail(userDTO.getEmail()),MessageUtils.getMessage("email.not.valid", userDTO.getEmail()));
//        Assert.isTrue(DataUtils.validatePhone(userDTO.getPhoneNumber()),MessageUtils.getMessage("phoneNumber.not.valid", userDTO.getPhoneNumber()));
        checkData(userDTO);
//        acc.setId(service.getSequenceNumber(SEQUENCE_NAME));
        User user1 = userRepository.findByUserName(userDTO.getUserName());
        Assert.isNull(user1, MessageUtils.getMessage("userName.not.valid", userDTO.getUserName()));
//        acc.setId(service.countId());
        acc.setUserName(userDTO.getUserName());
        String encodedString = Base64.getEncoder().encodeToString(userDTO.getPassWord().getBytes());
        acc.setPassWord(encodedString);
//        acc.setRoles(1);
        acc.setPhoneNumber(userDTO.getPhoneNumber());
        user1 = userRepository.findByEmail(userDTO.getEmail());
        Assert.isNull(user1, MessageUtils.getMessage("success.found", userDTO.getEmail()));
        acc.setEmail(userDTO.getEmail());
        acc.setStatus(1);
        acc.setUpDateTime(System.currentTimeMillis());
        userRepository.save(acc);
        ResponseDTO responseDTO = successResponse();
        responseDTO.setResponse(userRepository.findByUserName(acc.getUserName()));
//        responseDTO.setCode(200);
//        responseDTO.setMessage(MessageUtils.getMessage("success.upload"));
        return responseDTO;
    }
    @Transactional
    public ResponseDTO updateAccount(UserDTO userDTO){
        checkData(userDTO);
//        Assert.isTrue(DataUtils.notNullOrEmpty(userDTO), MessageUtils.getMessage("error.input.null", userDTO));;
//        ResponseDTO responseDTO = failResponse();
        User acc = userRepository.findByUserName(userDTO.getUserName());
        Assert.notNull(acc, MessageUtils.getMessage("error.notfound", userDTO.getUserName()));
        Assert.notNull(acc, MessageUtils.getMessage("error.notfound", userDTO.getEmail()));
        String encodedString = Base64.getEncoder().encodeToString(userDTO.getPassWord().getBytes());
        acc.setPassWord(encodedString);
        acc.setPhoneNumber(userDTO.getPhoneNumber());
        acc.setEmail(userDTO.getEmail());
        acc.setUpDateTime(System.currentTimeMillis());
        userRepository.save(acc);
        ResponseDTO responseDTO = successResponse();
        responseDTO.setResponse(userRepository.findByUserName(acc.getUserName()));
        return responseDTO;
    }

    public ResponseDTO getAllAccount(){
        ResponseDTO responseDTO = successResponse();
        responseDTO.setResponse(userRepository.findAllByStatus(1));
        return responseDTO;
    }
//    public ResponseDTO deleteAccount(int id){
//        Assert.notNull(id, MessageUtils.getMessage("error.notfound", id));
//        User acc =userRepository.findById(id);
//        acc.setStatus(0);
//        acc.setUpDateTime(System.currentTimeMillis());
//        userRepository.save(acc);
//        ResponseDTO responseDTO = successResponse();
//        return responseDTO;
//    }

    public ResponseDTO findAccount(String userName){
        User acc = userRepository.findByUserName(userName);
        Assert.notNull(acc, MessageUtils.getMessage("error.notfound", userName));
        ResponseDTO responseDTO = successResponse();
        responseDTO.setResponse(acc);
        return responseDTO;
    }

    public ResponseDTO login(LoginDTO login){
        User acc = userRepository.findByUserName(login.getUserName());
        Assert.notNull(acc, MessageUtils.getMessage("error.notfound", login.getUserName()));
        String encodedString = Base64.getEncoder().encodeToString(login.getPassWord().getBytes());
        ResponseDTO responseDTO = new ResponseDTO();
        if(encodedString.equals(acc.getPassWord())){
            responseDTO.setCode(200);
            responseDTO.setMessage(MessageUtils.getMessage("success"));
        }else {
            responseDTO.setCode(400);
            responseDTO.setMessage(MessageUtils.getMessage("wrong password"));
        }
        return responseDTO;
    }
//    public ResponseDTO findUser

    public ResponseDTO successResponse(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(200);
        responseDTO.setMessage(MessageUtils.getMessage("success.upload"));
        return responseDTO;
    }

    public ResponseDTO failResponse (){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(400);
        responseDTO.setMessage(MessageUtils.getMessage("error.upload"));
        return responseDTO;
    }

    public void checkData(UserDTO userDTO){
        Assert.isTrue(DataUtils.notNullOrEmpty(userDTO), MessageUtils.getMessage("error.input.null", userDTO));
        Assert.isTrue(DataUtils.validateData(userDTO.getUserName(),c.REGEX_INPUT),MessageUtils.getMessage("userName.not.valid", userDTO.getUserName()));
        Assert.isTrue(DataUtils.validateData(userDTO.getEmail(),c.REGEX_EMAIL),MessageUtils.getMessage("email.not.valid", userDTO.getEmail()));
        Assert.isTrue(DataUtils.validateData(userDTO.getPhoneNumber(),c.REGEX_PHONE_NUMBER),MessageUtils.getMessage("phoneNumber.not.valid", userDTO.getPhoneNumber()));
        Assert.isTrue(DataUtils.validateData(userDTO.getPassWord(),c.REGEX_PASS),MessageUtils.getMessage("passWord.not.valid", userDTO.getPassWord()));
    }

    public ResponseEntity<?> authenticateUser( LoginDTO loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassWord()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, roles));
    }

    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
//        User user = new User(signUpRequest.getUsername(),
//                signUpRequest.getEmail(),
//                encoder.encode(signUpRequest.getPassword()),
//                signUpRequest.getPhoneNumber()
//        );
        User user = new User();
        user.setUserName(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setPassWord(encoder.encode(signUpRequest.getPassWord()));
        user.setStatus(1);


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

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
