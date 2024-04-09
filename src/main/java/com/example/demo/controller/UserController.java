package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.UserData;
import com.example.demo.service.UserService;
import com.example.demo.util.EncryptUtil;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/encrypt/{str}")
	public String getEncryptStr(@PathVariable String str) {
		return EncryptUtil.encrypt(str + "", EncryptUtil.getSystemKey());
	}

	@GetMapping("/{id}")
	public UserData getCurrenUser(@PathVariable String id) {
		String userId = EncryptUtil.decrypt(id, EncryptUtil.getSystemKey());
		return userService.getUser(Integer.valueOf(userId));
	}

	@PostMapping("")
	public UserData createUser(@RequestBody UserData user) {
		return userService.addUser(user);
	}

	@PutMapping("/{id}")
	public UserData setUserData(@PathVariable Integer id, @RequestBody UserData data) {
		return userService.updateUser(id, data);
	}

}
