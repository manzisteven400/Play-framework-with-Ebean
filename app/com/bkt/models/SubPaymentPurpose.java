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
@Table(name = "sub_payment_purpose")
public class SubPaymentPurpose extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "bank_account_id", referencedColumnName = "id")
	public BankAccount accountId;
	
	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution instId;
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultyId;
	
	@Required
	public String purpose;
	
	public String description;

	@Required
	public String accPriority;
	
	@ManyToOne
	@JoinColumn(name = "payment_purpose", referencedColumnName = "id")
	public PaymentPurpose paymentPurpose;
	
	public Integer hasDependent;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<PaymentLog> paymentLog;
	
	/**
	 * bi-directional one-to-many association to USSDTempLog
	 * 
	 */
	@OneToMany(cascade=CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;
	
	public static Finder<Long, SubPaymentPurpose> find=new Finder<Long,SubPaymentPurpose>(Long.class, SubPaymentPurpose.class);
	
}
