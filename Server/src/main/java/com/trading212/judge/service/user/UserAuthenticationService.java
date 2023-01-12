package com.trading212.judge.service.user;

import com.trading212.judge.model.auth.UserAuthModel;
import com.trading212.judge.repository.user.UserAuthenticationRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthenticationService implements UserDetailsService {

    private final UserAuthenticationRepository userAuthenticationRepository;

    public UserAuthenticationService(UserAuthenticationRepository userAuthenticationRepository) {
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthModel userAuthModel = userAuthenticationRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        return mapUserEntityToUser(userAuthModel);
    }

    private User mapUserEntityToUser(UserAuthModel userAuthModel) {
        Set<SimpleGrantedAuthority> roles = userAuthModel.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new User(
                userAuthModel.getUsername(), userAuthModel.getPasswordHash(), roles
        );
    }
}
