package com.bkt.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;

/**
 *map available degree program types/mode of study i.e Day, evening,... 
 */
@Entity
@Table(name = "study_program_type")
public class StudyProgramType extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Column(name="type_name")
	public String typeName;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<DegreeStudyProgram> degreeStudyProgram;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Student> student;
	
	public static Finder<Long, StudyProgramType> find = new Finder<Long, StudyProgramType>(Long.class, StudyProgramType.class);
}
