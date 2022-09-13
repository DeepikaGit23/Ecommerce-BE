package com.si.ecommerce.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.si.ecommerce.models.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class ControllerTestUtil {
    public static String BEARER = "Bearer ";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String modelToJson(final Object obj) {
        try {
            return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T jsonToModel(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.findAndRegisterModules().readValue(json, clazz);
    }

    public String generateToken(com.si.ecommerce.domain.User user){
        return ControllerTestUtil.BEARER + jwtTokenUtil.generateToken(new User(user.getEmail()
                , user.getPassword(), new ArrayList<>()));
    }
}
