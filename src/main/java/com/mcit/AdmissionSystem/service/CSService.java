package com.mcit.AdmissionSystem.service;

import com.mcit.AdmissionSystem.model.*;
import com.mcit.AdmissionSystem.repository.CSRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSService {

    private static final Logger log = LoggerFactory.getLogger(CSService.class);

    @Autowired
    CSRepository csRepository;

    public void add(Course course, Student student) {

        CS cs = new CS();
        cs.setCourse(course);
        cs.setStudent(student);

        csRepository.save(cs);
    }

    public void delete(CS cps) {

        csRepository.delete(cps);
    }

    public void deleteByCourseId(long id) {

        List<CS> csList = csRepository.findByCourseId(id);
        csRepository.delete(csList);
    }

    public void setMark(CS cps, double mark) {

        cps.setMark(mark);
        csRepository.save(cps);
    }

    public List<CS> findAll() {

        return csRepository.findAll();
    }

    public CS findById(Long id) {

        return csRepository.findOne(id);
    }

    public CS findOneByCourseIdAndStudentId(long courseId, long studentId) {

        return csRepository.findByCourseIdAndStudentId(courseId, studentId).get(0);
    }

    public List<CS> findByCourseId(long courseId) {

        return csRepository.findByCourseId(courseId);
    }

    public List<CS> findByStudentId(long studentId) {

        return csRepository.findByStudentId(studentId);
    }

}
