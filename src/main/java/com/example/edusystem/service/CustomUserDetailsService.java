package com.example.edusystem.service;

import com.example.edusystem.dto.SignInRequest;
import com.example.edusystem.response.UserSignInResponse;
import com.example.edusystem.entity.User;
import com.example.edusystem.repository.UserRepository;
import com.example.edusystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserSignInResponse signIn(SignInRequest signIn) {
        User user = (User) loadUserByUsername(signIn.getPhoneNumber());

        if (passwordEncoder.matches(signIn.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            return new UserSignInResponse(token, jwtService.getJwtExpiration(), HttpStatus.OK.value());
        } else {
            throw new BadCredentialsException("Invalid phone number or password.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByPhoneNumber(username);
        if (user == null){
            throw new UsernameNotFoundException("Phone Number not found");
        }
        return user;
    }
}
