package com.revature.service;

import java.util.List;

public class ReimbursementService {
	// TODO: ensure classes/methods match up with what we're actually working with
	
	private ReimbursementDao rdao;
	
	public ReimbursementService(ReimbursementDao rdao) {
		this.rdao = rdao;
	}
	
	public int add(Reimbursement r) {
		return rdao.insert(r);
	}
	
	public boolean approve(Reimbursement r) {
		String tempStatus = r.getStatus();
		r.setStatus("Approved");
		if(rdao.update(r)) {
			return true;
		} else {
			// Reset reimbursement's status
			r.setStatus(tempStatus);
			return false;
		}
	}
	
	public boolean deny(Reimbursement r) {
		String tempStatus = r.getStatus();
		r.setStatus("Denied");
		if(rdao.update(r)) {
			return true;
		} else {
			// Reset reimbursement's status
			r.setStatus(tempStatus);
			return false;
		}
	}
	
	public List<Reimbursement> getAll() {
		return rdao.findAll();
	}
}
