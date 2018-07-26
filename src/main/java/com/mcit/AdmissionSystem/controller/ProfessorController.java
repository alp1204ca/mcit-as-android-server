package com.mcit.AdmissionSystem.controller;

import com.mcit.AdmissionSystem.model.*;
import com.mcit.AdmissionSystem.service.MailService;
import com.mcit.AdmissionSystem.service.RoleService;
import com.mcit.AdmissionSystem.service.ProfessorService;
import com.mcit.AdmissionSystem.service.UserService;
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
public class ProfessorController {

    private static final Logger log = LoggerFactory.getLogger(ProfessorController.class);

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${as.mcit.url}")
    private String url;

    @GetMapping("/professor")
    @ResponseBody
    public ResponseEntity<List<Professor>> professor() {
        log.info("/professor called");

        try {

            List<Professor> professorList = professorService.findAll();
            return new ResponseEntity<>(professorList, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving professors",e);
            return new ResponseEntity("Error retrieving professors", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/professor/new")
    @ResponseBody
    public ResponseEntity newProfessor(@RequestBody Professor professor) {

        try {

            User user = userService.findByUserName(professor.getUser().getUserName());
            if (user != null) {
                log.error("Could not add professor " + professor.getFirstName() + " "
                        + professor.getLastName() + ". Username already in use: "
                        + professor.getUser().getUserName());
                return new ResponseEntity("Error adding professor - Username already in use", HttpStatus.BAD_REQUEST);
            } else {
                String password = RandomStringUtils.random(10, true, true);
                String passwordEncrypted = passwordEncoder.encode(password);
                Role adminRole = roleService.findByCode("ROLE_STUDENT");
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                user = new User(professor.getUser().getUserName(), passwordEncrypted, professor.getUser().getEmail(), roles);
                professor.setUser(user);
                professorService.add(professor);

                Map<String, Object> params = new HashMap<>();
                params.put("name", professor.getFirstName() + " " + professor.getLastName());
                params.put("username", professor.getUser().getUserName());
                params.put("password", password);
                params.put("url", url);
                params.put("type", "professor");

                mailService.send(mailFrom, user.getEmail(), "Welcome to MCIT professor", "new_account.html", params);

                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Could not add professor " + professor.getFirstName() + " " + professor.getLastName(), e);
            return new ResponseEntity("Error adding professor", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/professor/edit")
    @ResponseBody
    public ResponseEntity editProfessor(@RequestBody Professor professor) {

        Professor professor_ = professorService.findById(professor.getId());

        if (professor_ != null) {
            try {
                professor_.setFirstName(professor.getFirstName());
                professor_.setLastName(professor.getLastName());
                professor_.getUser().setEmail(professor.getUser().getEmail());
                professor_.getUser().setUserName(professor.getUser().getUserName());
                professorService.update(professor_);
                return new ResponseEntity(HttpStatus.OK);
            } catch (Exception e) {
                log.error("Could not update professor " +  professor.getFirstName() + " " + professor.getLastName(), e);
                return new ResponseEntity("Error updating professor", HttpStatus.BAD_REQUEST);
            }
        } else
            return new ResponseEntity("Professor does not exist", HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/professor/{id}")
    @ResponseBody
    public ResponseEntity deleteProfessor(@PathVariable long id) {

        Professor professor_ = professorService.findOneWithUserAndRoles(id);

        if (professor_ != null) {

            //TODO: redo
            //if (professor_.getProfessorCourses() != null && professor_.getProfessorCourses().size() > 0) {
            //    log.error("Could not delete professor id " +  id + ", professor is registered on course(s)");
            //    return new ResponseEntity("Professor cannot be deleted because this professor is registered on at least one course", HttpStatus.OK);
            //} else {
            try {
                professorService.delete(professor_);
                return new ResponseEntity("Professor successfully deleted", HttpStatus.OK);
            } catch (Exception e) {
                log.error("Could not delete professor id " + id, e);
                return new ResponseEntity("Error adding professor", HttpStatus.OK);
            }
            //}
        } else
            return new ResponseEntity("Professor does not exist", HttpStatus.OK);
    }

    @PostMapping("/professor/reset")
    @ResponseBody
    public ResponseEntity resetPassword(@ModelAttribute Professor professor) {

        Professor professor_ = null;

        try {
            professor_ = professorService.findById(professor.getId());

            if (professor_ == null) {
                log.error("Could not reset professor's password " + professor.getFirstName() + " "
                        + professor.getLastName() + ". Professor not found: "
                        + professor.getUser().getUserName());
                return new ResponseEntity("Error resetting professor's password - Invalid Professor id", HttpStatus.BAD_REQUEST);
            } else {
                String password = RandomStringUtils.random(10, true, true);
                String passwordEncrypted = passwordEncoder.encode(password);
                professor_.getUser().setPassword(passwordEncrypted);
                userService.update(professor_.getUser());

                Map<String, Object> params = new HashMap<>();
                params.put("name", professor_.getFirstName() + " " + professor_.getLastName());
                params.put("username", professor_.getUser().getUserName());
                params.put("password", password);
                params.put("url", url);
                params.put("type", "professor");

                mailService.send(mailFrom, professor_.getUser().getEmail(), "MCIT professor's password reset", "password_reset.html", params);

                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Could not reset professor's password " + professor.getId(), e);
            return new ResponseEntity("Error resetting professor's password", HttpStatus.BAD_REQUEST);
        }
    }

}
