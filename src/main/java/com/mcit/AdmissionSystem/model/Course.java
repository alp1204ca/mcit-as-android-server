package com.mcit.AdmissionSystem.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mcit.AdmissionSystem.web.json.CustomJsonDateDeserializer;
import com.mcit.AdmissionSystem.web.json.CustomJsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="as_course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="SEQ_AS_COURSE", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinTable(name = "as_course_professor",
            joinColumns = @JoinColumn(name="course_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="professor_id", referencedColumnName="id"))
    private Professor professor;

    @Column(name = "start_date")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date startDate;

    @Column(name = "end_date")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
