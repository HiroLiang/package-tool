package com.example.demo.service;

import java.util.ArrayList;
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
	@Value("${war.store.path}")
	private String war_store_path;

	@Autowired
	private GitRepositoryRepository gitRepositoryRepository;

	public List<GitRepository> getProjects(UserData owner) {
		return gitRepositoryRepository.findByOwner(owner);
	}

	public boolean newDapProject(UserData user) {
		System.out.println(dap_api_path + ", " + dap_admin_api_path + ", " + git_store_path);

		String dapUrl = getGitUrl(user.getGitAccount(), user.getAccessToken(), dap_api_path);
		String dapAdminUrl = getGitUrl(user.getGitAccount(), user.getAccessToken(), dap_admin_api_path);

		String gitPath = git_store_path + "\\" + user.getGitAccount();

		boolean isCloned = GitUtil.gitClone(dapUrl, gitPath + "\\dap-api");
		if (isCloned) {
			System.out.println("clone dap-api success.");
			GitUtil.gitClone(dapAdminUrl, gitPath + "\\dap-api\\dap-api-admin");
		}

		if (isCloned) {
			System.out.println("clone dap-api-admin success.");
			GitRepository newProject = new GitRepository();
			newProject.setProjectName("dap-api");
			newProject.setOwner(user);
			gitRepositoryRepository.save(newProject);
			return true;
		}
		return false;
	}

	public boolean compileDapProject(String gitAccount, String accessToken, String dapBranch, String adminBranch) {
		boolean result = false;
		String gitPath = git_store_path + "\\" + gitAccount;

		String dapRemote = getGitUrl(gitAccount, accessToken, dap_api_path);
		GitUtil.gitCheckout(dapBranch, gitPath + "\\dap-api", dapRemote);

		String adminRemote = getGitUrl(gitAccount, accessToken, dap_admin_api_path);
		GitUtil.gitCheckout(dapBranch, gitPath + "\\dap-api\\dap-api-admin", adminRemote);

		result = GitUtil.compileProject(gitPath + "\\dap-api");
		
		String delScript = "Remove-Item -Path " + war_store_path + "\\* -Recurse -Force";
		GitUtil.processShell(delScript, "");
		
		GitUtil.createFolder(war_store_path);

		String[] modules = new String[] { "dap-api-admin", "dap-frontend", "dap-batch", "dap-loan", "dap-test" };
		if(result)
			GitUtil.collectWar(gitPath + "\\dap-api", war_store_path, modules);

		return result;
	}

	public List<String> getBranchs(String project, String gitAccount) {
		String gitPath = git_store_path + "\\" + gitAccount + "\\" + project;
		String script = "git branch -r";
		List<String> result = GitUtil.processShell(script, "", gitPath);
		if (result == null)
			return new ArrayList<String>();
		return result;
	}

	private String getGitUrl(String account, String token, String path) {
		return "https://" + account + ":" + token + "@" + path;
	}

}
