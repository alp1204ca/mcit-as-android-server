package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String name);

    @Query("select course from Course course where course.professor.user.userName = :userName")
    List<Course> findAllByProfessorUserName(@Param("userName") String userName);
}
