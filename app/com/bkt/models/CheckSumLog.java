package com.bkt.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name = "check_sum_log")
public class CheckSumLog extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Column(name="check_sum")
	public String checkSum;
	
	@Column(name="date_time")
	public String dateTime;
	
	@Column(name="check_sum_type")
	public String checkSumType;
	
	@Column(name="status")
	public int status;
	
	@Column(name="check_sum_count")
	public int checkSumCount;
	
	public static Finder<Long, CheckSumLog> find = new Finder<Long, CheckSumLog>(Long.class, CheckSumLog.class);

}
