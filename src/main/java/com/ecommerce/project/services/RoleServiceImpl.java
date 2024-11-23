package com.ecommerce.project.services;

import com.ecommerce.project.configurations.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleServices {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Role findByRoleName(AppRole roleUser) {
        return roleRepository.findByRoleName(roleUser).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }
}
