package com.mcit.AdmissionSystem.controller;

import com.mcit.AdmissionSystem.model.*;
import com.mcit.AdmissionSystem.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CSService csService;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${as.mcit.url}")
    private String url;

    @GetMapping("/student")
    @ResponseBody
    public ResponseEntity<List<Student>> student() {
        log.info("/student called");

        try {

            List<Student> studentList = studentService.findAll();
            return new ResponseEntity<>(studentList, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving students",e);
            return new ResponseEntity("Error retrieving students", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/student/new")
    @ResponseBody
    public ResponseEntity newStudent(@RequestBody Student student) {

        try {

            User user = userService.findByUserName(student.getUser().getUserName());
            if (user != null) {
                log.error("Could not add student " + student.getFirstName() + " "
                        + student.getLastName() + ". Username already in use: "
                        + student.getUser().getUserName());
                return new ResponseEntity("Error adding student - Username already in use", HttpStatus.BAD_REQUEST);
            } else {
                String password = RandomStringUtils.random(10, true, true);
                String passwordEncrypted = passwordEncoder.encode(password);
                Role adminRole = roleService.findByCode("ROLE_STUDENT");
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                user = new User(student.getUser().getUserName(), passwordEncrypted, student.getUser().getEmail(), roles);
                student.setUser(user);
                studentService.add(student);

                Map<String, Object> params = new HashMap<>();
                params.put("name", student.getFirstName() + " " + student.getLastName());
                params.put("username", student.getUser().getUserName());
                params.put("password", password);
                params.put("url", url);
                params.put("type", "student");

                mailService.send(mailFrom, user.getEmail(), "Welcome to MCIT student", "new_account.html", params);

                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Could not add student " + student.getFirstName() + " " + student.getLastName(), e);
            return new ResponseEntity("Error adding student", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/student/edit")
    @ResponseBody
    public ResponseEntity editStudent(@RequestBody Student student) {

        Student student_ = studentService.findById(student.getId());

        if (student_ != null) {
            try {
                student_.setFirstName(student.getFirstName());
                student_.setLastName(student.getLastName());
                student_.getUser().setEmail(student.getUser().getEmail());
                student_.getUser().setUserName(student.getUser().getUserName());
                studentService.update(student_);
                return new ResponseEntity(HttpStatus.OK);
            } catch (Exception e) {
                log.error("Could not update student " +  student.getFirstName() + " " + student.getLastName(), e);
                return new ResponseEntity("Error updating student", HttpStatus.BAD_REQUEST);
            }
        } else
            return new ResponseEntity("Student does not exist", HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/student/{id}")
    @ResponseBody
    public ResponseEntity deleteStudent(@PathVariable long id) {

        Student student_ = studentService.findOneWithUserAndRoles(id);

        if (student_ != null) {

            try {
                List<CS> csList = csService.findByStudentId(id);
                if (csList.size() > 0)
                    return new ResponseEntity("Error - Student in course", HttpStatus.OK);
                studentService.delete(student_);
                return new ResponseEntity("Student successfully deleted", HttpStatus.OK);
            } catch (Exception e) {
                log.error("Could not delete student id " + id, e);
                return new ResponseEntity("Error deleting student", HttpStatus.OK);
            }

        } else
            return new ResponseEntity("Student does not exist", HttpStatus.OK);
    }

    @PostMapping("/student/reset")
    @ResponseBody
    public ResponseEntity resetPassword(@ModelAttribute Student student) {

        Student student_ = null;

        try {
            student_ = studentService.findById(student.getId());

            if (student_ == null) {
                log.error("Could not reset student's password " + student.getFirstName() + " "
                        + student.getLastName() + ". Student not found: "
                        + student.getUser().getUserName());
                return new ResponseEntity("Error resetting student's password - Invalid Student id", HttpStatus.BAD_REQUEST);
            } else {
                String password = RandomStringUtils.random(10, true, true);
                String passwordEncrypted = passwordEncoder.encode(password);
                student_.getUser().setPassword(passwordEncrypted);
                userService.update(student_.getUser());

                Map<String, Object> params = new HashMap<>();
                params.put("name", student_.getFirstName() + " " + student_.getLastName());
                params.put("username", student_.getUser().getUserName());
                params.put("password", password);
                params.put("url", url);
                params.put("type", "student");

                mailService.send(mailFrom, student_.getUser().getEmail(), "MCIT student's password reset", "password_reset.html", params);

                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Could not reset student's password " + student.getId(), e);
            return new ResponseEntity("Error resetting student's password", HttpStatus.BAD_REQUEST);
        }
    }

}
