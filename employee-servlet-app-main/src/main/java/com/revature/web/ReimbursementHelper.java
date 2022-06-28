package com.revature.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.ReimbursementDao;
import com.revature.models.Employee;
import com.revature.models.Reimbursement;
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
		// TODO Auto-generated method stub
		
		PrintWriter out;
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			out.print("{\"message\": \"Approving...\"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void processDeny(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		PrintWriter out;
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			out.print("{\"message\": \"Denying...\"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void processDelete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
