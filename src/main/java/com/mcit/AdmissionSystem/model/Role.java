package com.mcit.AdmissionSystem.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="as_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="SEQ_AS_ROLE", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;

    @Column(name="code")
    String code;

    @Column(name="description")
    String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
