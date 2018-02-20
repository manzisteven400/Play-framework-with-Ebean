package com.bkt.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.ebean.Model;

/**
 * @author pc
 *
 */

@Entity
@Table(name = "student_pay_log")
public class StudentPayLog extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "student_id", referencedColumnName = "id")
	public Student studentId;

	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution instId;
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultId;
	
	@ManyToOne
	@JoinColumn(name = "tuition_fees_id", referencedColumnName = "id")
	public TuitionFees tuitionFee;
		
	@Column(name="status_desc")
	public String statusDesc;
	
	@Column(name="amount_paid")
	public double amountPaid;

	@Column(name="amount_expected")
	public double amountExpected;

	@Column(name="amount_prev")
	public double amountPrev;

	
	public static Finder<Long, StudentPayLog> find = new Finder<Long, StudentPayLog>(Long.class, StudentPayLog.class);

}