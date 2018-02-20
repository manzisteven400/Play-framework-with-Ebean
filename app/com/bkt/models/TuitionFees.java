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
 *Setting institution tuition fees. 
 */
@Entity
@Table(name = "tuition_fees")
public class TuitionFees extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "institution_calender_id", referencedColumnName = "id")
	public InstitutionCalender instCalendar;
	
	@ManyToOne
	@JoinColumn(name = "degree_study_program_id", referencedColumnName = "id")
	public DegreeStudyProgram degreeStudyProgramId;
	
	@ManyToOne
	@JoinColumn(name = "none_degree_study_program_id", referencedColumnName = "id")
	public NoneDegreeStudyProgram noneDegreeStudyProgramId;
	
	
	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution institutionId;
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultyId;
	
	@Column(name="amount")
	public double amount;
	
	@Column(name="status")
	public String status;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<StudentPayLog> studentPayLog;
	
	public static Finder<Long, TuitionFees> find = new Finder<Long, TuitionFees>(Long.class, TuitionFees.class);
}
