package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.GitRepository;
import com.example.demo.model.entity.UserData;
import com.example.demo.model.repository.GitRepositoryRepository;
import com.example.demo.util.GitUtil;

@Service
public class GitService {
	
	@Value("${git.url.dap}")
	private String dap_api_path;
	@Value("${git.url.dap-admin}")
	private String dap_admin_api_path;
	@Value("${git.store.path}")
	private String git_store_path;
	
	@Autowired
	private GitRepositoryRepository gitRepositoryRepository;
	
	public List<GitRepository> getProjects(UserData owner){
		return gitRepositoryRepository.findByOwner(owner);
	}

	public boolean newDapProject(UserData user) {
		System.out.println(dap_api_path + ", " + dap_admin_api_path + ", " + git_store_path);
		
		String dapUrl = getGitUrl(user.getGitAccount(), user.getAccessToken(), dap_api_path);
		String dapAdminUrl = getGitUrl(user.getGitAccount(), user.getAccessToken(), dap_admin_api_path);
		
		String gitPath = git_store_path + "\\" + user.getGitAccount();
		
		boolean isCloned = GitUtil.gitClone(dapUrl, gitPath + "\\dap-api");
		if(isCloned)
			GitUtil.gitClone(dapAdminUrl, gitPath + "\\dap-api\\dap-api-admin");
		
		if(isCloned) {
			GitRepository newProject = new GitRepository();
			newProject.setProjectName("dap-api");
			newProject.setOwner(user);
			gitRepositoryRepository.save(newProject);
			return true;
		}
		return false;
	}
	
	private String getGitUrl(String account, String token, String path) {
		return "https://" + account + ":" + token + "@" + path;
	}

}
