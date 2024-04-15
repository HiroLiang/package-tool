package com.example.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.controller.WebSocketServer;
import com.example.demo.model.enumerate.CmdLineProcess;
import com.example.demo.model.enumerate.JavaVersion;

@Component
public class GitUtil {

	private static String sep;
	private static String os;
	
	private static String java8;
	private static String java11;
	
	@Value("${file.separator}")
    public void setSep(String sep) {
        GitUtil.sep = sep;
    }
    
    @Value("${server.system.os}")
    public void setOs(String os) {
        GitUtil.os = os;
    }
    
    @Value("${mvn.jdk8.path}")
    public void setJava8(String java8) {
    	GitUtil.java8 = java8;
    }
    
    @Value("${mvn.jdk11.path}")
    public void setJava11(String java11) {
    	GitUtil.java11 = java11;
    }

	public static boolean createFolder(String path) {
		String cmd = null;
		if ("windows".equals(os)) {
			cmd = "powershell.exe -c mkdir \"" + path + "\"";
		} else if ("linux".equals(os)) {
			cmd = "/bin/sh -c mkdir -p \"" + path + "\"";
		}

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
	
	public static List<String> getBranchs(String projectLocation) {
		String script = "git branch -r";
		System.out.println("process : " + script);
		List<String> branchs = processShell(script, "", projectLocation);
		if(branchs != null)
			return branchs;
		return new ArrayList<String>();
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
		System.out.println("process script : " + script);
		List<String> result = processShell(script, "");
		if (result == null)
			return false;
		return true;
	}

	public static boolean compileProject(String path, JavaVersion jdk) {
		String script = "mvn clean install -am -DskipTests";
		System.out.println("process : " + script);
		List<String> result = processShell(script, "", jdk, path, "send-client");
		if (result == null)
			return false;
		return true;
	}

	public static void delFolder(String path) {
		String script = null;
		if("windows".equals(os))
			script = "rm \"" + path + "\" -Recurse -Force";
		if("linux".equals(os))
			script = "rm -rf \"" + path + "\"";
		processShell(script, "");
	}
	
	public static void collectAllWars(String gitPath, String warPath) {
		String script = null;
		if("windows".equals(os))
			 script = "Get-ChildItem -Path \"" + gitPath + "\" -Recurse -Filter *.war | "
			 		+ "ForEach-Object { Move-Item -Path $_.FullName -Destination \"" + warPath + "\" }";
		if("linux".equals(os))
			script = "find \"" + gitPath + "\" -path \"*/target/*.war\" "
					+ "-exec mv {} \"" + warPath + "\" \\;" ;
		System.out.println("process : " + script);
		
		List<String> result = processShell(script, "");
		System.out.println(result);
	}

	/*
	 * oprions : [0] - work place , [1] - tag to specific process
	 */
	public static List<String> processShell(String script, String args, JavaVersion jdk, String... options) {

		String[] cmd = getCmd(script, jdk);
		File dir = getWorkspace(options);
		
		try {
			return doCmd(cmd, dir, options);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static List<String> processShell(String script, String args, String... options) {

		String[] cmd = getCmd(script);
		File dir = getWorkspace(options);
		
		try {
			return doCmd(cmd, dir, options);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

	// ------------------------------------------------------------------------------------------------------

	private static String[] getCmd(String script, JavaVersion jdk) {
		if ("windows".equals(os))
			return new String[] { "powershell.exe", "-c", 
					"$env:JAVA_HOME='" + jdkPath(jdk) + "' ;", 
					"$env:PATH=\"" + jdkPath(jdk) + "\\bin\" + $env:PATH ;"
					, script };
		return new String[] { "/bin/sh", "-c", "export JAVA_HOME=" + jdkPath(jdk) + " && ", script };
	}
	
	private static String[] getCmd(String script) {
		if ("windows".equals(os))
			return new String[] { "powershell.exe", "-c", script };
		return new String[] { "/bin/sh", "-c", script };
	}

	private static File getWorkspace(String[] options) {
		if (options.length > 0) {
			if (options[0] != null)
				return new File(options[0]);
		}
		return null;
	}

	private static List<String> doCmd(String[] cmd, File workDir, String[] options) throws IOException, InterruptedException {
		List<String> result = new ArrayList<String>();

		Process process = Runtime.getRuntime().exec(cmd, null, workDir);
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			result.add(line);
			processLine(line, options);
			
			if (!line.startsWith("Progress"))
				System.out.println(line);
		}
		int status = process.waitFor();
		if (status != 0)
			return null;

		return result;

	}
	
	private static void processLine(String line, String[] options) throws IOException {
		if(options.length > 1) {
			switch(CmdLineProcess.fromOption(options[1])) {
			case SEND_CLIENT:
				if (!line.startsWith("Progress"))
					WebSocketServer.session.getBasicRemote().sendText("{show}" + line);
				break;
			}
		}
	}
	
	private static String jdkPath(JavaVersion jdk) {
		switch (jdk) {
		case JAVA11: 
			return GitUtil.java11;
		case JAVA8:
			return GitUtil.java8;
		default:
			return GitUtil.java11;
		}
	}
	
	// ------------------------------------------------------------------------------------------------------
	
	public static void main(String[] args) {
		String script = "mvn -v";
		List<String> list = testProcessShell(script, "");
		System.out.println(list);
	}
	
	public static List<String> testProcessShell(String script, String args, String... options) {

		String[] cmd = new String[] { "powershell.exe", "-c", 
				"$env:JAVA_HOME='C:\\systex\\tools\\JDK\\openlogic-openjdk-11.0.22+7-windows-x64' ;", 
				"$env:PATH=\"C:\\systex\\tools\\JDK\\openlogic-openjdk-11.0.22+7-windows-x64\\bin\" + $env:PATH ;"
				, script };
		File dir = getWorkspace(options);
		
		try {
			return doCmd(cmd, dir, options);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}
}
