package com.tusofia.ndurmush.business.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tusofia.ndurmush.base.entity.BaseEntity;
import com.tusofia.ndurmush.base.entity.LongIdentifiable;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@UserDefinition
@Table(name = "t_user")
@NamedQuery(name = User.NAMED_QUERY_FIND_BY_IDS, query = "SELECT u FROM User u WHERE u.id IN :ids")
@NamedQuery(name = User.NAMED_QUERY_FIND_BY_LOGIN, query = "SELECT t FROM User t WHERE LOWER(t.login) = LOWER(:login)")
@NamedQuery(name = User.NAMED_QUERY_FIND_BY_EMAIL, query = "SELECT t FROM User t WHERE LOWER(t.email) = LOWER(:email)")
public class User extends BaseEntity implements LongIdentifiable {

    public static final String NAMED_QUERY_FIND_BY_LOGIN = "User.findByLogin";
    public static final String NAMED_QUERY_FIND_BY_EMAIL = "User.findByEmail";
    public static final String NAMED_QUERY_FIND_BY_IDS = "User.findByIds";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_BUYER = "BUYER";
    public static final String ROLE_SUPPLIER = "SUPPLIER";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUserId")
    @SequenceGenerator(name = "seqUserId", sequenceName = "seq_user_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Username
    @Column(name = "login")
    private String login;

    @Password
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Roles
    @JsonIgnore
    public String getRoleString() {
        return role.name();
    }

    @Override
    @JsonBackReference(value = "creator")
    public User getCreator() {
        return super.getCreator();
    }

    @Override
    @JsonBackReference(value = "editor")
    public User getEditor() {
        return super.getEditor();
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public String getReversedDisplayName() {
        return lastName + ", " + firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

}
