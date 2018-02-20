package com.bkt.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.ebean.Model;

/**
 *map available faculty with academic program
 */
@Entity
@Table(name = "faculty_academic")
public class FacultyAcademic extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "academic_program_id", referencedColumnName = "id")
	public AcademicProgram academicProgram;
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultId;
	
	public static Finder<Long, FacultyAcademic> find = new Finder<Long, FacultyAcademic>(Long.class, FacultyAcademic.class);
}
