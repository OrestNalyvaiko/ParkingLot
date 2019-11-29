package com.nalyvaiko.repository;

import com.nalyvaiko.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
