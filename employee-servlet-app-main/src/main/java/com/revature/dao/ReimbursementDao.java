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
			String hql = "UPDATE Reimbursement set reimbAmount = :reimbAmount, reimbSubmitted = :reimbSubmitted, reimbDescription = :reimbDescription, reimbResolved = :reimbResolved, reimbAuthor = :reimbAuthor, reimbResolver = :reimbResolver WHERE id = :remId";
			Query query = ses.createQuery(hql);
			query.setParameter("remId", id);
			query.setParameter("reimbAmount", r.getReimbAmount());
			query.setParameter("reimbSubmitted", r.getReimbSubmitted());
			query.setParameter("reimbResolved", r.getReimbResolved());
			query.setParameter("reimbDescription", r.getReimbDescription());
			query.setParameter("reimbAuthor", r.getReimbAuthor());
			query.setParameter("reimbResolver", r.getReimbResolver());
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
			String hql = "DELETE FROM Reimbursement WHERE id = :remId";
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
