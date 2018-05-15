package com.dillonsoftware.webservices.service;

import com.dillonsoftware.webservices.bean.User;
import com.dillonsoftware.webservices.mybatis.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserMapper userMapper;

	@Autowired
	public UserService(
			final UserMapper userMapper) {
		this.userMapper = userMapper;
	}


	public List<User> listUsers() {
		return userMapper.list();
	}

	public List<User> findUsers(final String name) {
		return userMapper.find(name);
	}


	public User getUser(final Integer userId) {
		return userMapper.fetch(userId);
	}


}
