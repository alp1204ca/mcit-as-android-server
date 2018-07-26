package com.mcit.AdmissionSystem.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.DELETE_ORPHAN;

@Entity
@Table(name = "as_user" )
public final class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="SEQ_AS_USER", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "as_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_code", referencedColumnName = "code")})
    @NotNull
    private Set<Role> roles;

    @Column(name = "user_name",unique = true)
    @NotNull
    private String userName;

    @Column(name="email")
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    public User() {}

    public User(String userName, String password, String email, Set<Role> roles) {
        this.roles = roles;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }

    public boolean hasRole(String role) {
    	for (Role role_ : roles) {
			if (role_.getCode().equals(role)) {
				return true;
			}
		}
    	return false;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
