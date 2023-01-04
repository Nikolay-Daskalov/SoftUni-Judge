package com.trading212.judge.service.user;

import com.trading212.judge.model.dto.UserDTO;
import com.trading212.judge.model.dto.UserRegistrationDTO;
import com.trading212.judge.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean isExist(String username) {
        return userRepository.isExists(username);
    }

    public UserDTO register(UserRegistrationDTO userRegistrationDTO) {
        return userRepository.register(userRegistrationDTO.username(), userRegistrationDTO.email(), userRegistrationDTO.password());
    }
}
