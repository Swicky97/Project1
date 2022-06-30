package com.revature.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.revature.dao.IReimbursementDao;
import com.revature.dao.ReimbursementDao;
import com.revature.models.Reimbursement;

public class ReimbursementService {
	// TODO: ensure classes/methods match up with what we're actually working with
	
	private IReimbursementDao rdao;
	
	public ReimbursementService(ReimbursementDao rdao) {
		this.rdao = rdao;
	}
	
	public int add(Reimbursement r) {
		return rdao.insert(r);
	}
	
	/**
	 * Removes a reimbursement with the given id
	 * @param id
	 * @return
	 */
	public boolean remove(int id) {
		return rdao.delete(id);
	}
	
	/**
	 * Approves a reimbursement for the resolver
	 * @param r
	 * @param resolver
	 * @return
	 */
	public Reimbursement approve(Reimbursement r, int resolver) {
		if(r.isReimbApproved()) return r; // already approved
		r.setReimbApproved(true);
		r.setReimbResolver(resolver);
		r.setReimbResolved(Timestamp.from(Instant.now())); // Uncomment for actual use
		if(!rdao.update(r)) {
			r.setReimbApproved(false); // Reset reimbursement's status
			r.setReimbResolved(-1L);
			r.setReimbResolver(-1);
		}
		return r;
	}
	
	/**
	 * Denies a reimbursement for the resolver
	 * @param r
	 * @param resolver
	 * @return
	 */
	public Reimbursement deny(Reimbursement r, int resolver) {
		if(r.isReimbApproved()) return r; // can't renege
		r.setReimbResolved(Timestamp.from(Instant.now())); // Uncomment for actual use
		r.setReimbResolver(resolver);
		if(!rdao.update(r)) {
			r.setReimbResolved(-1); // Reset reimbursement's status
			r.setReimbResolver(-1);
		}
		return r;
	}
	
	public List<Reimbursement> getAll() {
		return rdao.findAll();
	}

	public List<Reimbursement> getAuthoredBy(int authorId) {
		List<Reimbursement> result = rdao.findAll().stream()
				.filter(r -> r.getReimbAuthor() == authorId)
				.toList();
		return result;
	}
}
