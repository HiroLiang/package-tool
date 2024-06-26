package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.UserData;
import com.example.demo.model.repository.UserDataRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserDataRepository userDataRepository;

	@Transactional
	public UserData addUser(UserData user) {
		Optional<UserData> optional = userDataRepository.findByGitAccount(user.getGitAccount());
		if(optional.isPresent())
			return optional.get();
		return userDataRepository.save(user);
		
	}

	public UserData getUser(Integer id) {
		Optional<UserData> optional = userDataRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return new UserData(0, "", "", "", null);
	}
	
	public UserData getUser(String gitAccount) {
		Optional<UserData> optional = userDataRepository.findByGitAccount(gitAccount);
		if(optional.isPresent())
			return optional.get();
		return null;
	}

	@Transactional
	public UserData updateUser(Integer id, UserData data) {
		UserData user = getUser(id);
		if (user.getId() > 0 && id == data.getId())
			return userDataRepository.save(data);
		return user;
	}
}
