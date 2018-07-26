package com.mcit.AdmissionSystem.controller;

import com.mcit.AdmissionSystem.model.*;
import com.mcit.AdmissionSystem.service.CSService;
import com.mcit.AdmissionSystem.service.CourseService;
import com.mcit.AdmissionSystem.service.StudentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CSAssignmentController {

    private static final Logger log = LoggerFactory.getLogger(CSAssignmentController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CSService csService;


    @GetMapping("/cs")
    @ResponseBody
    public ResponseEntity<List<CS>> student() {
        log.info("/cs called");

        try {

            List<CS> csList = csService.findAll();
            return new ResponseEntity<>(csList, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving course / students",e);
            return new ResponseEntity("Error retrieving course / students", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cs/all-students-not-in-course/{id}")
    @ResponseBody
    public ResponseEntity<List<Student>> studentNotInCourse(@PathVariable("id") long id) {
        log.info("/all-students-not-in-course called");

        try {

            List<Student> students = studentService.findAll();
            List<CS> csList = csService.findByCourseId(id);
            csList.removeIf(cs -> cs.getStudent() == null);

            csList.forEach(cs -> {
                students.removeIf(s -> s.getId() == cs.getStudent().getId());
            });

            return new ResponseEntity<>(students, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving students not in course " + id,e);
            return new ResponseEntity("Error retrieving students not in course " + id, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cs/new")
    @ResponseBody
    public ResponseEntity newStudent(@RequestBody CS cs) {

        try {

            csService.add(cs.getCourse(), cs.getStudent());
            return new ResponseEntity(HttpStatus.OK);

        } catch (Exception e) {
            log.error("Could not add course / student", e);
            return new ResponseEntity("Error adding course / student", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cs/{id1}/{id2}")
    @ResponseBody
    public ResponseEntity removeStundetFromCourse(@PathVariable long id1, @PathVariable long id2) {

        CS cs = csService.findOneByCourseIdAndStudentId(id1, id2);

        if (cs != null) {

            try {
                csService.delete(cs);
                return new ResponseEntity("Student successfully removed from course", HttpStatus.OK);
            } catch (Exception e) {
                log.error("Could not remove student from course", e);
                return new ResponseEntity("Error removing student from course", HttpStatus.OK);
            }
        } else
            return new ResponseEntity("Course / Student does not exist", HttpStatus.OK);
    }

}
