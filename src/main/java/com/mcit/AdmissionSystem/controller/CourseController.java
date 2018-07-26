package com.mcit.AdmissionSystem.controller;

import com.mcit.AdmissionSystem.model.CS;
import com.mcit.AdmissionSystem.model.Course;
import com.mcit.AdmissionSystem.model.Professor;
import com.mcit.AdmissionSystem.model.Student;
import com.mcit.AdmissionSystem.service.CSService;
import com.mcit.AdmissionSystem.service.CourseService;
import com.mcit.AdmissionSystem.service.ProfessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private CSService csService;

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/course")
    @ResponseBody
    public ResponseEntity<List<Course>> course( ) {
        try {

            List<Course> courseList = courseService.findAll();
            return new ResponseEntity<>(courseList, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving courses",e);
            return new ResponseEntity("Error retrieving courses", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/course/new")
    @ResponseBody
    public ResponseEntity newCourse(@RequestBody Course course) {

        if (course != null && course.getName() != null) {

            Course course_ = courseService.findByName(course.getName());

            if (course_ == null) {
                try {
                    courseService.add(course);
                    csService.add(course, null);
                    return new ResponseEntity(HttpStatus.CREATED);
                } catch (Exception e) {
                    log.error("Could not add course " + course.getName(), e);
                    return new ResponseEntity("Error adding course", HttpStatus.BAD_REQUEST);
                }
            } else
                return new ResponseEntity("Course already exists", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity("Invalid course name", HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/course/edit")
    @ResponseBody
    public ResponseEntity editCourse(@RequestBody Course course) {

        if (course != null && course.getName() != null) {

            Course course_ = courseService.findById(course.getId());

            if (course_ != null) {
                try {
                    course_ = courseService.findByName(course.getName());
                    if (course_ == null || course.getId() == course_.getId()) {
                        Professor professor = professorService.findById(course.getProfessor().getId());
                        course.setProfessor(professor);
                        courseService.update(course);
                        return new ResponseEntity("Course successfully deleted", HttpStatus.OK);
                    } else {
                        return new ResponseEntity("Course already exists", HttpStatus.OK);
                    }

                } catch (Exception e) {
                    log.error("Could not update course " + course.getName(), e);
                    return new ResponseEntity("Error updating course", HttpStatus.OK);
                }
            } else
                return new ResponseEntity("Course does not exist", HttpStatus.OK);
        } else
            return new ResponseEntity("Invalid course name", HttpStatus.OK);
    }

    @DeleteMapping("/course/{id}")
    @ResponseBody
    public ResponseEntity deleteCourse(@PathVariable long id) {

            Course course_ = courseService.findById(id);

            if (course_ != null) {
                try {
                    List<CS> csList = csService.findByCourseId(id);
                    if (csList.size() > 0)
                        return new ResponseEntity("Error - course assigned to students",HttpStatus.OK);
                    courseService.deleteById(id);
                    return new ResponseEntity("Course successfully deleted",HttpStatus.OK);
                } catch (Exception e) {
                    log.error("Could not delete course " + course_.getName(), e);
                    return new ResponseEntity("Error adding course", HttpStatus.BAD_REQUEST);
                }
            } else
                return new ResponseEntity("Course does not exist", HttpStatus.BAD_REQUEST);

    }

}
