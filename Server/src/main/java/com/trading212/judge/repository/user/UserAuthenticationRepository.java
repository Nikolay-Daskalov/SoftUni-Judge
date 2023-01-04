package com.trading212.judge.repository.user;

import com.trading212.judge.model.auth.UserAuthModel;

import java.util.Optional;

public interface UserAuthenticationRepository {

    Optional<UserAuthModel> findByUsername(String username);
}
