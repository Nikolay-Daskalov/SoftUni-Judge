package com.trading212.judge.service.user;

import com.trading212.judge.repository.user.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Integer getStandard() {
        return roleRepository.getStandard();
    }
}
