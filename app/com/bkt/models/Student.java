/**
 * 
 */
package com.bkt.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "student")
public class Student extends Model {

	/**
	 * Student table
	 */
	
	/**
	 * Student registration status
	 */
	public static enum StudentStatus {ACTIVE, DISABLED,APPLICANT,ADMITTED,OTHER}
	
	/**
	 * Account status: ONGOING,DONE
	 */
	public static enum ApplicantStatus {ONGOING,DONE}
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long id;
	
	@Required
	public String firstName;

	@Required
	public String lastName;

	@Required
	@Column(unique=true)
	public String regNumber;

	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution instId;
	
	@ManyToOne
	@JoinColumn(name = "academic_program_id", referencedColumnName = "id")
	public AcademicProgram academicProgram;
	
	@ManyToOne
	@JoinColumn(name = "degree_program_id", referencedColumnName = "id")
	public DegreeProgram degreeProgram;
	
	@ManyToOne
	@JoinColumn(name = "study_program_type_id", referencedColumnName = "id")
	public StudyProgramType studyProgram;
	
	@ManyToOne
	@JoinColumn(name = "institution_calender_id", referencedColumnName = "id")
	public InstitutionCalender academicYear;
	
	@ManyToOne
	@JoinColumn(name = "none_degree_program_id", referencedColumnName = "id")
	public NoneDegreeProgram noneDegreeProgram;
	
	@Required
	@Column(unique=true)
	public String nida;
	
	public String phone;
	public String dob;
	public String stdClass;
	public String sex;
	
	@Column(unique=true)
	public String email;
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultyId;

	@Required
	@Enumerated(EnumType.STRING)
	public StudentStatus stdStatus;

	@Required
	@Enumerated(EnumType.STRING)
	public ApplicantStatus applicantStatus;
	
	public String password;
	public String stdPic;
	
	/**
	 * bi-directional one-to-many association to USSDTempLog
	 * 
	 */
	@OneToMany(cascade=CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<StudentPayLog> studentPayLog;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<PaymentLog> paymentLog;

	public static Finder<Long, Student> find= new Finder<Long, Student>(Long.class,Student.class);

}
