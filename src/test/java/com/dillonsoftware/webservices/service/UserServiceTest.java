package com.dillonsoftware.webservices.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.dillonsoftware.webservices.bean.User;
import com.dillonsoftware.webservices.mybatis.mapper.UserMapper;
import com.dillonsoftware.webservices.test.MockHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private UserService userService;

	@Test
	public void should_list_users() {
		final List<User> expectedUsers = new ArrayList<>();

		when(userMapper.list()).thenReturn(expectedUsers);

		final List<User> users = userService.listUsers();

		verify(userMapper).list();
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(expectedUsers, users);
	}


	@Test
	public void should_fetch_user() {
		final Integer userId = 1;
		final User expectedUser = new User();

		when(userMapper.fetch(userId)).thenReturn(expectedUser);

		final User user = userService.getUser(userId);

		verify(userMapper).fetch(userId);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(expectedUser, user);
	}

	@Test
	public void should_find_user() {
		final String name = "some name";
		final User expectedUser = new User();
		final List<User> userList = new ArrayList<>();
		userList.add(expectedUser);

		when(userMapper.find(name)).thenReturn(userList);

		final List<User> foundUserList = userService.findUsers(name);

		final User user = foundUserList.get(0);
		verify(userMapper).find(name);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(expectedUser, user);
	}

	@Test
	public void should_add_user() {
		final String name = "some name";
		final User expectedUser = new User();
		final List<User> userList = new ArrayList<>();
		userList.add(expectedUser);

		when(userMapper.find(name)).thenReturn(userList);

		userService.addUser(name);

		verify(userMapper).add(name);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		final List<User> foundUserList = userService.findUsers(name);
		assertEquals(1, foundUserList.size());
		final User newUser = foundUserList.get(0);

		assertEquals(expectedUser, newUser);
	}

}
