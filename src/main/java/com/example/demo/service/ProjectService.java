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
	
	public GitProject addNewProject(GitProject project) {
		if(project.getUrl().startsWith("https://"))
			project.setUrl(project.getUrl().replace("https://", ""));
		return gitProjectRepository.save(project);
	}
	
	public GitProject editProject(Integer id, GitProject project) {
		Optional<GitProject> optional = gitProjectRepository.findById(id);
		if(optional.isPresent()) {
			GitProject gitProject = optional.get();
			gitProject.setName(project.getName());
			gitProject.setJdk(project.getJdk());
			gitProject.setUrl(project.getUrl().replace("https://", ""));
			if(project.getChildren() != null)
				setParent(project, project.getChildren());
			gitProject.setChildren(project.getChildren());
			return gitProjectRepository.save(gitProject);
		}
		return null;
	}
	
	// ------------------------------------------------------------------------------------------------------
	
	private void setParent(GitProject parent, List<GitProject> children) {
		for (GitProject child : children) {
			child.setParent(parent);
			if(child.getChildren() != null)
				setParent(child, child.getChildren());
		}
	}
	
}
