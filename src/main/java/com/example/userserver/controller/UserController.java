package com.example.userserver.controller;


import com.example.userserver.dto.ResponseDTO;
import com.example.userserver.dto.UserDTO;
import com.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


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
        ResponseDTO response = new ResponseDTO();
        response = accService.findAccount(userName);
        return response;
    }

    @GetMapping(value = "/check-exist")
    public ResponseDTO checkExist(){
        return accService.successResponse();
    }



    public ResponseEntity<String> getData(String functionUrl){
        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String result = restTemplate.getForObject(url + functionUrl ,String.class);
        return ResponseEntity.ok(result);
//        return response;
    }
}
