package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.UserData;
import com.example.demo.model.entity.UserProject;
import com.example.demo.model.entity.UserProjectId;

public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectId> {
	
	List<UserProject> findByOwner(UserData owner);

}
