package com.si.ecommerce.services;

import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.exceptions.UnauthorizeRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthenticationService {

    private final UserService userService;

    public void authenticateUser(int userId) throws UnauthorizeRequestException, NotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userDetails.getUsername().equals(userService.getUserEmail(userId))) {
            throw new UnauthorizeRequestException("Unauthorized");
        }
    }
}
