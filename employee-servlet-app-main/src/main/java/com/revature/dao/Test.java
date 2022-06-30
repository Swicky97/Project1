package com.revature.dao;

import com.revature.models.Employee;
import com.revature.models.Role;

public class Test {

	public static void main(String[] args) {
		EmployeeDao edao = new EmployeeDao();
		Employee e = new Employee(1, "girl" , "one", "password" , "boy1", Role.Employee);
		boolean b = edao.delete(2);
		System.out.println(b);
	}

}
