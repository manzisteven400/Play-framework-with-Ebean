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
 *Maping academic programs with institutions 
 */
@Entity
@Table(name = "inst_academic")
public class InstAcademic extends Model {

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
	
	@Column(name="status")
	public String status;
	
	public static Finder<Long, InstAcademic> find = new Finder<Long, InstAcademic>(Long.class, InstAcademic.class);


}
