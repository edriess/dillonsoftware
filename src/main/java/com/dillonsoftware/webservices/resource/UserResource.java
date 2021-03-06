package com.dillonsoftware.webservices.resource;

import com.dillonsoftware.webservices.bean.Error;
import com.dillonsoftware.webservices.bean.User;
import com.dillonsoftware.webservices.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.Comparator;
import java.util.List;

@Service
@Path("/users")
public class UserResource {

	private final UserService userService;

	@Autowired
	public UserResource(
			final UserService userService) {
		this.userService = userService;
	}


	@GET
	@Produces("application/json;charset=utf-8")
	public Response getUsers(@QueryParam("name") final String name) {
		List<User> users;
		if (name != null) {
			users = userService.findUsers(name);
		} else {
			users = userService.listUsers();
		}

		if (users == null || users.isEmpty()) {
			final Error error = new Error();
			error.setMessage("No Users found.");
			return Response.status(Response.Status.NOT_FOUND).entity(error).build();
		} else {
			users.sort(new SortUsersByName());
			return Response.status(Response.Status.OK).entity(users).build();
		}
	}


	@Path("/{userId}")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getUser(@PathParam("userId") final Integer userId) {
		final User user = userService.getUser(userId);

		if (null == user) {
			final Error error = new Error();
			error.setMessage("User not found.");
			return Response.status(Response.Status.NOT_FOUND).entity(error).build();
		} else {
			return Response.status(Response.Status.OK).entity(user).build();
		}
	}

	@Path("/add/{name}")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getUser(@PathParam("name") final String name) {

		userService.addUser(name);

		return Response.status(Response.Status.OK).build();
	}

	@Path("/remove/{userId}")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response removeUser(@PathParam("userId") final Integer userId) {

		userService.removeUser(userId);

		return Response.status(Response.Status.OK).build();
	}


	class SortUsersByName implements Comparator<User> {
		@Override
		public int compare(final User u1, final User u2) {
			return u1.getName().compareTo(u2.getName());
		}
	}

}
