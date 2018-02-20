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
 * The persistent class for the user_group database table.
 * 
 */
@Entity
@Table(name = "message")
public class Message extends Model {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue
	public Long id;
	
	@Column(name = "id")
	public String phone;
	
	@Column(name = "date_time")
	public String dateTime;
	
	@Column(name = "status")
	public int status;
	
	@Column(name = "message_text")
	public String messageText;
	
	/**
	 * bi-directional many-to-one association to userAccount
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "user_account_id", referencedColumnName = "id")
	public UserAccount userAccount;
	
	/**
	 * bi-directional many-to-one association to userAccount
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "topic_id", referencedColumnName = "id")
	public Topic topic;
	
	/**
	 * Manages insert,update.delete and select operations in database
	 * 
	 */
	public static Finder<Long, Message> find = new Finder<Long, Message>(Long.class, Message.class);

}
