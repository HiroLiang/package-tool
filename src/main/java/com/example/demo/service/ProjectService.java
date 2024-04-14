package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.GitProject;
import com.example.demo.model.entity.UserData;
import com.example.demo.model.entity.UserProject;
import com.example.demo.model.entity.UserProjectId;
import com.example.demo.model.repository.GitProjectRepository;
import com.example.demo.model.repository.UserProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {
	
	@Autowired
	private UserProjectRepository userProjectRepository;
	@Autowired
	private GitProjectRepository gitProjectRepository;
	
	public GitProject getProject(String name) {
		GitProject project = null;
		Optional<GitProject> optional = gitProjectRepository.findByName(name);
		if(optional.isPresent())
			project =  optional.get();
		return project;
	}
	
	public List<GitProject> getAllProjects() {
		return gitProjectRepository.findAllProjects();
	}

	public List<GitProject> getUserProjects(UserData user) {
		List<UserProject> projects = userProjectRepository.findByOwner(user);
		return projects.stream().map(userProject -> userProject.getProject()).toList();
	}
	
	@Transactional
	public void addUserProject(UserData user, GitProject project) {
		UserProjectId id = new UserProjectId(user, project);
		UserProject userProject = new UserProject(id, user, project);
		userProjectRepository.save(userProject);
	}
	
	// ------------------------------------------------------------------------------------------------------
	
}
