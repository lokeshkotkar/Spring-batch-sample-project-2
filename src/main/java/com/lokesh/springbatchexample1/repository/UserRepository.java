package com.lokesh.springbatchexample1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lokesh.springbatchexample1.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
