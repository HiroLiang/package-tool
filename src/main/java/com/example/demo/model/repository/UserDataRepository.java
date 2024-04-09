package com.example.demo.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.UserData;

public interface UserDataRepository extends JpaRepository<UserData, Integer> {

	Optional<UserData> findByGitAccount(String gitAccount);
}
