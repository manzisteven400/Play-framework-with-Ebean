/**
 * 
 */
package com.bkt.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * @author pc
 *
 */
@Entity
@Table(name = "user_privilege")
public class UserPrivilege  extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Required
	public String name;
	
	@Required
	public String description;


	public static Finder<Long, UserPrivilege> find=new Finder<Long,UserPrivilege>(Long.class, UserPrivilege.class);
	
}
