package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_project")
public class UserProject {

	@EmbeddedId
	private UserProjectId id;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "OWNER", referencedColumnName = "GIT_ACCOUNT", insertable = false, updatable = false)
	private UserData owner;
	
	@ManyToOne
	@JoinColumn(name = "PROJECT", referencedColumnName = "ID", insertable = false, updatable = false)
	private GitProject project;
	
}
