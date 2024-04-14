package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.model.entity.GitProject;
import com.example.demo.model.repository.GitProjectRepository;

@SpringBootApplication
public class PackageToolApplication implements CommandLineRunner{
	
	@Autowired
	private GitProjectRepository gitProjectRepository;

	public static void main(String[] args) {
		SpringApplication.run(PackageToolApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(gitProjectRepository.count() == 0) {
			GitProject dap_api = new GitProject();
			dap_api.setName("dap-api");
			dap_api.setUrl("git.systex.com/misystex/050_tbbbank/b05002204_tbb_asp/backend/dap-api.git");
			dap_api.setJdk("11");
			dap_api.setParent(null);
			dap_api = gitProjectRepository.save(dap_api);
			
			GitProject dap_api_admin = new GitProject();
			dap_api_admin.setName("dap-api-admin");
			dap_api_admin.setUrl("git.systex.com/misystex/050_tbbbank/b05002204_tbb_asp/backend/dap-adi-admin.git");
			dap_api_admin.setJdk("11");
			dap_api_admin.setParent(dap_api);
			dap_api_admin = gitProjectRepository.save(dap_api_admin);
		}
	}

}
