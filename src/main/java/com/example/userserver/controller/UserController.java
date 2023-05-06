package com.example.userserver.controller;

import com.example.userserver.dto.ResponseDTO;
import com.example.userserver.dto.UserDTO;
import com.example.userserver.entity.Item;
import com.example.userserver.entity.User;
import com.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/account")
public class UserController {
    @Autowired
    private final UserService accService;
    @Autowired
    private RestTemplate restTemplate;

    public static final String url = "https://itemserver-production.up.railway.app/";

    public UserController(UserService accService) {
        this.accService = accService;
    }


    @PostMapping("/add")
    public ResponseDTO addAccount(@RequestBody UserDTO accDTO){
        ResponseDTO response = new ResponseDTO();
        response = accService.addAccount(accDTO);
        return response;
    }

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
    public ResponseDTO deleteUser(@PathVariable(name = "id") Integer userId) {
        ResponseDTO response = new ResponseDTO();
        response = accService.deleteAccount(userId);
        return response;
    }

    @PostMapping(value = "/getAllItem")
    public ResponseEntity<String> getAllItem(@RequestPart("id") int id){
        String functionUrl = "/item/findByOwner/" + id;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
//        ResponseEntity<String> response = restTemplate.exchange(url +"/item/findByOwner/" + id, HttpMethod.GET, entity, String.class);

        return getData(functionUrl);
    }

    @PostMapping(value = "/getAllFolder")
    public ResponseEntity<String> getAllFolder(@RequestPart("id") int id){
        String functionUrl = "/folder/findByOwner/" + id;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
//        ResponseEntity<String> response = restTemplate.exchange(url +"/item/findByOwner/" + id, HttpMethod.GET, entity, String.class);

        return getData(functionUrl);
    }

    public ResponseEntity<String> getData(String functionUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(url + functionUrl , HttpMethod.GET, entity, String.class);
        return response;
    }
}
