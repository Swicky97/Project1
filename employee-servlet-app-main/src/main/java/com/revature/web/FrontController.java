package com.revature.web;

import java.io.IOException;
import java.io.PrintWriter;

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
		case "me":
			RequestHelper.getMe(request, response);
			break;
		case "employees":
			// invoke some functionality from the request helper which would return all employees
			RequestHelper.processEmployees(request, response);
			break;
		case "reimbursement":
			if(request.getQueryString() == null) {
				ReimbursementHelper.getReimbursements(request, response);
			} else {
				
				ReimbursementHelper.getAssociatedReimbursements(request, response);
			}
			break;
		case "reimbursement/mine":
			ReimbursementHelper.getUsersReimbursements(request, response);
			break;
		case "reimbursement/resolved":
			ReimbursementHelper.getResolved(request, response);
			break;
		case "reimbursement/unresolved":
			ReimbursementHelper.getUnresolved(request, response);
			break;
		case "dashboard":
			RequestHelper.processDashboard(request, response);
			break;
		default:
			// TODO: Redirect to 404
			response.setStatus(404);
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
		case "reimbursement":
			ReimbursementHelper.addReimbursement(request, response);
			break;
		default:
			response.setStatus(404);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print("{\"message\": \"No resource found at location: " + URI + "\"}");
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String URI = request.getRequestURI().replace("/employee-servlet-app/", "");
		switch(URI) {
		case "reimbursement/approve":
			ReimbursementHelper.processApprove(request, response);
			break;
		case "reimbursement/deny":
			ReimbursementHelper.processDeny(request, response);
			break;
		default:
			response.setStatus(404);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print("{\"message\": \"No resource found at location: " + URI + "\"}");
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String URI = request.getRequestURI().replace("/employee-servlet-app/", "");
		switch(URI) {
		case "reimbursement":
			ReimbursementHelper.processDelete(request, response);
			break;
		default:
			response.setStatus(404);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print("{\"message\": \"No resource found at location: " + URI + "\"}");
		}
	}
}
