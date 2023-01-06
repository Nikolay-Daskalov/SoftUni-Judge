package com.trading212.judge.repository.user;

import com.trading212.judge.model.dto.UserDTO;

public interface UserRepository {

    boolean register(String username, String email, String password, Integer standardRoleId);

    boolean isExists(String username);
}
