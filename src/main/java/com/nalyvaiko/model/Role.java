package com.nalyvaiko.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "role_name")
  private String roleName;

  public Role() {
  }

  public Role(Long id, String roleName) {
    this.id = id;
    this.roleName = roleName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Role)) {
      return false;
    }
    Role role = (Role) o;
    return Objects.equals(getId(), role.getId()) &&
        Objects.equals(getRoleName(), role.getRoleName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getRoleName());
  }

  @Override
  public String toString() {
    return "Role{" +
        "id=" + id +
        ", roleName='" + roleName + '\'' +
        '}';
  }
}
