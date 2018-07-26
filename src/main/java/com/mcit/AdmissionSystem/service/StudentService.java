package com.mcit.AdmissionSystem.service;

import com.mcit.AdmissionSystem.model.Student;
import com.mcit.AdmissionSystem.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    StudentRepository studentRepository;

    public Student findById(Long id) {

        return studentRepository.findOne(id);
    }

    public Student findByUserName(String userName) {

        return studentRepository.findByUserName(userName);
    }



    public Student findOneWithUserAndRoles(Long id) {

        return studentRepository.findOneWithUserAndRoles(id);
    }

    public List<Student> findAll() {

        return studentRepository.findAll();
    }

    public void delete(Student student) {

        studentRepository.delete(student);
    }

    public Student update(Student student) {

        return studentRepository.save(student);
    }

    public Student add(Student student) {

        return studentRepository.save(student);
    }
}
