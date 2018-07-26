package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CSRepository extends JpaRepository <CS,Long> {

    @Query("select cs from CS cs where cs.course.id = :id")
    List<CS> findByCourseId(@Param("id") long id);

    @Query("select cs from CS cs where cs.student.id = :id")
    List<CS> findByStudentId(@Param("id") long id);

    @Query("select cs from CS cs where cs.course.id = :courseId and cs.student.id = :studentId")
    List<CS> findByCourseIdAndStudentId(@Param("courseId") long courseId, @Param("studentId") long studentId);
}
