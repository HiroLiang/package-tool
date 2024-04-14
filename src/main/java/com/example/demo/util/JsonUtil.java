package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	
	public static <T> T fromJson(String json, Class<T> clazz) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, clazz);
	}

}
