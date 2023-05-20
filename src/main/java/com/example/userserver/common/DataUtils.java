package com.example.userserver.common;

import com.example.userserver.dto.LoginDTO;
import com.example.userserver.dto.SignupRequest;
import com.example.userserver.dto.UserDTO;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtils {

//    public static Const c;

    public static boolean matchByPattern(String value, String regex){
        if(nullOrEmpty(regex) || nullOrEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    public static boolean notNullOrEmpty(Collection objects) {
        return !nullOrEmpty(objects);
    }

    public static boolean nullOrEmpty(Collection objects) {
        return objects == null || objects.isEmpty();
    }
    public static boolean nullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean notNullOrEmpty(UserDTO objects) {
        if(!objects.getUserName().isEmpty()&&!objects.getPassWord().isEmpty()&&
            !objects.getPhoneNumber().isEmpty()&&!objects.getEmail().isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    public static boolean notNullOrEmpty(SignupRequest objects) {
        if(!objects.getUserName().isEmpty()&&!objects.getPassWord().isEmpty()&&
                !objects.getPhoneNumber().isEmpty()&&!objects.getEmail().isEmpty()){
            return true;
        }else {
            return false;
        }
    }


//    public static boolean validateEmail (String email){
//        Pattern pattern = Pattern.compile(c.REGEX_EMAIL);
//        if(pattern.matcher(email).matches()){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//    public static boolean validatePhone (String phone){
//        Pattern pattern = Pattern.compile(c.REGEX_PHONE_NUBBER);
//        if(pattern.matcher(phone).matches()){
//            return true;
//        }else {
//            return false;
//        }
//    }
//    public static boolean validateUserName (String userName, String type){
//        if(userName.length()>=6 && userName.length()<=15){
//            Pattern pattern = Pattern.compile(type);
//            if(pattern.matcher(userName).matches()){
//                return true;
//            }else {
//                return false;
//            }
//        }else{
//            return false;
//        }
//    }

    public static boolean validateData (String data, String type){
        Pattern pattern = Pattern.compile(type);
        if(pattern.matcher(data).matches()){
            return true;
        }else {
            return false;
        }
    }
}
