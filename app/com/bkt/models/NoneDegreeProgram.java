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
 *Available none degree programs in the institution 
 */
@Entity
@Table(name = "none_degree_program")
public class NoneDegreeProgram extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "academic_program_id", referencedColumnName = "id")
	public AcademicProgram academicProgram;
	
	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution institution;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Student> student;
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultyId;
	
	
	@Column(name="degree_name")
	public String degreeName;
	
	@Column(name="degree_accronym")
	public String degreeAccronym;
	

	@Column(name="status")
	public String status;

	
	public static Finder<Long, NoneDegreeProgram> find = new Finder<Long, NoneDegreeProgram>(Long.class, NoneDegreeProgram.class);
}
