package com.revature.service;

import java.util.List;

import com.revature.dao.IReimbursementDao;
import com.revature.models.Reimbursement;

public class ReimbursementService {
	// TODO: ensure classes/methods match up with what we're actually working with
	
	private IReimbursementDao rdao;
	
	public ReimbursementService() {
		// TODO: DELETE once rdao is in project
	}
	
	public ReimbursementService(ReimbursementDao rdao) {
		this.rdao = rdao;
	}
	
	public int add(Reimbursement r) {
		return rdao.insert(r);
	}
	
	public boolean approve(Reimbursement r) {
		
		if(rdao.update(r)) {
			return true;
		} else {
			// Reset reimbursement's status
			return false;
		}
	}
	
	public boolean deny(Reimbursement r) {
		
		if(rdao.update(r)) {
			return true;
		} else {
			// Reset reimbursement's status
			return false;
		}
	}
	
	public List<Reimbursement> getAll() {
		return rdao.findAll();
	}
}
