package com.ecommerce.project.services;

import com.ecommerce.project.configurations.AppRole;
import com.ecommerce.project.model.Role;

public interface RoleServices {

    Role findByRoleName(AppRole roleUser);
}
