/**
 * 
 */
package com.bkt.models;

import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "user_profile")
public class UserProfile extends Model {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Required
	public String name;
	
	@Required
	public String description;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<UserAccount> userAccount;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;

	public static Finder<Long, UserProfile> find=new Finder<Long,UserProfile>(Long.class, UserProfile.class);
	
}
