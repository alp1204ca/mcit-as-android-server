package com.mcit.AdmissionSystem.service;

import com.mcit.AdmissionSystem.model.User;
import com.mcit.AdmissionSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findByUserName(String userName) {

        return userRepository.findOneWithRolesByUserName(userName);
    }

    public User findById(Long id) {

        return userRepository.findOneWithRolesById(id);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
