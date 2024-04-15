package com.example.demo.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.enumerate.JavaVersion;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "git_project")
public class GitProject {
	
	@Id
	@Column(name = "ID", columnDefinition = "INT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "NAME", columnDefinition = "VARCHAR(50)", unique = true)
	private String name;
	
	@Column(name = "URL", columnDefinition = "VARCHAR(225)")
	private String url;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "JDK", columnDefinition = "VARCHAR(25)")
	private JavaVersion jdk;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
	private GitProject parent;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL)
	private List<GitProject> children = new ArrayList<>();
}
