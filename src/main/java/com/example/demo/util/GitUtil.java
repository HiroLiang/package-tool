package com.example.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GitUtil {

	public static boolean createFolder(String path) {
		String cmd = "cmd.exe /c mkdir " + path;

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
	
	public static boolean gitCheckout(String branchs, String path, String remote) {
		String script = "git remote set-url origin " + remote;
		System.out.println("process : " + script);
		List<String> result = processShell(script, "", path);
		if(result.size() == 0)
			return false;
		
		script = "git checkout -b " + branchs + " origin/" + branchs;
		System.out.println("process : " + script);
		result = processShell(script, "", path);
		if(result.size() == 0)
			return false;
		
		return true;
	}

	public static boolean gitClone(String url, String path) {
		String script = "git clone " + url + " " + path;
		System.out.println("process : " + script);
		List<String> result = processShell(script, "");
		if(result.size() == 0)
			return false;
		return true;
	}

	public static List<String> processShell(String script, String args, String... workspace) {
		List<String> result = new ArrayList<String>();
		String[] cmd = new String[] { "powershell.exe", "-c", script };

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
				System.out.println(line);
			}

			int status = process.waitFor();
			if(status != 0)
				return new ArrayList<String>();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void main(String[] args) {
		processShell(
				"git clone https://2301031:glpat-ZRh5ftssQFUHfa9JginF@git.systex.com/misystex/050_tbbbank/b05002204_tbb_asp/backend/dap-adi-admin.git D:\\docker\\git\\base\\2301031\\dap-api-admin",
				"");
	}

}
