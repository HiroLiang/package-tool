package com.example.demo.model.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_data")
public class UserData {
	
	@Id
	@Column(name = "ID", columnDefinition = "INT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "GIT_ACCOUNT", columnDefinition = "VARCHAR(50)", unique = true)
	private String gitAccount;
	@Column(name = "ACCESS_TOKEN", columnDefinition = "VARCHAR(225)")
	private String accessToken;
	@Column(name = "USER_NAME", columnDefinition = "VARCHAR(50)")
	private String userName;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<GitRepository> repositories;

}
