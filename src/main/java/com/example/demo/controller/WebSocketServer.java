package com.example.demo.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ProjectBranchDto;
import com.example.demo.service.GitService;
import com.example.demo.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/compiler")
public class WebSocketServer {
	
	private static GitService gitService;
	
	public static Session session;
	
	@Autowired
	public void setGitService (GitService gitService) {
		WebSocketServer.gitService = gitService;
	}
	
	@OnOpen
	public void startCompile(Session session) {
		System.out.println("socket open");
		WebSocketServer.session = session;
	}
	
	/*
	 *  split : [0] - mission name
	 */
	@OnMessage
	public void getMission(String msg, Session session) {
		System.out.println(msg);
		String[] split = msg.split(",split,");
		for (int i = 0; i < split.length; i++) {
		    split[i] = split[i].replace(" ", "").replace("\t", "");
		}
		switch (split[0]) {
		// split : [1] - projectName , [2] - gitAccount , [3] - Base64 : [{gitProject: 'name1', branchName: 'branch1'},..]
		case "compile": 
			Map<String,String> map = getBranchMap(split[3]);
			
			boolean result = gitService.compileProject(split[2], split[1], map);
			send(result ? "y" : "n");
		}
	}
	
	@OnClose
	public void endCompile(Session session) {
		System.out.println("socket close");
	}
	
	public void send(String msg) {
		try {
			WebSocketServer.session.getBasicRemote().sendText(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ------------------------------------------------------------------------------------------------------
	
	private Map<String, String> getBranchMap(String branchBase64) {
		Map<String, String> map = new HashMap<>();
		
		String decodedStr = new String(Base64.getDecoder().decode(branchBase64));
		String json = URLDecoder.decode(decodedStr, StandardCharsets.UTF_8).replace(" ", "").replace("\t", "");
		try {
			ProjectBranchDto[] dtoList = JsonUtil.fromJson(json, ProjectBranchDto[].class);
			for (ProjectBranchDto dto : dtoList) {
				map.put(dto.getName(), dto.getBranch());
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println(map);
		return map;
	}

}
