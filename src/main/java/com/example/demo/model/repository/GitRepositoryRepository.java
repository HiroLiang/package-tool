package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.GitRepository;
import com.example.demo.model.entity.UserData;

public interface GitRepositoryRepository extends JpaRepository<GitRepository, Integer> {
	
	List<GitRepository> findByOwner(UserData owner);

}
