package com.example.userserver.service;

import com.example.userserver.common.Const;
import com.example.userserver.common.DataUtils;
import com.example.userserver.common.MessageUtils;
import com.example.userserver.dto.ResponseDTO;
import com.example.userserver.dto.UserDTO;
import com.example.userserver.entity.User;
import com.example.userserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Base64;

@Service
public class UserService {
    private final UserRepository userRepository;
    public static Const c;
    @Autowired
    private SequenceGeneratorService service;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        acc.setId(service.countId());
        acc.setUserName(userDTO.getUserName());
        String encodedString = Base64.getEncoder().encodeToString(userDTO.getPassWord().getBytes());
        acc.setPassWord(encodedString);
        acc.setRole(1);
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
    public ResponseDTO deleteAccount(int id){
        Assert.notNull(id, MessageUtils.getMessage("error.notfound", id));
        User acc =userRepository.findById(id);
        acc.setStatus(0);
        acc.setUpDateTime(System.currentTimeMillis());
        userRepository.save(acc);
        ResponseDTO responseDTO = successResponse();
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

}
