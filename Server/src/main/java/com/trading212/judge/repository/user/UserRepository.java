package com.trading212.judge.repository.user;

import com.trading212.judge.model.auth.UserAuthModel;

import java.util.Optional;

public interface UserRepository {

    Optional<UserAuthModel> findByUsernameForAuthentication(String username);
}
