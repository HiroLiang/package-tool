package com.example.demo.model.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserProjectId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "OWNER", referencedColumnName = "GIT_ACCOUNT")
	private UserData owner;
	
	@ManyToOne
    @JoinColumn(name = "PROJECT", referencedColumnName = "ID")
    private GitProject project;
    
}
