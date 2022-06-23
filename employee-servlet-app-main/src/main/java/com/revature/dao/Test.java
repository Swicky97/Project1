package com.revature.dao;

import com.revature.models.Employee;

public class Test {

	public static void main(String[] args) {
		EmployeeDao edao = new EmployeeDao();
		Employee e = new Employee(2, "girl" , "one", "password" , "boy1");
		boolean b = edao.update(e);
		System.out.println(b);
	}

}
