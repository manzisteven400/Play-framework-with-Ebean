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
@Table(name = "payment_log")
public class PaymentLog extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "student_id", referencedColumnName = "id")
	public Student studentId;

	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution instId;
	
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty facultId;
	
	@ManyToOne
	@JoinColumn(name = "bank_id", referencedColumnName = "id")
	public Bank bank;
	
	@ManyToOne
	@JoinColumn(name = "bank_account_id", referencedColumnName = "id")
	public BankAccount bankAcc;
	
	@ManyToOne
	@JoinColumn(name = "payment_purpose_id", referencedColumnName = "id")
	public PaymentPurpose paymentPurpose;
	
	@ManyToOne
	@JoinColumn(name = "sub_purpose_id", referencedColumnName = "id")
	public SubPaymentPurpose subPaymentPurpose;
	
	@ManyToOne
	@JoinColumn(name = "institution_calender_id", referencedColumnName = "id")
	public InstitutionCalender academicYear;
	
	@Required
	@Column(name="payer_name")
	public String payerName;

	@Column(name="msisdn")
	public String msisdn;
	
	@Column(name="student_names")
	public String studentNames;

	@Column(name="nida")
	public String nida;

	@Column(name="is_registered")
	public String isRegistered;

	@Required
	public String extTrxId;

	@Required
	@Column(name="bank_slip")
	public String bankSlip;

	@Required
	@Column(name="payment_channel")
	public String paymentChannel;//momo,bank,tigocash,airtelmoney

	@Column(name="operator")
	public String operator;
	
	@Column(name="status_desc")
	public String statusDesc;
	
	@Column(name="ussd_status")
	public String ussdStatus;
	
	@Column(name="payment_year")
	public int paymentYear;
	
	@Column(name="payment_device")
	public String paymentDevice;
	
	@Column(name="processing_number")
	public String processingNumber;

	@Required
	@Column(name="amount_paid")
	public double amountPaid;

	@Column(name="payment_date")
	public Date paymentDate;

	@Column(name="posting_date")
	public Date postingDate;

	@Column(name="student_ref")
	public String studentRef;
	
	@Column(name="logged")
	public int logged;
	
	@Column(name="batch_check_sum")
	public String batchCheckSum;
	
	@Column(name="trx_check_sum")
	public String trxCheckSum;
	
	@Column(name="log_bank_account")
	public String logBankAccount;
	/**
	 * bi-directional one-to-many association to USSDTempLog
	 * 
	 */
	@OneToMany(cascade = CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;

	public static Finder<Long, PaymentLog> find = new Finder<Long, PaymentLog>(Long.class, PaymentLog.class);

}
