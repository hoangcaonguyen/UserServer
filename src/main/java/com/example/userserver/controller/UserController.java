package com.example.userserver.controller;

import com.example.userserver.common.MessageUtils;
import com.example.userserver.dto.LoginDTO;
import com.example.userserver.dto.ResponseDTO;
import com.example.userserver.dto.UserDTO;
import com.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
@RequestMapping("/account")
public class UserController {
    @Autowired
    private final UserService accService;
    @Autowired
    private RestTemplate restTemplate;

    public static final String url = "https://itemserver-production.up.railway.app";

    public UserController(UserService accService) {
        this.accService = accService;
    }


//    @PostMapping("/add")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseDTO addAccount(@RequestBody UserDTO accDTO){
//        ResponseDTO response = new ResponseDTO();
//        response = accService.addAccount(accDTO);
//        return response;
//    }

    @PostMapping("/update")
    public ResponseDTO updateAccount(@RequestBody UserDTO userDTO){
        ResponseDTO response = new ResponseDTO();
        response = accService.updateAccount(userDTO);
        return response;
    }
    @GetMapping("/list")
    public ResponseDTO getAllAccount(){
        ResponseDTO response = new ResponseDTO();
        response = accService.getAllAccount();
        return response;
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseDTO deleteUser(@PathVariable(name = "id") String userId) {
        ResponseDTO response = new ResponseDTO();
        response = accService.deleteAccount(userId);
        return response;
    }

    @PostMapping(value = "/getAllItem")
    public ResponseEntity<String> getAllItem(@RequestPart("id") String id){
        String functionUrl = "/item/findByOwner/" + id;
        return getData(functionUrl);
    }

    @PostMapping(value = "/getAllFolder")
    public ResponseEntity<String> getAllFolder(@RequestPart("id") String id){
        String functionUrl = "/folder/findByOwner/" + id;
        return getData(functionUrl);
    }

    @PostMapping(value = "/findUser")
    public ResponseDTO findUser(@RequestPart("userName") String userName){
//        Assert.notNull(request, MessageUtils.getMessage("passWord.not.valid", request);
        ResponseDTO response = new ResponseDTO();
        response = accService.findAccount(userName);
        return response;
    }

    @GetMapping(value = "/check-exist")
    public ResponseDTO checkExist(){
        return accService.successResponse();
    }

//    @PostMapping(value = "/login")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseDTO login(@RequestBody LoginDTO login){
//        ResponseDTO response = new ResponseDTO();
//        response = accService.login(login);
//        return response;
//    }

    public ResponseEntity<String> getData(String functionUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(url + functionUrl , HttpMethod.GET, entity, String.class);
        return response;
    }
}
