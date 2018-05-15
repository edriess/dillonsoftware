package com.dillonsoftware.webservices.resource;

import com.dillonsoftware.webservices.bean.Error;
import com.dillonsoftware.webservices.bean.User;
import com.dillonsoftware.webservices.service.UserService;
import com.dillonsoftware.webservices.test.MockHelper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

	@Mock
	private UserService userService;

	@Mock
	private HttpServletRequest httpServletRequest;

	@InjectMocks
	private UserResource userResource;

	@Before
	public void before() {
		ResteasyProviderFactory.getContextDataMap().put(HttpServletRequest.class, httpServletRequest);
	}


	@Test
	public void should_list_users() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(userResource);

		final Integer userId = 1;
		final String name = "name";

		final User expectedUser = new User() {
			{
				setId(userId);
				setName(name);
			}
		};

		final List<User> expectedUsers = new ArrayList<User>() {
			{
				add(expectedUser);
			}
		};

		final CollectionType resultType = TypeFactory.defaultInstance().constructCollectionType(List.class, User.class);
		final String expectedResponse = new ObjectMapper().writer().forType(resultType).writeValueAsString(expectedUsers);

		when(userService.listUsers()).thenReturn(expectedUsers);

		final MockHttpRequest request = MockHttpRequest.get("/users");
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(userService).listUsers();
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	public void should_get_user() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(userResource);

		final Integer userId = 1;
		final String name = "name";

		final User expectedUser = new User() {
			{
				setId(userId);
				setName(name);
			}
		};

		final String expectedResponse = new ObjectMapper().writer().forType(User.class).writeValueAsString(expectedUser);

		when(userService.getUser(userId)).thenReturn(expectedUser);

		final MockHttpRequest request = MockHttpRequest.get("/users/" + userId);
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(userService).getUser(userId);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	public void should_not_get_missing_user() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(userResource);

		final Integer userId = 1;
		final Error error = new Error() {
			{
				setMessage("User not found.");
			}
		};

		final String expectedResponse = new ObjectMapper().writer().forType(Error.class).writeValueAsString(error);

		when(userService.getUser(userId)).thenReturn(null);

		final MockHttpRequest request = MockHttpRequest.get("/users/" + userId);
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(userService).getUser(userId);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	public void should_list_sorted_users() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(userResource);

		final Integer userId = 1;
		final Integer userId2 = 2;
		final Integer userId3 = 3;

		final String name = "name1";
		final String name2 = "a_name2";
		final String name3 = "z_name3";

		final User expectedUser = new User() {
			{
				setId(userId);
				setName(name);
			}
		};
		final User expectedUser2 = new User() {
			{
				setId(userId2);
				setName(name2);
			}
		};
		final User expectedUser3 = new User() {
			{
				setId(userId3);
				setName(name3);
			}
		};

        //add users in the sorted order, as that is what the response should return.
		final List<User> expectedUsers = new ArrayList<User>() {
			{
				add(expectedUser2);
				add(expectedUser);
				add(expectedUser3);
			}
		};

		final CollectionType resultType = TypeFactory.defaultInstance().constructCollectionType(List.class, User.class);
		final String expectedResponse = new ObjectMapper().writer().forType(resultType).writeValueAsString(expectedUsers);

		when(userService.listUsers()).thenReturn(expectedUsers);

		final MockHttpRequest request = MockHttpRequest.get("/users");
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(userService).listUsers();
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	public void should_find_user() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(userResource);

		final Integer userId = 1;
		final String name = "name";

		final User expectedUser = new User() {
			{
				setId(userId);
				setName(name);
			}
		};

		final List<User> userList = new ArrayList<>();
		userList.add(expectedUser);


		final String expectedResponse = new ObjectMapper().writer().forType(User.class).writeValueAsString(expectedUser);

		when(userService.findUsers(name)).thenReturn(userList);

		final MockHttpRequest request = MockHttpRequest.get("/users?name=" + name);
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(userService).findUsers(name);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals("[" + expectedResponse + "]", response.getContentAsString());
	}

	@Test
	public void should_add_user() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(userResource);

		final String name = "somename";
		final MockHttpRequest request = MockHttpRequest.get("/users/add/" + name);
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(userService).addUser(name);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals("", response.getContentAsString());
	}

	@Test
	public void should_remove_user() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(userResource);

		final Integer idToRemove = 1;
		final MockHttpRequest request = MockHttpRequest.get("/users/remove/" + idToRemove);
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(userService).removeUser(idToRemove);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals("", response.getContentAsString());
	}


}
