package com.trading212.judge.repository.user;

import java.util.Optional;

public interface UserRepository {

    boolean register(String username, String email, String password, Integer standardRoleId);

    boolean isExists(String username);

    Optional<Integer> getIdByUsername(String username);
}
