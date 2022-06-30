package com.revature.dao;

import com.revature.models.Employee;
import com.revature.models.Role;

public class Test {

	public static void main(String[] args) {
		Employee manager = new Employee("Cameron", "Seibel", "cmseibel", "password", Role.Manager);
		EmployeeDao edao = new EmployeeDao();
		edao.insert(manager);
	}

}
