package com.mcit.AdmissionSystem.service;

import com.mcit.AdmissionSystem.model.Role;
import com.mcit.AdmissionSystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role findByCode(String code) {

        return roleRepository.findByCode(code);
    }
}
