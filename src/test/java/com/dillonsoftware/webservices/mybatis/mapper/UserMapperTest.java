package com.dillonsoftware.webservices.mybatis.mapper;

import com.dillonsoftware.webservices.bean.User;
import com.dillonsoftware.webservices.spring.DataConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = {DataConfiguration.class})
@Rollback
@Transactional
@Component
public class UserMapperTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void should_list_users() {
		final List<User> users = userMapper.list();

		assertEquals(4, users.size());
	}

	@Test
	public void should_fetch_user() {
		final List<User> users = userMapper.list();
		final User firstUser = users.get(0);

		final User user = userMapper.fetch(firstUser.getId());

		assertEquals(firstUser.getId(), user.getId());
		assertEquals(firstUser.getName(), user.getName());
	}

	@Test
	public void should_find_user() {
		final List<User> users = userMapper.list();
		final User firstUser = users.get(0);

		final List<User> usersFound = userMapper.find(firstUser.getName());

		assertEquals(firstUser.getId(), usersFound.get(0).getId());
		assertEquals(firstUser.getName(), usersFound.get(0).getName());
	}

	@Test
	public void should_find_user_by_partial_name() {
		final List<User> usersFound = userMapper.find("walk");

		assertEquals(1, usersFound.size());
		assertEquals(new Integer(1), usersFound.get(0).getId());
		assertEquals("Luke Skywalker", usersFound.get(0).getName());
	}

	@Test
	public void should_add_user() {
		final String name = "Joe Blow";
		final List<User> initialUsersFound = userMapper.find(name);
		assertEquals(0, initialUsersFound.size());

		userMapper.add(name);

		final List<User> usersFound = userMapper.find(name);
		final User newUser = usersFound.get(0);

		assertEquals(1, usersFound.size());
		assertTrue(newUser.getId() > 0);
		assertEquals(name, newUser.getName());
	}

	@Test
	public void should_remove_user() {
		final int idToRemove = 2;
		final List<User> initialUsersFound = userMapper.list();
		assertEquals(4, initialUsersFound.size());

		userMapper.remove(idToRemove);

		final List<User> usersFound = userMapper.list();
		assertEquals(3, usersFound.size());

	}

}
