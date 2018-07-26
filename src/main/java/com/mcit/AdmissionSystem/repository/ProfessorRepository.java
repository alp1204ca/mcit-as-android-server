package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

      @Query("select professor from Professor professor join fetch professor.user user join fetch user.roles roles where professor.id = ?1")
      Professor findOneWithUserAndRoles(long id);

}
