package com.mcit.AdmissionSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name="AS_COURSE_STUDENT")
public class CS {

    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="SEQ_AS_CS", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Student student;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Column(name = "mark")
    private Double mark;

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
