package com.revature.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.revature.dao.EmployeeDao;
import com.revature.models.Employee;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.service.EmployeeService;

public class RequestHelper {
	
	// employee service
	private static EmployeeService eserv = new EmployeeService(new EmployeeDao());
	// object mapper (for frontend)
	private static ObjectMapper om = new ObjectMapper();
	
	/*
	 * This method will call the EmployeeService's  findAll method()
	 * -- use an object mapper to transform that list to a JSON String
	 * -- then use the print writer to print out that JSON string to the screen
	 */
	public static void processEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// http://localhost:8080/employee-servlet-app/employees
		// will return me an entire list of all the employees in JSON
		
		// 1. set the content type to be application/json
//		response.setContentType("application/json");
		response.setContentType("text/html");
		
		// 2. Call the getAll() method from the employee service
		List<Employee> emps = eserv.getAll();
		
		// 3. transform the list to a string
		String jsonString = om.writeValueAsString(emps);
		
		// get print writer, then write it out
		PrintWriter out = response.getWriter();
		out.write(jsonString); // write the string to the response body	
	}
	
	
	
	public static void processRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(firstname == null || lastname == null || username == null || password == null) {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("<h1>Registration failed. One or more null values in request.</h1>");
			out.println("<a href=\"index.html\">Back</a>");
			return;
		}
		Employee e = new Employee(firstname, lastname, username, password, Role.Employee);
		int pk = eserv.register(e);
		if (pk > 0 ) {
			e.setId(pk);
			// add the user to the session
			HttpSession session = request.getSession();
			session.setAttribute("the-user", e);
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} else {
			// if it's -1, that means the register method failed (and there's probably a duplicate user)
			// TODO: provide better logic in the Service layer to check for PSQL exceptions
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			response.setStatus(409); // Conflict - duplicate unique value
			out.println("<h1>Registration failed.  Username already exists</h1>");
			out.println("<a href=\"index.html\">Back</a>");
		}
	}
	
	/**
	 * What does this method do?
	 * 
	 * It extracts the parameters from a request (username and password) from the UI
	 * It will call the confirmLogin() method from the EmployeeService and 
	 * see if a user with that username and password exists
	 * 
	 * Who will provide the method with the HttpRequest? The UI
	 * We need to build an html doc with a form that will send these prameters to the method
	 */
	public static void processLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 1. Extract the parameters from the request (username & password)
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 2. call the confirm login(0 method from the employeeService and see what it returns
		Employee e = eserv.confirmLogin(username, password);
		// 3. If the user exists, lets print their info to the screen
		if (e.getId() > 0) {
			// grab the session
			HttpSession session = request.getSession();
			// add the user to the session
			session.setAttribute("the-user", e);
			// Redirect to dashboard
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} else {
			response.setStatus(401); // Unauthorized
			response.sendRedirect(request.getContextPath());
		}
	}

	/**
	 * Remove current user from session
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public static void processLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO: Edit HTML to use requests for logout
		HttpSession session = request.getSession();
		session.removeAttribute("the-user");
		response.sendRedirect(request.getContextPath());
	}

	/**
	 * Send user to dashboard if logged in
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void processDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// If there is an employee in the session, go to dashboard, else back to login
		HttpSession session = request.getSession();
		Employee e = (Employee) session.getAttribute("the-user");
		if(e != null && e.getId() > 0) {
			// Go to dashboard
			// TODO: change welcome.html to dashboard's html
			response.setStatus(200);
			if(e.getRole().equals(Role.Manager)) {
				request.getRequestDispatcher("manager.html").forward(request, response);
			} else {
				request.getRequestDispatcher("welcome.html").forward(request, response);
			}
		} else {
			// Redirect to landing page
			response.setStatus(401);
			response.sendRedirect(request.getContextPath());
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public static void getMe(HttpServletRequest request, HttpServletResponse response) {
		// TODO: Think about what user information we actually want to send (probably not the password)
		HttpSession session = request.getSession();
		Employee e = (Employee) session.getAttribute("the-user");
		try (PrintWriter out = response.getWriter()) {
			String json = om.writeValueAsString(e);
			response.setContentType("application/json");
			response.setStatus(200);
			out.write(json);
		} catch (IOException e1) {
			response.setStatus(500);
			e1.printStackTrace();
		}
		
	}
	
	public static void getReimbursements(HttpServletRequest request, HttpServletResponse response) {
		// TODO: Think about what user information we actually want to send (probably not the password)
		HttpSession session = request.getSession();
		Employee e = (Employee) session.getAttribute("the-user");
		try (PrintWriter out = response.getWriter()) {
			String json = om.writeValueAsString(e);
			response.setContentType("application/json");
			response.setStatus(200);
			out.write(json);
		} catch (IOException e1) {
			response.setStatus(500);
			e1.printStackTrace();
		}
		
	}
	
	public static void getReimbursementsById(HttpServletRequest request, HttpServletResponse response) {
		// TODO: Think about what user information we actually want to send (probably not the password)
		HttpSession session = request.getSession();
		Employee e = (Employee) session.getAttribute("the-user");
		try (PrintWriter out = response.getWriter()) {
			String json = om.writeValueAsString(e);
			response.setContentType("application/json");
			response.setStatus(200);
			out.write(json);
		} catch (IOException e1) {
			response.setStatus(500);
			e1.printStackTrace();
		}
		
	}
	
	public static void updateUser(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		try (PrintWriter out = response.getWriter()) {
			HttpSession session = request.getSession();
			Employee user = (Employee) session.getAttribute("the-user");
			if(user == null) {
				response.setStatus(401);
				out.print("{\"message\": \"You must be logged in to submit a reimbursement request.\"}");
				return;
			}
			
			JsonParser jsonParser = new JsonParser();
			JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) request.getInputStream()));
			JsonObject jsonobj = root.getAsJsonObject();

			String firstName = jsonobj.get("firstName").getAsString();
			String lastName = jsonobj.get("lastName").getAsString();
			String username = jsonobj.get("username").getAsString();
			String password = jsonobj.get("password").getAsString();
			System.out.println(user.toString());
			if(firstName != null && !firstName.isEmpty()) {
				user.setFirstName(firstName);
			}
			if(lastName != null && !lastName.isEmpty()) {
				user.setLastName(lastName);
			}
			if(username != null && !username.isEmpty()) {
				user.setUsername(username);
			}
			if(password != null && !password.isEmpty()) {
				user.setPassword(password);
			}
			System.out.println(user.toString());
			eserv.updateInfo(user);
			String json = om.writeValueAsString(user);
			response.setStatus(200);
			out.print(json);
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
		
	}

}