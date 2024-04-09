package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "git_repository")
public class GitRepository {

	@Id
	@Column(name = "ID", columnDefinition = "INT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "PROJECT_NAME", columnDefinition = "VARCHAR(50)")
	private String projectName;

	@ManyToOne
	@JoinColumn(name = "OWNER",referencedColumnName = "GIT_ACCOUNT")
	@JsonIgnore
	private UserData owner;
	
}
