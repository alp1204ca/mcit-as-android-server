package com.mcit.AdmissionSystem.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="as_student")
public class Student implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="SEQ_AS_STUDENT", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;

    @Column(name="first_name")
    @NotNull
    private String firstName;

    @Column(name="last_name")
    @NotNull
    private String lastName;

    @OneToOne( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}