package com.bkt.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;

/**
 * The persistent class for the user_group database table.
 * 
 */
@Entity
@Table(name = "topic")
public class Topic extends Model {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue
	public Long id;
	
	@Column(name = "heading_eng")
	public String headingEng;
	
	@Column(name = "heading_kiny")
	public String headingKiny;
	
	@Column(name = "heading_fre")
	public String headingFre;
	
	/**
	 * One-to-Many association to USSDTempLog
	 * 
	 */
	@OneToMany(cascade=CascadeType.ALL)
	public List<USSDTempLog> ussdTempLog;
	
	
	@Column(name = "date_time")
	public String dateTime;
	
	/**
	 * Many-to-one association to Message
	 * 
	 */
	@OneToMany(cascade=CascadeType.ALL)
	public List<Message> message;
	
	/**
	 * Manages insert,update.delete and select operations in database
	 * 
	 */
	public static Finder<Long, Topic> find = new Finder<Long, Topic>(Long.class, Topic.class);

}
