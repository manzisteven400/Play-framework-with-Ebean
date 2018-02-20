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
* The persistent class for the ussd_temp_log database table.
* 
*/
@Entity
@Table(name = "ussd_temp_log")
public class USSDTempLog extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue
	public Long id;

	/**
	 * External Transaction id for payment done via mobile money platforms
	 * 
	 * 
	 */
	@Column(name = "momo_trx")
	public String momoTrx;
	
	/**
	 * upiNumber to identify the land from national land center
	 * 
	 * 
	 */
	@Column(name = "upi_number")
	public String upiNumber;
	
	/**
	 * Registration Number for user being registered
	 * 
	 * 
	 */
	@Column(name = "registeration_number")
	public String registerationNumber;
	
	/**
	 * Internal Transaction id for payment done via mobile money platforms
	 * 
	 * 
	 */
	@Column(name = "processing_number")
	public String processNumber;
	
	/**
	 * Status of the payment done via mobile money: Pending, Posted, timed out or rejected
	 * 
	 * 
	 */
	@Column(name = "momo_status")
	public String momoStatus;
	
	/**
	 * Phone number of the user browsing the system
	 * 
	 * 
	 */
	@Column(name = "msisdn")
	public String msisdn;
	
	/**
	 * Level to track which menu user is browsing and control user's navigation
	 * through out the system
	 * 
	 */
	@Column(name = "level")
	public int level;

	
	@ManyToOne
	@JoinColumn(name = "payment_log_id", referencedColumnName = "id")
	public PaymentLog paymentLog;
	
	@ManyToOne
	@JoinColumn(name = "payment_purpose_id", referencedColumnName = "id")
	public PaymentPurpose paymentPurpose;

	@ManyToOne
	@JoinColumn(name = "student_id", referencedColumnName = "id")
	public Student student;

	@Column(name = "reg_number")
	public String studentRegNumber;
	
	/**
	 * National Identity number as provided by the NIDA
	 * to uniquely identify the individual person in the entire country
	 * 
	 */
	@Column(name = "nid")
	public String nid;
	/**
	 * First name of the person doing registration on the USSD
	 * 
	 * 
	 */
	@Column(name = "first_name")
	public String firstName;
	
	/**
	 * Last name of the person doing registration on the USSD
	 * 
	 * 
	 */
	@Column(name = "last_name")
	public String lastName;
	/**
	 * Sex of the person doing registration on the USSD: M for male and F for female
	 * 
	 * 
	 */
	@Column(name = "sex")
	public String sex;
	
	/**
	 * Date pf birth of the person doing registration on the USSD
	 * 
	 * 
	 */
	@Column(name = "dob")
	public String dob;
	
	/**
	 * The province object helps to capture and temporarily save land's location
	 * as from National land center
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	public Institution institution;
	
	/**
	 * The District object helps to capture and temporarily save land's location
	 * as from National land center
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "faculty_id", referencedColumnName = "id")
	public Faculty faculty;

	
	/**
	 * Flag for the user being registered:1 if user is doing registration else 0 for users browsing the authenticated menu
	 * 
	 * 
	 */
	@Column(name = "register")
	public int register;
	
	/**
	 * Flag for the user exiting the menu: logging out: 1 if user signs out else 0
	 * 
	 * 
	 */
	@Column(name = "exit_menu")
	public int exitMenu;
	
	/**
	 * user entered text messages
	 * 
	 * 
	 */
	@Column(name = "text_message")
	public String textMessage;
	
	/**
	 * this is the user id
	 * 
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "user_account_id", referencedColumnName = "id")
	public UserAccount userAccounts;
	
	@ManyToOne
	@JoinColumn(name = "bank_account_id", referencedColumnName = "id")
	public BankAccount bankAccount;
	
	@ManyToOne
	@JoinColumn(name = "user_profile_id", referencedColumnName = "id")
	public UserProfile userProfile;
	
	@ManyToOne
	@JoinColumn(name = "topic_id", referencedColumnName = "id")
	public Topic topic;
	
	
	/**
	 * this is status for the request:1 closed else 0
	 * 
	 * 
	 */
	@Column(name = "status")
	public int status;
	
	/**
	 * date of the current request
	 * 
	 * 
	 */
	@Column(name = "date_time")
	public String dateTime;
	/**
	 * Language the current user has chosen
	 * 
	 * 
	 */
	@Column(name = "language_id")
	public String language;
	
	@Column(name = "first_menu")
	public int menuFirst;
	
	@Column(name = "second_menu")
	public int secondMenu;
	
	@Column(name = "third_menu")
	public int thirdMenu;
	
	@Column(name = "fourth_menu")
	public int fourthMenu;
	
	@Column(name = "fifth_menu")
	public int fifthMenu;
	
	@Column(name = "amount")
	public int amount;
	
	
	@Column(name = "sixth_menu")
	public int sixthMenu;
	
	@Column(name = "institution_list")
	public String institutionList;
	
	@Column(name = "purpose_list")
	public String purposeAccountList;
	
	@Column(name = "cell_list")
	public String cellList;
	
	@Column(name = "village_list")
	public String villageList;
	
	/**
	 * Manages insert,update.delete and select operations in database
	 * 
	 */
	public static Finder<Long, USSDTempLog> find = new Finder<Long, USSDTempLog>(Long.class, USSDTempLog.class);

	
}
