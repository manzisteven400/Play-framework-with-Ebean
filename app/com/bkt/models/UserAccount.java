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

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * @author pc
 *
 */
@Entity
@Table(name = "user_account")
public class UserAccount extends Model {
	
	
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
    
    @Required(message="Phone is required")
    @Column(unique=true)
    public String phoneNumber; 
    
    @Required(message="Email is required") 
    @Email(message="Invalid Email Address")
    @Column(unique=true)
    public String email;
    
    @Required(message="Enter a password")
    @MinLength(value = 8, message="Password must be at least 8 characters")
    public String password;
    
    @Required(message="Your First name is required")
    public String firstName;  
    
    @Required(message="Your Last name is required")
    public String lastName; 
    
    @ManyToOne
	@JoinColumn(name = "user_profile_id", referencedColumnName = "id")
	public UserProfile userProfile;
    
    @Required
    public String status;
    
    @Required
    public long createdById;
	
    public long modById;
	  
    @Required
    @CreatedTimestamp
    public Date regDate;
	
    @UpdatedTimestamp
    public Date modDate;
    
    /**
	 * bi-directional one-to-many association to USSDTempLog
	 * 
	 */
	@OneToMany(cascade=CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;
	
    public static Finder<Long, UserAccount> find=new Finder<Long,UserAccount>(Long.class, UserAccount.class);
	
}
