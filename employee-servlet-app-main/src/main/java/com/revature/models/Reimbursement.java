package com.revature.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is a persistent class meaning we need to provide a no-args constructor
 * a PK, getters & setters, hashCode & equals
 */

@Entity
@Table(name="reimbursements") // these annotations come from the JPA (that's the specification)
public class Reimbursement {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="reimb_amount")
	private double reimbAmount;
	
	@Column(name="reimb_submitted")
	private long reimbSubmitted;
	
	@Column(name="reimb_resolved")
	private long reimbResolved;
	
	@Column(name="reimb_approved")
	private boolean reimbApproved;
	
	@Column(name="reimb_description")
	private String reimbDescription;
	
	@Column(name="reimb_author")
	private int reimbAuthor;
	
	@Column(name="reimb_resolver")
	private int reimbResolver;

	public Reimbursement() {
		super();
	}

	public Reimbursement(double reimbAmount, long reimbSubmitted, long reimbResolved, boolean reimbApproved,
			String reimbDescription, int reimbAuthor, int reimbResolver) {
		super();
		this.reimbAmount = reimbAmount;
		this.reimbSubmitted = reimbSubmitted;
		this.reimbResolved = reimbResolved;
		this.reimbApproved = reimbApproved;
		this.reimbDescription = reimbDescription;
		this.reimbAuthor = reimbAuthor;
		this.reimbResolver = reimbResolver;
	}

	
	
	public Reimbursement(int id, double reimbAmount, long reimbSubmitted, long reimbResolved, boolean reimbApproved,
			String reimbDescription, int reimbAuthor, int reimbResolver) {
		super();
		this.id = id;
		this.reimbAmount = reimbAmount;
		this.reimbSubmitted = reimbSubmitted;
		this.reimbResolved = reimbResolved;
		this.reimbApproved = reimbApproved;
		this.reimbDescription = reimbDescription;
		this.reimbAuthor = reimbAuthor;
		this.reimbResolver = reimbResolver;
	}

	public boolean isReimbApproved() {
		return reimbApproved;
	}

	public void setReimbApproved(boolean reimbApproved) {
		this.reimbApproved = reimbApproved;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getReimbAmount() {
		return reimbAmount;
	}

	public void setReimbAmount(double reimbAmount) {
		this.reimbAmount = reimbAmount;
	}

	public long getReimbSubmitted() {
		return reimbSubmitted;
	}

	public void setReimbSubmitted(long reimbSubmitted) {
		this.reimbSubmitted = reimbSubmitted;
	}

	public long getReimbResolved() {
		return reimbResolved;
	}

	public void setReimbResolved(long reimbResolved) {
		this.reimbResolved = reimbResolved;
	}

	public String getReimbDescription() {
		return reimbDescription;
	}

	public void setReimbDescription(String reimbDescription) {
		this.reimbDescription = reimbDescription;
	}

	public int getReimbAuthor() {
		return reimbAuthor;
	}

	public void setReimbAuthor(int reimbAuthor) {
		this.reimbAuthor = reimbAuthor;
	}

	public int getReimbResolver() {
		return reimbResolver;
	}

	public void setReimbResolver(int reimbResolver) {
		this.reimbResolver = reimbResolver;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, reimbAmount, reimbAuthor, reimbDescription, reimbResolved, reimbResolver,
				reimbSubmitted);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		return id == other.id && Double.doubleToLongBits(reimbAmount) == Double.doubleToLongBits(other.reimbAmount)
				&& reimbAuthor == other.reimbAuthor && reimbDescription == other.reimbDescription
				&& reimbResolved == other.reimbResolved && reimbResolver == other.reimbResolver
				&& reimbSubmitted == other.reimbSubmitted;
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", reimbAmount=" + reimbAmount + ", reimbSubmitted=" + reimbSubmitted
				+ ", reimbResolved=" + reimbResolved + ", reimbDescription=" + reimbDescription + ", reimbAuthor="
				+ reimbAuthor + ", reimbResolver=" + reimbResolver + "]";
	}
	
	
	
	

}
