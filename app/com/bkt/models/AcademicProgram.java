package com.bkt.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * @author pc
 *
 */
@Entity
@Table(name = "academic_program")
public class AcademicProgram extends Model {
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Required
	@Column(unique=true)
    public String name;
	
	@Required
	@Column(unique=true)
    public String accronym;
	
	@Column(unique=true)
    public String code;
	
	@Column(name="status")
	public String status;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<NoneDegreeProgram> noneDgree;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<InstAcademic> instAcademic;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<DegreeProgram> degreeProgram;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<FacultyAcademic> facultyAcademic;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Student> student;
	
	public static Finder<Long, AcademicProgram> find= new Finder<Long, AcademicProgram>(Long.class,AcademicProgram.class);
}
