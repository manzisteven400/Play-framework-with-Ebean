/**
 * 
 */
package com.bkt.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CreatedTimestamp;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * @author pc
 *
 */
@Entity
@Table(name = "profile_privilege")
public class ProfilePrivilege extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Required
	public Long profileId;
	
	@Required
	public Long privilegeId;
	
	@Required
	public String status;
	
	@Required
	@CreatedTimestamp
	public Date regDate;
	
	@CreatedTimestamp
	public Date modDate;
	
	public static Finder<Long, ProfilePrivilege> find=new Finder<Long,ProfilePrivilege>(Long.class, ProfilePrivilege.class);
	
}
