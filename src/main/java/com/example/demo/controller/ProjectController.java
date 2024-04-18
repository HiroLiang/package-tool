package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.GitProjectBranchs;
import com.example.demo.model.entity.GitProject;
import com.example.demo.model.entity.UserData;
import com.example.demo.service.GitService;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserService userService;
	@Autowired
	private GitService gitService;
	
	@GetMapping("/all")
	public List<GitProject> getAllProjects() {
		return projectService.getAllProjects();
	}
	
	@GetMapping("/{gitAccount}")
	public List<GitProject> getUserProjects(@PathVariable String gitAccount){
		List<GitProject> rs = new ArrayList<>();
		UserData user = userService.getUser(gitAccount);
		if(user != null)
			rs = projectService.getUserProjects(user);
		return rs;
	}
	
	@GetMapping("/branch/{projectName}/{gitAccount}")
	public GitProjectBranchs getBranchs(@PathVariable String projectName, @PathVariable String gitAccount){
		GitProject project = projectService.getProject(projectName);
		UserData user = userService.getUser(gitAccount);
		
		return gitService.getBranches(user, project, "");
	}
	
	@PostMapping("/clone/{projectName}/{gitAccount}")
	public Integer cloneProject(@PathVariable String projectName, @PathVariable String gitAccount) {
		Integer rs = 1;
		GitProject project = projectService.getProject(projectName);
		UserData user = userService.getUser(gitAccount);
		
		boolean result = gitService.cloneProject(project, user, "");
		if(result) {
			projectService.addUserProject(user, project);
			rs = 0;
		}
		return rs;
	}
	
	@PostMapping("/add")
	public List<GitProject> addNewProject(@RequestBody GitProject project) {
		GitProject newProject = projectService.addNewProject(project);
		if(newProject != null)
			System.out.println("save project success");
		
		return projectService.getAllProjects();
	}
	
	@PutMapping("/edit/{id}")
	public List<GitProject> editProject(@PathVariable Integer id, @RequestBody GitProject project) {
		System.out.println("edit project : " + project);
		GitProject editProject = projectService.editProject(id, project);
		if(editProject != null)
			System.out.println("edit project success");
		
		return projectService.getAllProjects();
	}

}
