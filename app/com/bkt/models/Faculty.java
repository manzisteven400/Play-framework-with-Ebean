/**
 * 
 */
package com.bkt.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * @author pc
 *
 */
@Entity
@Table(name = "faculty")
public class Faculty extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long id;

	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution instId;

	@Required
	public String name;

	public String accronym;

	@Required
	public String code;

	@OneToMany(cascade=CascadeType.ALL)
	public List<Student> student;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<StudentPayLog> studentPayLog;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<PaymentLog> paymentLog;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<DegreeProgram> degreeProgram;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<FacultyAcademic> facultyAcademic;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<NoneDegreeProgram> noneDegreeProgram;

	@OneToMany(cascade = CascadeType.ALL)
	public List<TuitionFees> tuitionFees;

	public static Finder<Long, Faculty> find = new Finder<Long, Faculty>(Long.class, Faculty.class);

}
