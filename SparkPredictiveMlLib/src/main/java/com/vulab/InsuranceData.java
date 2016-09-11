package com.vulab;

import java.io.Serializable;

public class InsuranceData implements Serializable {

	private Double creditability;
	private Double balance;
	private Double duration;
	private Double history;
	private Double purpose;
	private Double amount;
	private Double savings;
	private Double employment;
	private Double instPercent;
	private Double sexMarried;
	private Double guarantors;
	private Double residenceDuration;
	private Double assets;
	private Double age;
	private Double concCredit;
	private Double apartment;
	private Double credits;
	private Double occupation;
	private Double dependents;
	private Double hasPhone;
	private Double foreign;
	
	public InsuranceData(){
		
	}
	
	
	public InsuranceData(Double creditability, Double balance, Double duration, Double history, Double purpose,
			Double amount, Double savings, Double employment, Double instPercent, Double sexMarried, Double guarantors,
			Double residenceDuration, Double assets, Double age, Double concCredit, Double apartment, Double credits,
			Double occupation, Double dependents, Double hasPhone, Double foreign) {
		super();
		this.creditability = creditability;
		this.balance = balance;
		this.duration = duration;
		this.history = history;
		this.purpose = purpose;
		this.amount = amount;
		this.savings = savings;
		this.employment = employment;
		this.instPercent = instPercent;
		this.sexMarried = sexMarried;
		this.guarantors = guarantors;
		this.residenceDuration = residenceDuration;
		this.assets = assets;
		this.age = age;
		this.concCredit = concCredit;
		this.apartment = apartment;
		this.credits = credits;
		this.occupation = occupation;
		this.dependents = dependents;
		this.hasPhone = hasPhone;
		this.foreign = foreign;
	}
	
	
	public Double getCreditability() {
		return creditability;
	}
	public void setCreditability(Double creditability) {
		this.creditability = creditability;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getDuration() {
		return duration;
	}
	public void setDuration(Double duration) {
		this.duration = duration;
	}
	public Double getHistory() {
		return history;
	}
	public void setHistory(Double history) {
		this.history = history;
	}
	public Double getPurpose() {
		return purpose;
	}
	public void setPurpose(Double purpose) {
		this.purpose = purpose;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getSavings() {
		return savings;
	}
	public void setSavings(Double savings) {
		this.savings = savings;
	}
	public Double getEmployment() {
		return employment;
	}
	public void setEmployment(Double employment) {
		this.employment = employment;
	}
	public Double getInstPercent() {
		return instPercent;
	}
	public void setInstPercent(Double instPercent) {
		this.instPercent = instPercent;
	}
	public Double getSexMarried() {
		return sexMarried;
	}
	public void setSexMarried(Double sexMarried) {
		this.sexMarried = sexMarried;
	}
	public Double getGuarantors() {
		return guarantors;
	}
	public void setGuarantors(Double guarantors) {
		this.guarantors = guarantors;
	}
	public Double getResidenceDuration() {
		return residenceDuration;
	}
	public void setResidenceDuration(Double residenceDuration) {
		this.residenceDuration = residenceDuration;
	}
	public Double getAssets() {
		return assets;
	}
	public void setAssets(Double assets) {
		this.assets = assets;
	}
	public Double getAge() {
		return age;
	}
	public void setAge(Double age) {
		this.age = age;
	}
	public Double getConcCredit() {
		return concCredit;
	}
	public void setConcCredit(Double concCredit) {
		this.concCredit = concCredit;
	}
	public Double getApartment() {
		return apartment;
	}
	public void setApartment(Double apartment) {
		this.apartment = apartment;
	}
	public Double getCredits() {
		return credits;
	}
	public void setCredits(Double credits) {
		this.credits = credits;
	}
	public Double getOccupation() {
		return occupation;
	}
	public void setOccupation(Double occupation) {
		this.occupation = occupation;
	}
	public Double getDependents() {
		return dependents;
	}
	public void setDependents(Double dependents) {
		this.dependents = dependents;
	}
	public Double getHasPhone() {
		return hasPhone;
	}
	public void setHasPhone(Double hasPhone) {
		this.hasPhone = hasPhone;
	}
	public Double getForeign() {
		return foreign;
	}
	public void setForeign(Double foreign) {
		this.foreign = foreign;
	}

}
