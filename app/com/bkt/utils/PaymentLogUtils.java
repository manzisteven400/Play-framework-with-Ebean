package com.bkt.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.bkt.models.AcademicProgram;
import com.bkt.models.Bank;
import com.bkt.models.BankAccount;
import com.bkt.models.DegreeProgram;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
import com.bkt.models.InstitutionCalender;
import com.bkt.models.NoneDegreeProgram;
import com.bkt.models.PaymentLog;
import com.bkt.models.PaymentPurpose;
import com.bkt.models.Student;
import com.bkt.models.StudyProgramType;
import com.bkt.models.Student.ApplicantStatus;
import com.bkt.models.Student.StudentStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

public class PaymentLogUtils {
	private static final Logger LOG = LoggerFactory.getLogger(PaymentLogUtils.class);

	public static JsonNode  sumOfPaymentByStudentPerInstIdAndYear(Long instId,Long year,String isRegistered) {
	    final String sql = "SELECT SUM(amount_paid) as total_sum,student_id,institution_calender_id FROM payment_log where status_desc='posted' AND ussd_status='SUCCESS' AND institution_id="+instId+" AND is_registered=\""+isRegistered+"\"  AND institution_calender_id="+year+" group by student_id";
	    ObjectNode payLog;
	     ObjectNode allpayLog= Json.newObject();
	    ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		
	    //SELECT SUM(amount_paid) as tot_amount,student_id,institution_calender_id FROM university_db.payment_log where is_registered='yes' AND institution_calender_id=1 group by student_id
	    SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
	    List<SqlRow> findList = sqlQuery.findList();
	    int totalAmount=0;
	    for(SqlRow row:findList){
	    	payLog = Json.newObject();
	    	try {
	    		totalAmount=totalAmount+row.getInteger("total_sum");
		    	payLog.put("totalSum", row.getLong("total_sum"));
		    	payLog.put("studentId", row.getLong("student_id"));
		    	payLog.put("instCalender", row.getLong("institution_calender_id"));
		    	array.add(payLog);
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				//e.printStackTrace();
				
			}
	    }
	    allpayLog.put("data", array);
	    allpayLog.put("totalAmount", totalAmount);
	    return allpayLog;
	}public static List<Student>  searchByInstitutionAndKeyword(Long instId,String keyword) {
	    final String sql = "SELECT * FROM student where institution_id="+instId+" AND faculty_id in (select id from faculty where institution_id="+instId+" AND name LIKE '%"+keyword+"%' OR accronym LIKE '%"+keyword+"%' OR code LIKE '%"+keyword+"%') OR email LIKE '%"+keyword+"%' OR "
	    		+ "first_name LIKE '%"+keyword+"%' OR last_name LIKE '%"+keyword+"%' OR nida LIKE '%"+keyword+"%' OR reg_number LIKE '%"+keyword+"%' OR phone LIKE '%"+keyword+"%'";
	    LOG.info("sql..."+sql);
	    Student payLog;
	    SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
	    List<SqlRow> findList = sqlQuery.findList();
	   
	    //list of students to be filled from query
	    List<Student> myList=new ArrayList<Student>();
	    for(SqlRow row:findList){
	    	payLog = new Student();
	    	try {
	    		try {
					if(row.getLong("academic_program_id")>0){
						payLog.academicProgram=AcademicProgram.find.byId(row.getLong("academic_program_id"));
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    		try {
					if(row.getLong("institution_calender_id")>0){
						payLog.academicYear=InstitutionCalender.find.byId(row.getLong("institution_calender_id"));
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    		try {
					if(row.getLong("degree_program_id")>0){
						payLog.degreeProgram=DegreeProgram.find.byId(row.getLong("degree_program_id"));
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    		try {
					if(row.getLong("faculty_id")>0){
						payLog.facultyId=Faculty.find.byId(row.getLong("faculty_id"));
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    		try {
					if(row.getLong("institution_id")>0){
						payLog.instId=Institution.find.byId(row.getLong("institution_id"));
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		try {
					if(row.getLong("none_degree_program_id")>0){
						payLog.noneDegreeProgram=NoneDegreeProgram.find.byId(row.getLong("none_degree_program_id"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getLong("study_program_type_id")>0){
						payLog.studyProgram=StudyProgramType.find.byId(row.getLong("study_program_type_id"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("applicant_status")!=null){
						payLog.applicantStatus=ApplicantStatus.valueOf(row.getString("applicant_status"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("dob")!=null){
						payLog.dob=row.getString("dob");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("email")!=null){
						payLog.email=row.getString("email");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("first_name")!=null){
						payLog.firstName=row.getString("first_name");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("last_name")!=null){
						payLog.lastName=row.getString("last_name");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("nida")!=null){
						payLog.nida=row.getString("nida");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("phone")!=null){
						payLog.phone=row.getString("phone");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("reg_number")!=null){
						payLog.regNumber=row.getString("reg_number");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("sex")!=null){
						payLog.sex=row.getString("sex");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("std_class")!=null){
						payLog.stdClass=row.getString("std_class");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("std_pic")!=null){
						payLog.stdPic=row.getString("std_pic");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("std_status")!=null){
						payLog.stdStatus=StudentStatus.valueOf(row.getString("std_status"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		
	    		myList.add(payLog);
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				//e.printStackTrace();
				
			}
	    }
	    
	    return myList;
	}public static JsonNode  sumOfPaymentByPurposePerInstIdAndYear(Long instId,Long year) {
	    final String sql = "SELECT SUM(amount_paid) as tot_amount,institution_calender_id,payment_purpose_id FROM payment_log where status_desc='posted' AND ussd_status='SUCCESS' AND institution_id="+instId+" AND institution_calender_id="+year+" group by payment_purpose_id";
	    ObjectNode payLog;

	     ObjectNode allpayLog= Json.newObject();
	    ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
	    //SELECT SUM(amount_paid) as tot_amount,student_id,institution_calender_id FROM university_db.payment_log where is_registered='yes' AND institution_calender_id=1 group by student_id
	    SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
	    List<SqlRow> findList = sqlQuery.findList();
	    int totalAmount=0;
	    for(SqlRow row:findList){
	    	payLog = Json.newObject();
	    try {
	    	totalAmount=totalAmount+row.getInteger("tot_amount");
	    	payLog.put("totalSum", row.getLong("tot_amount"));
	    	payLog.put("purposeId", row.getLong("payment_purpose_id"));
	    	payLog.put("instCalender", row.getLong("institution_calender_id"));
	    	array.add(payLog);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
		}

    	
	    }
	    allpayLog.put("data", array);
	    allpayLog.put("totalAmount", totalAmount);
	    return array;
	}public static long sumOfPaymentByChannelPerInstIdAndYear(Long instId,Long year,String channel,String isRegistered) {
	    final String sql = "SELECT SUM(amount_paid) as total_sum FROM payment_log where status_desc='posted' AND ussd_status='SUCCESS' AND institution_id="+instId+" AND is_registered=\""+isRegistered+"\" AND payment_channel=\""+channel+"\" AND institution_calender_id="+year;

	    SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
	    SqlRow row = sqlQuery.findUnique();
	    try {
			return row.getLong("total_sum");
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			////e.printStackTrace();
			
			return Long.parseLong("0");
		}
	}//.eq("status_desc","posted")
	//.eq("ussd_status","success")
	
	public static long totalCountOfPaymentByChannelPerInstIdAndYear(Long instId,Long year,String channel,String isRegistered) {
	    final String sql = "SELECT COUNT(id) as total_count FROM payment_log where status_desc='posted' AND ussd_status='SUCCESS' AND institution_id="+instId+" AND is_registered=\""+isRegistered+"\" AND payment_channel=\""+channel+"\" AND institution_calender_id="+year;

	    SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
	    SqlRow row = sqlQuery.findUnique();
	    try {
			return row.getLong("total_count");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
			
			return Long.parseLong("0");
		}
	}public static List<PaymentLog>  searchPaymentByInstitutionAndKeyword(Long instId,String keyword) {
		
	    final String sql = "SELECT * FROM payment_log where institution_id="+instId+" AND "
	    		+ "(faculty_id in (select id from faculty where institution_id="+instId+" AND name LIKE '%"+keyword+"%' OR accronym LIKE '%"+keyword+"%' OR code LIKE '%"+keyword+"%')"
	    				+ " OR bank_account_id in (select id from bank_account where institution_id="+instId+" AND account_number LIKE '%"+keyword+"%') "
	    						+ " OR payment_purpose_id in (select id from payment_purpose where purpose LIKE '%"+keyword+"%') OR institution_calender_id in (select id from institution_calender where institution_id="+instId+" AND academic_year LIKE '%"+keyword+"%') OR student_id in (SELECT id FROM student where institution_id="+instId+" AND faculty_id in (select id from faculty where institution_id="+instId+" AND name LIKE '%"+keyword+"%' OR accronym LIKE '%"+keyword+"%' OR code LIKE '%"+keyword+"%') OR email LIKE '%"+keyword+"%' OR "
	    		+ "first_name LIKE '%"+keyword+"%' OR last_name LIKE '%"+keyword+"%' OR nida LIKE '%"+keyword+"%' OR reg_number LIKE '%"+keyword+"%' OR phone LIKE '%"+keyword+"%') OR amount_paid LIKE '%"+keyword+"%' OR "
	    		+ "ext_trx_id LIKE '%"+keyword+"%' OR bank_slip LIKE '%"+keyword+"%' OR payment_channel LIKE '%"+keyword+"%' OR processing_number LIKE '%"+keyword+"%' OR msisdn LIKE '%"+keyword+"%' OR payment_date LIKE '%"+keyword+"%' OR posting_date LIKE '%"+keyword+"%')";
	    
	    
	    LOG.info("sql..."+sql);
	    PaymentLog payLog;
	    SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
	    List<SqlRow> findList = sqlQuery.findList();
	   
	    //list of students to be filled from query
	    List<PaymentLog> myList=new ArrayList<PaymentLog>();
	    for(SqlRow row:findList){
	    	payLog = new PaymentLog();
	    	try {
	    		try {
					if(row.getLong("institution_calender_id")>0){
						payLog.academicYear=InstitutionCalender.find.byId(row.getLong("institution_calender_id"));
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    		
	    		try {
					if(row.getLong("faculty_id")>0){
						payLog.facultId=Faculty.find.byId(row.getLong("faculty_id"));
					}
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    		try {
					if(row.getLong("institution_id")>0){
						payLog.instId=Institution.find.byId(row.getLong("institution_id"));
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		try {
					if(row.getLong("bank_id")>0){
						payLog.bank=Bank.find.byId(row.getLong("bank_id"));
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		
	    		try {
					if(row.getLong("bank_account_id")>0){
						payLog.bankAcc=BankAccount.find.byId(row.getLong("bank_account_id"));
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		try {
					if(row.getLong("student_id")>0){
						payLog.studentId=Student.find.byId(row.getLong("student_id"));
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		try {
					if(row.getLong("payment_purpose_id")>0){
						payLog.paymentPurpose=PaymentPurpose.find.byId(row.getLong("payment_purpose_id"));
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		
	    		try {
					if(row.getDouble("amount_paid")!=null){
						payLog.amountPaid=row.getDouble("amount_paid");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}

	    		try {
					if(row.getDouble("payment_year")!=null){
						payLog.paymentYear=row.getInteger("payment_year");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getLong("id")!=null){
						payLog.id=row.getLong("id");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("bank_slip")!=null){
						payLog.bankSlip=row.getString("bank_slip");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("is_registered")!=null){
						payLog.isRegistered=row.getString("is_registered");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("msisdn")!=null){
						payLog.msisdn=row.getString("msisdn");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("nida")!=null){
						payLog.nida=row.getString("nida");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("operator")!=null){
						payLog.operator=row.getString("operator");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("payer_name")!=null){
						payLog.payerName=row.getString("payer_name");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("payment_channel")!=null){
						payLog.paymentChannel=row.getString("payment_channel");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("payment_date")!=null){
						payLog.paymentDate=row.getDate("payment_date");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("posting_date")!=null){
						payLog.postingDate=row.getDate("posting_date");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("payment_device")!=null){
						payLog.paymentDevice=row.getString("payment_device");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("processing_number")!=null){
						payLog.processingNumber=row.getString("processing_number");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("status_desc")!=null){
						payLog.statusDesc=row.getString("status_desc");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		
	    		try {
					if(row.getString("student_names")!=null){
						payLog.studentNames=row.getString("student_names");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		try {
					if(row.getString("ussd_status")!=null){
						payLog.ussdStatus=row.getString("ussd_status");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    		
	    		myList.add(payLog);
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				//e.printStackTrace();
				
			}
	    }
	    
	    return myList;
	}

}
