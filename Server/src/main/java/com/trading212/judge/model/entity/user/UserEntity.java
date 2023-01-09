package com.trading212.judge.model.entity.user;

import com.trading212.judge.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class UserEntity extends BaseEntity {

    public static final String TABLE_NAME = "users";

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return username.equals(that.username) && passwordHash.equals(that.passwordHash) && email.equals(that.email) && roles.equals(that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, passwordHash, email, roles);
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

        public Builder setCreatedAt(Instant createdAt) {
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
