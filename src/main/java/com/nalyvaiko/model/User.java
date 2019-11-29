package com.nalyvaiko.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "first_name", nullable = false)
  private String name;
  @Column(name = "last_name", nullable = false)
  private String surname;
  @Column(name = "email", nullable = false, unique = true)
  private String email;
  @Column(name = "username", nullable = false, unique = true)
  private String username;
  @Column(name = "password", nullable = false)
  private String password;
  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  public User(Long id, String name, String surname, String email,
      String username, String password, Role role) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public User() {
  }

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

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(getId(), user.getId()) &&
        Objects.equals(getName(), user.getName()) &&
        Objects.equals(getSurname(), user.getSurname()) &&
        Objects.equals(getEmail(), user.getEmail()) &&
        Objects.equals(getUsername(), user.getUsername()) &&
        Objects.equals(getPassword(), user.getPassword()) &&
        Objects.equals(getRole(), user.getRole());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getId(), getName(), getSurname(), getEmail(), getUsername(),
            getPassword(), getRole());
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", surname='" + surname + '\'' +
        ", email='" + email + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", role=" + role +
        '}';
  }
}
