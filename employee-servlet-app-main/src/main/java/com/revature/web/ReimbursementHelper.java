package com.revature.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.service.ReimbursementService;

public class ReimbursementHelper {

	private static ReimbursementService rserv = new ReimbursementService(/* TODO: Pass in rdao */);
	// object mapper (for frontend)
	private static ObjectMapper om = new ObjectMapper();
	
	/**
	 * Return a list of all reimbursements
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public static void processReimbursements(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		
		// 2. Call the getAll() method from the employee service
		List<Reimbursement> reimbursements = rserv.getAll();
		
		// 3. transform the list to a string
		String jsonString = om.writeValueAsString(reimbursements);
		
		// get print writer, then write it out
		PrintWriter out = response.getWriter();
		out.write(jsonString); // write the string to the response body	
	}
}
