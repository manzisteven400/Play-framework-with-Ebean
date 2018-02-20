/**
 * 
 */
package com.bkt.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * @author pc
 *
 */
@Entity
@Table(name = "user_account_archive")
public class UserAccountArchive extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    public Long Id;
    
	@Required(message="id is required")
    public Long userId;
    
	@Required(message="Insitution id is required")
	public Long instId;
    
    @Required(message="Phone is required")
    public String phoneNumber; 
    
    @Required(message="Email is required") 
    @Email(message="Invalid Email Address")
    public String userEmail;
    
    @Required(message="Enter a password")
    @MinLength(value = 8, message="Password must be at least 8 characters")
    public String userPass;
    
    @Required(message="Your First name is required")
    public String userFirstName;  
    
    @Required(message="Your Last name is required")
    public String userLastName; 
    
    public String userAccountValidateCode;
    
    @Required
    public String userPrivilege;
    
    @Required
    public String userStatus;
    
    @Required
    public String accountStatus;
	
    @Required
    public long createdById;
	
    public long modById;
	  
    @Required
    public Date regDate;
	
    public Date modDate;
	
    public static Finder<Long, UserAccountArchive> find=new Finder<Long,UserAccountArchive>(Long.class, UserAccountArchive.class);
	



}
