package com.revature.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FrontController
 */
public class FrontController extends HttpServlet {
       
	// Default serial ID so it stops barking at me
	private static final long serialVersionUID = 1L;

	/**
	 * This method will be responsible for determining what resource the client is requesting
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1. URI rewriting
		// http://localhost:8080/employee-servlet-app/login -- we want to capture login
		// http://localhost:8080/employee-servlet-app/employees -- if they go here it returns all employees in the DB
		final String URI = request.getRequestURI().replace("/employee-servlet-app/", "");
		// we're capturing the very last part of the URI
		// set up a switch case statement in which we call the appropriate functionality based on the URI returned
		switch(URI) {
		case "":
			RequestHelper.processDashboard(request, response);
			break;
		case "employees":
			// invoke some functionality from the request helper which would return all employees
			RequestHelper.processEmployees(request, response);
			break;
		case "reimbursements":
			ReimbursementHelper.processReimbursements(request, response);
			break;
//		case "dashboard":
//			RequestHelper.processDashboard(request, response);
//			break;
		default:
			// TODO: Redirect to 404
			response.setStatus(404);
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String URI = request.getRequestURI().replace("/employee-servlet-app/", "");
		switch(URI) {
		case "login":
			// invoke some function from the RequestHelper
			RequestHelper.processLogin(request, response);
			break;
		case "logout":
			RequestHelper.processLogout(request, response);
			break;
		case "register":
			RequestHelper.processRegistration(request, response);
			break;
		default:
			response.setStatus(404);
			break;
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) {
		final String URI = request.getRequestURI().replace("/employee-servlet-app/", "");
		switch(URI) {
		case "reimbursements/approve":
			
			break;
		case "reimbursements/deny":
			
		default:
			response.setStatus(404);
			break;
		}
	}

}
