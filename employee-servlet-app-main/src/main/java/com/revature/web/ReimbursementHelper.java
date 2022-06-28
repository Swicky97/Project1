package com.revature.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.ReimbursementDao;
import com.revature.models.Employee;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.service.ReimbursementService;

public class ReimbursementHelper {

	private static ReimbursementService rserv = new ReimbursementService(new ReimbursementDao());
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
	 * Creates a new reimbursement request, so long as the user is logged in
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
			double amount = Double.parseDouble(request.getParameter("amount"));
			long submitted = System.currentTimeMillis();
			long resolved = -1L;
			boolean approved = false;
			int author = user.getId();
			String description = request.getParameter("description");
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
			int resolver = user.getId();
			int id = Integer.parseInt(request.getParameter("id"));
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

	public static void processDeny(HttpServletRequest request, HttpServletResponse response) {
		// TODO: Re-evaluate when front-end is ready-er
		response.setContentType("application/json");
		try (PrintWriter out = response.getWriter()) {
			HttpSession session = request.getSession();
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
			int id = Integer.parseInt(request.getParameter("id"));
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
