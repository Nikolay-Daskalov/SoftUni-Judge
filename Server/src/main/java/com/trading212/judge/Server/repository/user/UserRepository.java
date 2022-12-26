package com.trading212.judge.Server.repository.user;

import com.trading212.judge.Server.model.auth.UserAuthModel;

import java.util.Optional;

public interface UserRepository {

    Optional<UserAuthModel> findByUsernameForAuthentication(String username);
}
