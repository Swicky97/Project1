package com.revature.service;

import java.util.List;
import java.util.Optional;

import com.revature.dao.EmployeeDao;
import com.revature.models.Employee;
import com.revature.models.Role;

public class EmployeeService {
	
	private EmployeeDao edao;
	
	/**
	 * Dependency Injection via Constructor Injection
	 * 
	 * Constructor Injection is a sophisticated way of ensuring 
	 * that the EmployeeService object ALWAYS has an EmployeeDao object
	 * 
	 */
	public EmployeeService(EmployeeDao edao) {
		this.edao = edao;
	}
	
	/**
	 * Verify login information is correct, send user to their dashboard
	 * @param username
	 * @param password
	 * @return Employee with matching credentials, else empty Employee object
	 */
	public Employee confirmLogin(String username, String password) {
		
		// TODO Optional: Hash passwords? Plain text password storage is cringe
		Optional<Employee> possibleEmp = edao.findAll().stream()
				.filter(e -> (e.getUsername().equals(username) && e.getPassword().equals(password)))
				.findFirst();
		// IF the employee is present, return it, otherwise return empty Emp object (with id of 0)
		return (possibleEmp.isPresent() ? possibleEmp.get() : new Employee());
		// (TODO?) ideally you should optimize this and set up a custom exception to be returned
	}
	
	/**
	 * Get a list of all registered employees
	 * @return List of Employee objects
	 */
	public List<Employee> getAll() {
		return edao.findAll();
	}
	
	/**
	 * Register a new employee and generate an ID for them
	 * @param e Employee to register
	 * @return int ID of the new employee
	 */
	public int register(Employee e) {
		return edao.insert(e);
	}
	
	public String viewAccountInfo(Employee e) {
		 Employee user = edao.findById(e.getId());
		 return user.toString();
	}

	public List<Employee> getManagers() {
		List<Employee> managers = edao.findAll().stream()
				.filter(e -> e.getRole().equals(Role.Manager))
				.toList();
		return managers;
	}
	
}
