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
@Table(name = "degree_study_program")
public class DegreeStudyProgram extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "degree_program_id", referencedColumnName = "id")
	public DegreeProgram degreeProgram;
	
	@ManyToOne
	@JoinColumn(name = "study_program_type_id", referencedColumnName = "id")
	public StudyProgramType studyProgramId;
	
	
	@Column(name="program_study_duration")
	public String programStudyDuration;
	
	@Column(name="status")
	public String status;
	

	@OneToMany(cascade = CascadeType.ALL)
	public List<TuitionFees> tuitionFees;
	
	public static Finder<Long, DegreeStudyProgram> find = new Finder<Long, DegreeStudyProgram>(Long.class, DegreeStudyProgram.class);
}
