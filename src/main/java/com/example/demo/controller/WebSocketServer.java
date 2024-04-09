package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.service.GitService;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/compiler")
public class WebSocketServer {
	
	@Autowired
	private GitService gitService;
	
	@OnOpen
	public void startCompile(Session session) {
		System.out.println("socket open");
	}
	
	@OnMessage
	public void getMission(String msg, Session session) {
		System.out.println(msg);
		String[] split = msg.split(",split,");
		for (int i = 0; i < split.length; i++) {
		    split[i] = split[i].replace(" ", "").replace("\t", "");
		}
		switch (split[0]) {
		case "dap-api": 
			System.out.println(split[1]);
			System.out.println(split[1].length());
			break;
		}
	}
	
	@OnClose
	public void endCompile(Session session) {
		System.out.println("socket close");
	}
	
	public void send(Session session, String msg) {
		try {
			session.getBasicRemote().sendText(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
