package com.bkt.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;

/**
 *Institution calendar when the academic year will start,... 
 */
@Entity
@Table(name = "institution_calender")
public class InstitutionCalender extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Column(name="academic_year")
	public String academicYear;
	
	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution institution;
	
	@Column(name="start_date")
	public String startDate;
	
	@Column(name="end_date")
	public String endDate;
	
	@Column(name="batch_code")
	public String batchCode;
	
	@Column(name="status")
	public String status;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Student> student;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<PaymentLog> paymentLog;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<TuitionFees> tuitionFees;
	
	public static Finder<Long, InstitutionCalender> find = new Finder<Long, InstitutionCalender>(Long.class, InstitutionCalender.class);
}
