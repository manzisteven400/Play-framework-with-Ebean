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
 *Available degree programs in the institution 
 */
@Entity
@Table(name = "degree_program")
public class DegreeProgram extends Model {

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
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultyId;
	
	@Column(name="degree_name")
	public String degreeName;
	
	@Column(name="degree_accronym")
	public String degreeAccronym;
	

	@Column(name="status")
	public String status;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Student> student;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<DegreeStudyProgram> degreeStudyProgram;

	
	public static Finder<Long, DegreeProgram> find = new Finder<Long, DegreeProgram>(Long.class, DegreeProgram.class);
	
}
