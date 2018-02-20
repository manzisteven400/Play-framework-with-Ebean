/**
 * 
 */
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

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * @author pc
 *
 */
@Entity
@Table(name = "bank_account")
public class BankAccount extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long id;
	

	@ManyToOne
	@JoinColumn(name = "bank_id", referencedColumnName = "id")
	public Bank bankId;
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultyId;

	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution instId;

	@Required
	@Column(unique = true)
	public String accountNumber;

	@Required
	public String accountStatus;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<PaymentPurpose> paymentPurpose;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<PaymentLog> paymentLog;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;

	public static Finder<Long, BankAccount> find = new Finder<Long, BankAccount>(Long.class, BankAccount.class);
}
