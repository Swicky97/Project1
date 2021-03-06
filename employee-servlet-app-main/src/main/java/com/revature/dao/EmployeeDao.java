package com.revature.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.revature.models.Employee;
import com.revature.util.HibernateUtil;

// servlet -> calls service --> calls dao
public class EmployeeDao {
	
	// CRUD methods
	
	// Create (think that in the service layer we'll have a REGISTER()
	public int insert(Employee e) {
		
		// grab the session object
		Session ses = HibernateUtil.getSession();
		
		// begin a tx
		Transaction tx = ses.beginTransaction();
		
		// capture the pk returned when the session method save() is called
		int pk = (int) ses.save(e);
		
		tx.commit();
		// return the pk
		return pk;
		
	}
	
	// Read
	public Employee findById(int id) {
		Employee e = new Employee();
		// grab the session
		Session ses = HibernateUtil.getSession();
		
		// begin a tx
		Transaction tx = ses.beginTransaction();
		
		Employee emp = ses.get(Employee.class, id);
		if (emp != null) {
			String hql = "SELECT * FROM Employee WHERE id = :empId";
			Query query = ses.createQuery(hql);
			query.setParameter("empId", id);
			query.setParameter("firstname", e.getFirstName());
			query.setParameter("lastname", e.getLastName());
			query.setParameter("password", e.getPassword());
			query.setParameter("username", e.getFirstName());
			query.executeUpdate();
		}
		tx.commit();
		return e;
	}
	
	public List<Employee> findAll() {
		
		// grab the session
		Session ses = HibernateUtil.getSession();
		
		// make an HQL -- Hibernate Query Language: odd mix of OOP & native SQL
		 List<Employee> emps = ses.createQuery("from Employee", Employee.class).list();
		
		// return the list of employees
		return emps;
		
	}
	
	public boolean delete(int id) {
		 
		int result = 0;
		// grab the session
		Session ses = HibernateUtil.getSession();
		
		// begin a tx
		Transaction tx = ses.beginTransaction();
		
		Employee emp = ses.get(Employee.class, id);
		if (emp != null) {
			String hql = "DELETE FROM Employee WHERE id = :empId";
			Query query = ses.createQuery(hql);
			query.setParameter("empId", id);
			result = query.executeUpdate();
		}
		tx.commit();
		if(result >= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	//not working
	public boolean update(Employee e) {
		int id = e.getId();
		int result = 0;
		// grab the session
		Session ses = HibernateUtil.getSession();
		
		// begin a tx
		Transaction tx = ses.beginTransaction();
		
		Employee emp = ses.get(Employee.class, id);
		if (emp != null) {
			String hql = "UPDATE Employee e SET e.firstName = :firstname, e.lastName = :lastname, e.password = :password, e.username = :username WHERE e.id = :empId";
			Query query = ses.createQuery(hql);
			query.setParameter("empId", id);
			query.setParameter("firstname", e.getFirstName());
			query.setParameter("lastname", e.getLastName());
			query.setParameter("password", e.getPassword());
			query.setParameter("username", e.getUsername());
			result = query.executeUpdate();
		}
		tx.commit();
		if(result >= 1) {
			return true;
		} else {
			return false;
		}
	}
	

}
