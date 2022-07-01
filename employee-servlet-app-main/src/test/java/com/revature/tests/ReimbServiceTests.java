package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.dao.ReimbursementDao;
import com.revature.models.Reimbursement;
import com.revature.service.ReimbursementService;

public class ReimbServiceTests {
	
	/**
	 * TODO: This file was built while we were using long in place of 
	 * timestamps for persistence. Will not function as-is
	 */
	
	private ReimbursementService rserv;
	private ReimbursementDao mockdao;
	
	@Before
	public void setup() {
		mockdao = mock(ReimbursementDao.class);
		rserv = new ReimbursementService(mockdao);
	}
	
	@After
	public void teardown() {
		mockdao = null;
		rserv = null;
	}
	
	@Test
	public void testApproval_success() {
		Reimbursement r = new Reimbursement(1, 1000, 1L, -1L, false, "haha", 1, -1);
		when(mockdao.update(r)).thenReturn(true);
		Reimbursement actual = rserv.approve(r, 2);
		Reimbursement expected = new Reimbursement(1, 1000, 1L, -1L, true, "haha", 1, 2);
		assertEquals(expected, actual); // Expected to "fail" when actually using timestamps
	}
	
	@Test
	public void testApproval_success_alreadyApproved() {
		Reimbursement r = new Reimbursement(1, 1000, 1L, 1L, true, "haha", 1, 2);
		Reimbursement actual = rserv.approve(r, 2);
		Reimbursement expected = r;
		assertEquals(expected, actual);
	}
	
	@Test
	public void testApproval_fail_updateFailed() {
		Reimbursement r = new Reimbursement(1, 1000, 1L, -1L, false, "haha", 1, -1);
		when(mockdao.update(r)).thenReturn(false);
		Reimbursement actual = rserv.approve(r, 2);
		Reimbursement expected = r;
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDenial_success() {
		Reimbursement r = new Reimbursement(1, 1000, 1L, -1L, false, "haha", 1, -1);
		when(mockdao.update(r)).thenReturn(true);
		Reimbursement actual = rserv.deny(r, 2);
		Reimbursement expected = new Reimbursement(1, 1000, 1L, -1L, false, "haha", 1, 2);
		assertEquals(expected, actual); // Expected to "fail" when actually using timestamps
	}
	
	@Test
	public void testDenial_fail_alreadyApproved() {
		Reimbursement r = new Reimbursement(1, 1000, 1L, 1L, true, "haha", 1, 2);
		Reimbursement actual = rserv.deny(r, 2);
		Reimbursement expected = r;
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDenial_fail_updateFailed() {
		Reimbursement r = new Reimbursement(1, 1000, 1L, -1L, false, "haha", 1, -1);
		when(mockdao.update(r)).thenReturn(false);
		Reimbursement actual = rserv.deny(r, 2);
		Reimbursement expected = r;
		assertEquals(expected, actual);
	}
}
