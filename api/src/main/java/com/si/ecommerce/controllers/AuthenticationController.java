package com.si.ecommerce.controllers;

import com.si.ecommerce.models.UserLoginRequest;
import com.si.ecommerce.services.JwtUserDetailsService;
import com.si.ecommerce.utils.JwtTokenUtil;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthenticationController {
    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth")
    public String getToken(@Valid @RequestBody UserLoginRequest loginRequest) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        return jwtTokenUtil.generateToken(userDetails);
    }
}
