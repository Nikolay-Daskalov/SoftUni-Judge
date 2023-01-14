package com.trading212.judge.service.user;

import com.trading212.judge.model.dto.user.UserRegistrationDTO;
import com.trading212.judge.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }


    public boolean isExist(String username, String email) {
        return userRepository.isExist(username, email);
    }

    public boolean register(UserRegistrationDTO userRegistrationDTO) {
        Integer standardRoleId = roleService.getStandard();

        return userRepository.register(userRegistrationDTO.username(), userRegistrationDTO.email(), userRegistrationDTO.password(), standardRoleId);
    }

    public Optional<Integer> getIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }
}
