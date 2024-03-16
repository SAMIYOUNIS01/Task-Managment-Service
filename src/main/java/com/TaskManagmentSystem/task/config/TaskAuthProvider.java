package com.TaskManagmentSystem.task.config;

import com.TaskManagmentSystem.task.exceptions.UserNotFoundException;
import com.TaskManagmentSystem.task.model.entity.Authority;
import com.TaskManagmentSystem.task.model.entity.User;
import com.TaskManagmentSystem.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class TaskAuthProvider implements AuthenticationProvider {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String pwd = authentication.getCredentials().toString();
        String username = authentication.getName();
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()){
            if(passwordEncoder.matches(pwd, user.get().getPwd())){
                return new UsernamePasswordAuthenticationToken(username , pwd ,grantedAuthorities(user.get().getAuthoritySet()));
            }
            else {
                throw new BadCredentialsException("Invalid.password");
            }
        }else {
            try {
                throw new UserNotFoundException("user.not.found");
            } catch (UserNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<GrantedAuthority> grantedAuthorities (Set<Authority> authorities){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority :
                authorities  ) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
