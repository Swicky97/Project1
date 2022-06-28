package com.revature.dao;

import java.util.List;

import com.revature.models.Reimbursement;

public interface IReimbursementDao {
	public int insert(Reimbursement r);
	public List<Reimbursement> findAll();
	public boolean delete(int id);
	public boolean update(Reimbursement r);
}
