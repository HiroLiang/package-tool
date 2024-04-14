package com.example.demo.model.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitProjectBranchs {
	
	private String name;
	
	private List<String> branchs = new ArrayList<>();
	
	private List<GitProjectBranchs> childern = new ArrayList<>();
 
}
