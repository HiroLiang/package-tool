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

import com.example.demo.model.entity.GitRepository;
import com.example.demo.model.entity.UserData;
import com.example.demo.service.GitService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/git")
public class GitController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private GitService gitService;
	
	@GetMapping("/{gitAccount}")
	public List<GitRepository> getProjects(@PathVariable String gitAccount) {
		UserData user = userService.getUser(gitAccount);
		return gitService.getProjects(user);
	}

	@PostMapping("/{project}")
	public Integer newProject(@PathVariable String project, @RequestBody UserData user) {
		boolean result = false;
		switch (project) {
		case "dap-api":
			result = gitService.newDapProject(user);
			break;
		}
		if(result)
			return 0;
		return 1;
	}
	
	@GetMapping("/branch/{project}/user/{gitAccount}")
	public List<String> getBranchs(@PathVariable String project, @PathVariable String gitAccount) {
		List<String> result = new ArrayList<String>();
		UserData user = userService.getUser(gitAccount);
		switch (project) {
		case "dap-api":
			result = gitService.getBranchs(project, user);
			break;
		case "dap-api-admin":
			result = gitService.getBranchs("dap-api/" + project, user);
			break;
		}
		return result;
	}
	
	@GetMapping("/compile/{project}/branch/{branch}")
	public void compileProject(@PathVariable String project, @PathVariable String branch) {
		switch (project) {
		case "dap-api":
			String[] split = branch.split(",split,");
			System.out.println(split[0]);
			System.out.println(split[1]);
			break;
		}
	}

}
