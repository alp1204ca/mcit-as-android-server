package com.mcit.AdmissionSystem.service;

import com.mcit.AdmissionSystem.model.Course;
import com.mcit.AdmissionSystem.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    CourseRepository courseRepository;

    public Course findByName(String name) {

        return courseRepository.findByName(name);
    }

    public Course findById(Long id) {

        return courseRepository.findOne(id);
    }

    public List<Course> findAll() {

        return courseRepository.findAll();
    }

    public List<Course> findAllByProfessor(String userName) {

        return courseRepository.findAllByProfessorUserName(userName);
    }

    public void deleteById(Long id) {

        courseRepository.delete(id);
    }

    public Course update(Course course) {

        return courseRepository.saveAndFlush(course);
    }

    public Course add(Course course) {

        return courseRepository.saveAndFlush(course);
    }
}
