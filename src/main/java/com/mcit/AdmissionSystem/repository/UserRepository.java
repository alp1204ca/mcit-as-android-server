package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    @EntityGraph(attributePaths = "roles")
    User findOneWithRolesByUserName(String userName);

    @EntityGraph(attributePaths = "roles")
    User findOneWithRolesById(Long id);

}
