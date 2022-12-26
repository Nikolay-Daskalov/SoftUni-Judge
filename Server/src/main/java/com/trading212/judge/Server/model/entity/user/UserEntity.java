package com.trading212.judge.Server.model.entity.user;

import com.trading212.judge.Server.model.entity.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Set;

public class UserEntity extends BaseEntity {

    private String username;
    private String passwordHash;
    private String email;
    private Set<RoleEntity> roles;

    public UserEntity() {
        super(null, null);
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public static class Builder {
        private final UserEntity user;

        public Builder() {
            user = new UserEntity();
        }

        public Builder setId(Integer id) {
            user.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            user.username = username;
            return this;
        }

        public Builder setPasswordHash(String passwordHash) {
            user.passwordHash = passwordHash;
            return this;
        }

        public Builder setEmail(String email) {
            user.email = email;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            user.createdAt = createdAt;
            return this;
        }

        public Builder setRoles(Set<RoleEntity> roles) {
            user.roles = roles;
            return this;
        }

        public UserEntity build() {
            return user;
        }
    }
}
