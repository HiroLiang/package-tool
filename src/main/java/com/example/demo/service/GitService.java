package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.GitProjectBranchs;
import com.example.demo.model.entity.GitProject;
import com.example.demo.model.entity.UserData;
import com.example.demo.util.GitUtil;

@Service
public class GitService {

	@Value("${git.store.path}")
	private String git_store_path;
	@Value("${war.store.path}")
	private String war_store_path;
	@Value("${file.separator}")
	private static String sep;
	
	public boolean cloneProject(GitProject project, UserData user, String projectPath) {
		projectPath = projectPath + sep + project.getName();
		
		String url = getGitUrl(user.getGitAccount(), user.getAccessToken(), project.getUrl());
		String path = git_store_path + projectPath;
		boolean result = GitUtil.gitClone(url, path);
		if(!result)
			return result;
		
		if(project.getChildren().size() > 0) {
			for (GitProject child : project.getChildren()) {
				result = cloneProject(child, user, projectPath);
				if(!result)
					return result;
			}
		}
		System.out.println("Clone project : " + project.getName() + "success." );
		return result;
	}
	
	public GitProjectBranchs getBranchs(UserData user, GitProject project) {
		GitProjectBranchs result = new GitProjectBranchs();
		
		result.setName(project.getName());
		result.setBranchs(getBranchs(user.getGitAccount(), project.getName()));
		for (GitProject child : project.getChildren()) {
			result.getChildern().add(getBranchs(user, child));
		}
		
		return result;
	}

	public boolean compileProject(UserData user, GitProject project, Map<String,String> map) {
		String basePath = git_store_path + sep + user.getGitAccount();
		checkoutProject(user, project, map, basePath);
		
		boolean result = GitUtil.compileProject(basePath + sep + project.getName());
		
		GitUtil.delFolder(war_store_path);
		GitUtil.createFolder(war_store_path);
		if(result)
			GitUtil.collectAllWars(basePath + sep + project.getName(), war_store_path);
		return result;
	}
	
	// ------------------------------------------------------------------------------------------------------

	private String getGitUrl(String account, String token, String path) {
		return "https://" + account + ":" + token + "@" + path;
	}
	
	private List<String> getBranchs(String gitAccount, String projectName) {
		String location = git_store_path + sep + gitAccount + sep + projectName;
		return GitUtil.getBranchs(location);
	}
	
	private void checkoutProject(UserData user, GitProject project, Map<String,String> map, String basePath) {
		String path = basePath + sep + project.getName();
		String remote = getGitUrl(user.getGitAccount(), user.getAccessToken(), project.getUrl());
		GitUtil.gitCheckout(map.get(project.getName()), path, remote);
		
		for (GitProject child : project.getChildren()) {
			checkoutProject(user, child, map, path);
		}
	}

}
