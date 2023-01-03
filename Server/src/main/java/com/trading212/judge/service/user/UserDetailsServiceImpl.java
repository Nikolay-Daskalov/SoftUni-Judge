package com.trading212.judge.service.user;

import com.trading212.judge.model.auth.UserAuthModel;
import com.trading212.judge.repository.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
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
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new User(
                userAuthModel.getUsername(), userAuthModel.getPasswordHash(), roles
        );
    }
}
