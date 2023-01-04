package com.trading212.judge.repository.user;

import com.trading212.judge.model.dto.UserDTO;

public interface UserRepository {

    UserDTO register(String username, String email, String password);

    boolean isExists(String username);
}
