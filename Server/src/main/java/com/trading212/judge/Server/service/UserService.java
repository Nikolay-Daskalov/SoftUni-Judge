package com.trading212.judge.Server.service;

import com.trading212.judge.Server.model.auth.UserAuthModel;
import com.trading212.judge.Server.repository.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthModel userAuthModel = userRepository.findByUsernameForAuthentication(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        return mapUserEntityToUser(userAuthModel);
    }

    private User mapUserEntityToUser(UserAuthModel userAuthModel) {
        Set<SimpleGrantedAuthority> roles = userAuthModel.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .collect(Collectors.toSet());

        return new User(
                userAuthModel.getUsername(), userAuthModel.getPasswordHash(), roles
        );
    }
}
