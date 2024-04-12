package com.example.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.controller.WebSocketServer;

public class GitUtil {

	public static boolean createFolder(String path) {
		String cmd = "/bin/sh -c mkdir -p " + path;

		boolean result = false;
		try {
			Process process = Runtime.getRuntime().exec(cmd);

			int status = process.waitFor();
			if (status == 0)
				result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void gitCheckout(String branchs, String path, String remote) {
		String script = "git remote set-url origin " + remote;
		System.out.println("process : " + script);
		processShell(script, "", path);
		
		script = "git checkout -b " + branchs + " origin/" + branchs;
		System.out.println("process : " + script);
		processShell(script, "", path);
		
		script = "git checkout " + branchs;
		System.out.println("process : " + script);
		processShell(script, "", path);
		
		script = "git merge origin/" + branchs;
		System.out.println("process : " + script);
		processShell(script, "", path);
	}

	public static boolean gitClone(String url, String path) {
		String script = "git clone " + url + " " + path;
		System.out.println("process : " + script);
		List<String> result = processShell(script, "");
		if(result == null)
			return false;
		return true;
	}
	
	public static boolean compileProject(String path) {
		String script = "mvn clean install -am -DskipTests";
		System.out.println("process : " + script);
		List<String> result = processShell(script, "", path, "show");
		if(result == null)
			return false;
		return true;
	}
	
	public static void collectWar(String gitPath, String warPath, String[] modules) {
		for (String module : modules) {
			String script = "mv " + gitPath + "/" + module + "/target/*.war " + 
					warPath + "/" + module + ".war";
			System.out.println("process : " + script);
			List<String> result = processShell(script, "");
			System.out.println(result);
		}
	}

	public static List<String> processShell(String script, String args, String... workspace) {
		List<String> result = new ArrayList<String>();
		String[] cmd = new String[] { "/bin/sh", "-c", "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk && " + script };

		File dir = null;
		if (workspace.length > 0) {
			if (workspace[0] != null) {
				dir = new File(workspace[0]);
				System.out.println(workspace[0]);
			}
		}

		try {
			Process process = Runtime.getRuntime().exec(cmd, null, dir);

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result.add(line);
				if(workspace.length > 1) {					
					if(workspace[1] == "show") {
						if(!line.startsWith("Progress"))
							WebSocketServer.session.getBasicRemote().sendText("{show}" + line);						
					}
				}
				if(!line.startsWith("Progress"))
					System.out.println(line);
			}

			int status = process.waitFor();
			if(status != 0)
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
