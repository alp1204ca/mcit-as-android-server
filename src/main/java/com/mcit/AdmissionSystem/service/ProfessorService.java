package com.mcit.AdmissionSystem.service;

import com.mcit.AdmissionSystem.model.Professor;
import com.mcit.AdmissionSystem.model.User;
import com.mcit.AdmissionSystem.repository.ProfessorRepository;
import com.mcit.AdmissionSystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private static final Logger log = LoggerFactory.getLogger(ProfessorService.class);

    @Autowired
    ProfessorRepository professorRepository;

    public Professor findById(Long id) {

        return professorRepository.findOne(id);
    }

    public Professor findOneWithUserAndRoles(Long id) {

        return professorRepository.findOneWithUserAndRoles(id);
    }



    public List<Professor> findAll() {

        return professorRepository.findAll();
    }

    public void delete(Professor professor) {

        professorRepository.delete(professor);
    }

    public Professor update(Professor professor) {

        return professorRepository.save(professor);
    }

    public Professor add(Professor professor) {

        return professorRepository.save(professor);
    }
}
