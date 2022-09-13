package com.si.ecommerce.services;

import com.si.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<com.si.ecommerce.domain.User> optionalUser = userRepository.findByEmail(email);
        com.si.ecommerce.domain.User user = optionalUser.orElseThrow(()->new UsernameNotFoundException("User not found with email id: " + email));
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}