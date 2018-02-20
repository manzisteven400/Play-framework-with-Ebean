/**
 * 
 */
package com.bkt.models;

import java.util.Date;
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
@Table(name = "institution")
public class Institution extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	public Long id;

	@Required
	@Column(unique = true)
	public String name;
	
	public String accronym;
	public String instLogo;
	public String address;
	public String studentPayTransactionFees;

	@Column(unique = true)
	public String phone;

	@Column(unique = true)
	public String email;

	@Column(unique = true)
	public String tin;
	
	public Date createdDate;

	@Column(unique = true)
	public String instCode;

	public String thirdParty;

	@OneToMany(cascade=CascadeType.ALL)
	public List<BankAccount> bankAccount;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Faculty> faculty;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<StudentPayLog> studentPayLog;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Student> student;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<UserAccount> userAccount;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<PaymentPurpose> paymentPurpose;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<PaymentLog> paymentLog;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<InstAcademic> instAcademic;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<NoneDegreeProgram> noneDgree;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<DegreeProgram> degreeProgram;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<InstitutionCalender> instCalander;

	@OneToMany(cascade = CascadeType.ALL)
	public List<TuitionFees> tuitionFees;

	public static Finder<Long, Institution> find = new Finder<Long, Institution>(Long.class, Institution.class);

}
