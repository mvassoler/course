package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_USERS")
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @Column(name = "ID")
    private UUID userId;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "FULL_NAME", nullable = false, length = 255)
    private String fullName;

    @Column(name = "USER_STATUS", nullable = false)
    private String userStatus;

    @Column(name = "USER_TYPE", nullable = false)
    private String userType;

    @Column(name = "CPF", length = 20)
    private String cpf;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<CourseModel> courses;

}
