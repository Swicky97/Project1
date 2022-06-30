package com.revature.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.revature.models.Employee;
import com.revature.models.Reimbursement;
import com.revature.util.HibernateUtil;

public class ReimbursementDao implements IReimbursementDao {

	public int insert (Reimbursement r) {
		Session ses = HibernateUtil.getSession();
		Transaction tx = ses.beginTransaction();
		int pk = (int) ses.save(r);
		tx.commit();
		return pk;
	}
	
	public List<Reimbursement> findAll() {
		
		Session ses = HibernateUtil.getSession();
		
		List<Reimbursement> rem = ses.createQuery("FROM Reimbursement", Reimbursement.class).list();
		
		return rem;
	}
	
	public boolean update(Reimbursement r) {
		int id = r.getId();
		
		int result = 0;
		
		Session ses = HibernateUtil.getSession();
		
		Transaction t = ses.beginTransaction();
		
		Reimbursement rem = ses.get(Reimbursement.class, id);
		
		if(rem != null) {
			String hql = "UPDATE Reimbursements set reimb_amount = reimbAmount, reimb_amount = reimbSubmitted, reimb_description = reimbDescription, reimb_resolved = reimbResolved, reimb_author = reimbAuthor, reimb_resolver = reimbResolver, WHERE id = remId";
			Query query = ses.createQuery(hql);
			query.setParameter("reimb_amount", r.getReimbAmount());
			query.setParameter("reimb_submitted", r.getReimbSubmitted());
			query.setParameter("reimb_resolved", r.getReimbResolved());
			query.setParameter("reimb_description", r.getReimbDescription());
			query.setParameter("reimb_author", r.getReimbAuthor());
			query.setParameter("reimb_resolver", r.getReimbResolver());
			result = query.executeUpdate();
		}
		t.commit();
		if(result >= 1) {
			return true;
		}else {
		
		return false;
	}
}
	public boolean delete(int id) {
		int result = 0;
		
		Session ses = HibernateUtil.getSession();
		
		Transaction t = ses.beginTransaction();
		
		Reimbursement rem = ses.get(Reimbursement.class, id);
		if(rem != null) {
			String hql = "DELETE FROM reimbursements WHERE id = remId";
			Query query =ses.createQuery(hql);
			query.setParameter("remId", id);
			result = query.executeUpdate();
		}
		t.commit();
		if(result >= 1) {
			return true;
		}else {
		return false;
	}
  }
}
