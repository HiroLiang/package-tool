package com.example.demo.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.entity.GitProject;

public interface GitProjectRepository extends JpaRepository<GitProject, Integer> {
	
	Optional<GitProject> findByName(String name);
	
	@Query(value = "SELECT * FROM git_project g WHERE g.PARENT_ID IS NULL", nativeQuery = true)
	List<GitProject> findAllProjects();

}
