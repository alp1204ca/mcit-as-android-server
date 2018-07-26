package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Role findByCode(String code);
}
