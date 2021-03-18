package edu.lwtech.csd297.teachersfirst.pojos;

import java.sql.Timestamp;
import java.util.Calendar;

import edu.lwtech.csd297.teachersfirst.DateHelpers;

public class Transaction {

	private int recID;
	private Timestamp transactionDate;
	private int operatorID;
	private String transactionMessage;

	// ----------------------------------------------------------------	

	public Transaction(int recID, Timestamp transactionDate, int  operatorID, String transactionMessage){

		if (recID < -1) throw new IllegalArgumentException("Invalid argument: recID < -1");
		if (transactionDate == null) throw new IllegalArgumentException("Invalid argument: transactionDate is null");
		if (operatorID < -1) throw new IllegalArgumentException("Invalid argument: operatorID < -1");
		if (transactionMessage == null) throw new IllegalArgumentException("Invalid argument: transactionMessage is null");
		if (transactionMessage.isEmpty()) throw new IllegalArgumentException("Invalid argument: transactionMessage is empty");

		this.recID = recID;
		this.transactionDate = transactionDate;
		this.operatorID = operatorID;
		this.transactionMessage = transactionMessage;
	}

	// ----------------------------------------------------------------	

	public int getRecID() {
		return this.recID;
	}

	public void setRecID(int recID) {
		if (recID <= 0)
			throw new IllegalArgumentException("setRecID: recID cannot be negative.");
		if (this.recID != -1)
			throw new IllegalArgumentException("setRecID: Object has already been added to the database (recID != 1).");

		this.recID = recID;
	}

	// ----------------------------------------------------------------	


	public Timestamp getTransactionDate() {
		return this.transactionDate;
	}

	public int getOperatorID() {
		return this.operatorID;
	}
	
	public String getTransactionMessage() {
		return this.transactionMessage;
	}


	// ----------------------------------------------------------------

	@Override
	public String toString() {
		return "Transaction/" + this.operatorID + "@" + this.transactionDate.toString() + "-" + this.transactionMessage;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; // can't be same as null
		if (obj == this) return true; // same as self is automatically true
		if (!(obj instanceof Transaction)) return false; // must be same type of object

		Transaction other = (Transaction) obj; // cast to compare fields
		if (this.recID != other.recID) return false;
		if (this.operatorID != other.operatorID) return false;
		if (!this.transactionDate.equals(other.transactionDate)) return false;
		if (!this.transactionMessage.equals(other.transactionMessage)) return false;

		// no failures, good match
		return true;
	}

}
