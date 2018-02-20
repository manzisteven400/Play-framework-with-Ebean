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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * @author pc
 *
 */
@Entity
@Table(name = "bank")
public class Bank extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long id;
	
	@Required
	@Column(unique=true)
    public String name;
	
	@Required
	@Column(unique=true)
    public String accronym;
	
	@Required
	@Column(unique=true)
    public String bnrCode;
	
	@Required
	public String bankStatus;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<BankAccount> bankAccount;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<PaymentLog> paymentLog;
	
	public static Finder<Long, Bank> find= new Finder<Long, Bank>(Long.class,Bank.class);
	
	
}
