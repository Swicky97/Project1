package com.revature.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.revature.dao.EmployeeDao;
import com.revature.dao.ReimbursementDao;
import com.revature.models.Employee;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.service.EmployeeService;
import com.revature.service.ReimbursementService;

public class ReimbursementHelper {

	private static ReimbursementService rserv = new ReimbursementService(new ReimbursementDao());
	private static EmployeeService eserv = new EmployeeService(new EmployeeDao());
	// object mapper (for frontend)
	private static ObjectMapper om = new ObjectMapper();
	
	/**
	 * Return a JSON list of all reimbursements
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public static void getReimbursements(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		List<Reimbursement> reimbursements = rserv.getAll();
		String jsonString = om.writeValueAsString(reimbursements);
		PrintWriter out = response.getWriter();
		out.write(jsonString); // write the string to the response body	
	}

	/**
	 * Retrieve reimbursement requests authored by a given user
	 * Expects request to have parameters (in query string):
	 * 		"author_id"
	 * @param request
	 * @param response
	 */
	public static void getAssociatedReimbursements(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("application/json");
		int authorId = Integer.parseInt(request.getParameter("authorId"));
		List<Reimbursement> rList = rserv.getReimbursementsByAuthor(authorId);
		try (PrintWriter out = response.getWriter()) {
			String json = om.writeValueAsString(rList);
			response.setStatus(200);
			out.write(json);
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 */
	public static void getResolved(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		
		try(PrintWriter out = response.getWriter()) {
			List<Reimbursement> rList = rserv.getResolved();
			List<Employee> managers = eserv.getManagers();
			List<JsonObject> jsonArray = new LinkedList<>();
			for(Reimbursement r : rList) {
				JsonObject jsonObject = new JsonObject();
				Optional<Employee> manager = managers.stream().filter(e -> e.getId() == r.getReimbResolver()).findAny();
				jsonObject.addProperty("id", r.getId());
				jsonObject.addProperty("amount", r.getReimbAmount());
				jsonObject.addProperty("description", r.getReimbDescription());
				jsonObject.addProperty("submitted", r.getReimbSubmitted().getTime());
				jsonObject.addProperty("resolved", r.getReimbResolved().getTime());
				jsonObject.addProperty("approved", r.isReimbApproved());
				jsonObject.addProperty("resolver", manager.isPresent() ? manager.get().getUsername() : null);
				jsonObject.addProperty("resolverId", r.getReimbResolver());
				jsonArray.add(jsonObject);
			}
			
			response.setStatus(200);
			out.print(new Gson().toJson(jsonArray));
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}

	/**
	 * Retrieve JSON object of all outstanding reimbursements
	 * @param request
	 * @param response
	 */
	public static void getUnresolved(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		List<Reimbursement> rList = rserv.getAll().stream()
				.filter(r -> r.getReimbResolved() == null)
				.collect(Collectors.toList());
		System.out.println(rList);
		try(PrintWriter out = response.getWriter()) {
			response.setContentType("application/json");
			String json = om.writeValueAsString(rList);
			response.setStatus(200);
			out.write(json);
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Retrieve all reimbursement requests authored by the current user
	 * @param request
	 * @param response
	 */
	public static void getUsersReimbursements(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try (PrintWriter out = response.getWriter()) {
			HttpSession session = request.getSession();
			Employee user = (Employee) session.getAttribute("the-user");
			if(user == null) {
				response.setStatus(401);
				out.print("{\"message\": \"You must be logged in to view your reimbursement requests.\"}");
				return;
			}
			int authorId = user.getId();
			List<Reimbursement> rList = rserv.getReimbursementsByAuthor(authorId);
			String json = om.writeValueAsString(rList);
			response.setStatus(200);
			out.write(json);
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new reimbursement request, so long as the user is logged in
	 * Expect request to have parameters
	 * 		"amount"
	 * 		"description"
	 * @param request
	 * @param response
	 */
	public static void addReimbursement(HttpServletRequest request, HttpServletResponse response) {
		// TODO: Edit once parameters passed to server are finalized
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

			double amount = jsonobj.get("amount").getAsDouble();
			Timestamp submitted = new Timestamp(System.currentTimeMillis());
			submitted.toLocalDateTime();
			Timestamp resolved = null;
			boolean approved = false;
			int author = user.getId();
			String description = jsonobj.get("description").getAsString();
			
			Reimbursement r = new Reimbursement(amount, submitted, resolved, approved, description, author, -1);
			int id = rserv.add(r);
			r.setId(id);
			String json = om.writeValueAsString(r);
			response.setStatus(200);
			out.print(json);
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}

	/**
	 * Approves a given request, so long as the current user is a Manager
	 * Expects request to have parameters:
	 * 		"id", the Reimbursement's ID
	 * @param request
	 * @param response
	 */
	public static void processApprove(HttpServletRequest request, HttpServletResponse response) {
		// TODO Re-evaluate when form is ready
		response.setContentType("application/json");
		try (PrintWriter out = response.getWriter()) {
			HttpSession session = request.getSession();
			Employee user = (Employee) session.getAttribute("the-user");
			if(user == null) {
				response.setStatus(401);
				out.print("{\"message\": \"You must be logged in to approve a reimbursement request.\"}");
				return;
			}
			if(!user.getRole().equals(Role.Manager)) {
				response.setStatus(401);
				out.print("{\"message\": \"Only managers may approve reimbursement requests.\"}");
				return;
			}
			
			JsonParser jsonParser = new JsonParser();
			JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) request.getInputStream()));
			JsonObject jsonobj = root.getAsJsonObject();
			
			int resolver = user.getId();
			int id = jsonobj.get("id").getAsInt();
			Optional<Reimbursement> optR = rserv.getAll().stream().filter(e -> e.getId() == id).findAny();
			if(optR.isPresent()) {
				Reimbursement r = optR.get();
				rserv.approve(r, resolver);
				response.setContentType("application/json");
				String json = om.writeValueAsString(r);
				out.print(json);
			} else {
				response.setStatus(404);
				response.setContentType("application/json");
				out.print("{\"message\": \"Could not find request with ID " + id + ".\"}");
			}
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}

	/**
	 * Denies a given request, so long as the current user is a Manager
	 * Expects request to have parameters:
	 * 		"id", the Reimbursement's ID
	 * @param request
	 * @param response
	 */
	public static void processDeny(HttpServletRequest request, HttpServletResponse response) {
		// TODO: Re-evaluate when front-end is ready-er
		response.setContentType("application/json");
		try (PrintWriter out = response.getWriter()) {
			HttpSession session = request.getSession();
			
			JsonParser jsonParser = new JsonParser();
			JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) request.getInputStream()));
			JsonObject jsonobj = root.getAsJsonObject();
			
			Employee user = (Employee) session.getAttribute("the-user");
			if(user == null) {
				response.setStatus(401);
				out.print("{\"message\": \"You must be logged in to deny a reimbursement request.\"}");
				return;
			}
			if(!user.getRole().equals(Role.Manager)) {
				response.setStatus(401);
				out.print("{\"message\": \"Only managers may deny reimbursement requests.\"}");
				return;
			}
			int resolver = user.getId();
			
			int id = jsonobj.get("id").getAsInt();
			
			Optional<Reimbursement> optR = rserv.getAll().stream().filter(e -> e.getId() == id).findAny();
			if(optR.isPresent()) {
				Reimbursement r = optR.get();
				rserv.deny(r, resolver);
				response.setContentType("application/json");
				String json = om.writeValueAsString(r);
				out.print(json);
			} else {
				response.setStatus(404);
				response.setContentType("application/json");
				out.print("{\"message\": \"Could not find request with ID " + id + ".\"}");
			}
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}

	/**
	 * Deletes a given request, so long as the current user is the user who created the request
	 * Expects request to have parameters:
	 * 		"id", the Reimbursement's ID
	 * @param request
	 * @param response
	 */
	public static void processDelete(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		try (PrintWriter out = response.getWriter()) {
			HttpSession session = request.getSession();
			Employee user = (Employee) session.getAttribute("the-user");
			if(user == null) {
				response.setStatus(401);
				out.print("{\"message\": \"You must be logged in to delete a reimbursement request.\"}");
				return;
			}
			int resolver = user.getId();
			int id = Integer.parseInt(request.getParameter("id"));
			Optional<Reimbursement> optR = rserv.getAll().stream().filter(e -> e.getId() == id).findAny();
			if(optR.isPresent()) {
				Reimbursement r = optR.get();
				if(r.getReimbAuthor() != user.getId()) {
					response.setStatus(401);
					out.print("{\"message\": \"Only a request's author can delete their request.\"}");
					return;
				}
				rserv.deny(r, resolver);
				response.setContentType("application/json");
				String json = om.writeValueAsString(r);
				out.print(json);
			} else {
				response.setStatus(404);
				response.setContentType("application/json");
				out.print("{\"message\": \"Could not find request with ID " + id + ".\"}");
			}
		} catch (IOException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}
}
