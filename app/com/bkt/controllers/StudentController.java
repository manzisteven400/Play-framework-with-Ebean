/**
 * 
 */
package com.bkt.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.avaje.ebean.Page;
import com.bkt.controllers.auth.AgendaAuthenticator;
import com.bkt.models.AcademicProgram;
import com.bkt.models.DegreeProgram;
import com.bkt.models.Faculty;
import com.bkt.models.Institution;
import com.bkt.models.InstitutionCalender;
import com.bkt.models.NoneDegreeProgram;
import com.bkt.models.PaymentLog;
import com.bkt.models.Student;
import com.bkt.models.StudyProgramType;
import com.bkt.utils.LogRequest;
import com.bkt.utils.PaymentLogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

/**
 * @author pc
 *
 */
@LogRequest
@Authenticated(AgendaAuthenticator.class)
@com.bkt.utils.CorsComposition.Cors
public class StudentController extends Controller {
	private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);

	public static Result listJson() {
		MDC.put("method", "listJson");

		LOG.info("Requested all students");
		ObjectNode httpStatus = Json.newObject();

		List<Student> all = Student.find.order("id desc").findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);

			LOG.info("Empty List is returned for all students");
			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Student user : all) {
				userJson = Json.newObject();

				userJson.put("applicantStatus", user.applicantStatus.toString());
				userJson.put("email", user.email);
				userJson.put("firstName", user.firstName);
				userJson.put("instId", user.instId.id);
				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("stdId", user.id);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdPic", user.stdPic);
				userJson.put("stdStatus", user.stdStatus.toString());
				userJson.put("dob", user.dob);
				userJson.put("sex", user.sex);
				userJson.put("stdClass", user.stdClass);
				try {
					userJson.put("academicProgram", user.academicProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("academicYear", user.academicYear.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("degreeProgram", user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgram", user.studyProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyName", user.facultyId.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}try {
					userJson.put("facultyId", user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array.add(userJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all students with count:" + all.size());

			return ok(httpStatus);
		}

	}

	public static Result searchAll(String name) {
		MDC.put("method", "searchAll");

		ObjectNode httpStatus = Json.newObject();

		List<Student> all = Student.find.where()
				.disjunction()
				.like("email", "%" + name + "%")
				.like("first_name", "%" + name + "%")
				.like("last_name", "%" + name + "%")
				.like("reg_number", "%" + name + "%")
				.like("std_status", "%" + name + "%")
				.like("phone", "%" + name + "%")
				.like("nida", "%" + name + "%")
				.order("id desc").findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);
			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Student user : all) {
				userJson = Json.newObject();

				userJson.put("applicantStatus", user.applicantStatus.toString());
				userJson.put("email", user.email);
				userJson.put("facultyId", user.id);
				userJson.put("firstName", user.firstName);
				userJson.put("instId", user.instId.id);
				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("stdId", user.id);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdPic", user.stdPic);
				userJson.put("stdStatus", user.stdStatus.toString());
				userJson.put("dob", user.dob);
				userJson.put("sex", user.sex);
				userJson.put("stdClass", user.stdClass);
				try {
					userJson.put("academicProgram", user.academicProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("academicYear", user.academicYear.academicYear);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("degreeProgram", user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgram", user.studyProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyName", user.facultyId.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}try {
					userJson.put("facultyId", user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array.add(userJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all students with count:" + all.size());

			return ok(httpStatus);
		}

	}

	public static Result searchAllByInsist(Long insitId, String name) {
		MDC.put("method", "searchAllByInsist");

		ObjectNode httpStatus = Json.newObject();
		//Long facultyId = Long.parseLong("0");
		/*List<Faculty> findList = Faculty.find.where().eq("institution_id", insitId).like("name", "%" + name + "%")
		.like("code", "%" + name + "%")
		.like("accronym", "%" + name + "%").findList();
		
		List<Long> myFaculties = new ArrayList<Long>();
		if(findList.size()>0){
			for(Faculty myFaculty:findList){
				myFaculties.add(myFaculty.id);
			}
		}*/
		try {//code
			//facultyId;
			
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		/*List<Student> all = Student.find.where()
				.eq("institution_id", insitId)
				.in("faculty_id", myFaculties)
				.disjunction()
				.like("email", "%" + name + "%")
				.like("first_name", "%" + name + "%")
				.like("last_name", "%" + name + "%")
				.like("reg_number", "%" + name + "%")
				.like("std_status", "%" + name + "%")
				.like("phone", "%" + name + "%")
				.like("nida", "%" + name + "%")
				.order("id desc").findList();*/

		if (PaymentLogUtils.searchByInstitutionAndKeyword(insitId, name).isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + PaymentLogUtils.searchByInstitutionAndKeyword(insitId, name).size());

			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Student user : PaymentLogUtils.searchByInstitutionAndKeyword(insitId, name)) {
				userJson = Json.newObject();

				userJson.put("applicantStatus", user.applicantStatus.toString());
				userJson.put("email", user.email);
				userJson.put("facultyId", user.id);
				userJson.put("firstName", user.firstName);
				userJson.put("instId", user.instId.id);
				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("stdId", user.id);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdPic", user.stdPic);
				userJson.put("stdStatus", user.stdStatus.toString());
				userJson.put("dob", user.dob);
				userJson.put("sex", user.sex);
				userJson.put("stdClass", user.stdClass);
				try {
					userJson.put("academicProgram", user.academicProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("academicYear", user.academicYear.academicYear);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("degreeProgram", user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgram", user.studyProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyName", user.facultyId.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}try {
					userJson.put("facultyId", user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array.add(userJson);
			}
			httpStatus.put("rowCount", PaymentLogUtils.searchByInstitutionAndKeyword(insitId, name).size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all students with count:" + PaymentLogUtils.searchByInstitutionAndKeyword(insitId, name).size());

			return ok(httpStatus);
		}

	}

	public static Result allStudentsByInstIdAndFacultyId(Long instId, Long facultyId) {
		MDC.put("method", "allStudentsByInstIdAndFacultyId");

		ObjectNode httpStatus = Json.newObject();
		List<Student> all = Student.find.where().eq("institution_id", instId).eq("faculty_id", facultyId).order("id desc").findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);
			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Student user : all) {
				userJson = Json.newObject();

				userJson.put("applicantStatus", user.applicantStatus.toString());
				userJson.put("email", user.email);
				userJson.put("facultyId", user.id);
				userJson.put("firstName", user.firstName);
				userJson.put("instId", user.instId.id);
				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("stdId", user.id);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdPic", user.stdPic);
				userJson.put("stdStatus", user.stdStatus.toString());
				userJson.put("dob", user.dob);
				userJson.put("sex", user.sex);
				userJson.put("stdClass", user.stdClass);
				try {
					userJson.put("academicProgram", user.academicProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("academicYear", user.academicYear.academicYear);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("degreeProgram", user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgram", user.studyProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyName", user.facultyId.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}try {
					userJson.put("facultyId", user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array.add(userJson);
			}
			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all students with count:" + all.size());

			return ok(httpStatus);
		}

	}

	public static Result allStudentsByInstId(Long instId) {
		MDC.put("method", "allStudentsByInstId");

		ObjectNode httpStatus = Json.newObject();
		List<Student> all = Student.find.where().eq("institution_id", instId).order("id desc").findList();

		if (all.isEmpty()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		} else {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Student user : all) {
				userJson = Json.newObject();

				userJson.put("applicantStatus", user.applicantStatus.toString());
				userJson.put("email", user.email);
				userJson.put("facultyId", user.id);
				userJson.put("firstName", user.firstName);
				userJson.put("instId", user.instId.id);
				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("stdId", user.id);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdPic", user.stdPic);
				userJson.put("stdStatus", user.stdStatus.toString());
				userJson.put("dob", user.dob);
				userJson.put("sex", user.sex);
				userJson.put("stdClass", user.stdClass);
				try {
					userJson.put("academicProgram", user.academicProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("academicYear", user.academicYear.academicYear);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("degreeProgram", user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgram", user.studyProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyName", user.facultyId.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}try {
					userJson.put("facultyId", user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array.add(userJson);
			}

			httpStatus.put("rowCount", all.size());
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all students with count:" + all.size());

			return ok(httpStatus);
		}

	}

	public static Result showJson(Long id) {
		MDC.put("method", "showJson");

		ObjectNode httpStatus = Json.newObject();
		Student user = Student.find.byId(id);
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("applicantStatus", user.applicantStatus.toString());
			userJson.put("email", user.email);
			userJson.put("facultyId", user.id);
			userJson.put("firstName", user.firstName);
			userJson.put("instId", user.instId.id);
			userJson.put("lastName", user.lastName);
			userJson.put("nida", user.nida);
			userJson.put("password", user.password);
			userJson.put("phone", user.phone);
			userJson.put("stdId", user.id);
			userJson.put("regNumber", user.regNumber);
			userJson.put("stdPic", user.stdPic);
			userJson.put("stdStatus", user.stdStatus.toString());
			userJson.put("dob", user.dob);
			userJson.put("sex", user.sex);
			userJson.put("stdClass", user.stdClass);
			try {
				userJson.put("academicProgram", user.academicProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("academicYear", user.academicYear.academicYear);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("degreeProgram", user.degreeProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("studyProgram", user.studyProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("facultyName", user.facultyId.name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}try {
				userJson.put("facultyId", user.facultyId.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 1);

			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		}

	}

	public static Result showByRegNumber(String regNumber) {
		MDC.put("method", "showByRegNumber");

		ObjectNode httpStatus = Json.newObject();
		Student user = Student.find.where().eq("reg_number", regNumber).findUnique();
		if (user.id > 0) {
			ObjectNode userJson = Json.newObject();

			userJson.put("applicantStatus", user.applicantStatus.toString());
			userJson.put("email", user.email);
			userJson.put("facultyId", user.id);
			userJson.put("firstName", user.firstName);
			userJson.put("instId", user.instId.id);
			userJson.put("lastName", user.lastName);
			userJson.put("nida", user.nida);
			userJson.put("password", user.password);
			userJson.put("phone", user.phone);
			userJson.put("stdId", user.id);
			userJson.put("regNumber", user.regNumber);
			userJson.put("stdPic", user.stdPic);
			userJson.put("stdStatus", user.stdStatus.toString());
			userJson.put("dob", user.dob);
			userJson.put("sex", user.sex);
			userJson.put("stdClass", user.stdClass);
			try {
				userJson.put("academicProgram", user.academicProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("academicYear", user.academicYear.academicYear);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("degreeProgram", user.degreeProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("studyProgram", user.studyProgram.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				userJson.put("facultyName", user.facultyId.name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}try {
				userJson.put("facultyId", user.facultyId.id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 1);

			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		}

	}

	public static Result showByRegNumberBank(String regNumber) {

		MDC.put("method", "showByRegNumberBank");

		ObjectNode httpStatus = Json.newObject();
		Student user = Student.find.where().eq("reg_number", regNumber).findUnique();
		if (user.id > 0) {

			ObjectNode userJson = Json.newObject();

			userJson.put("applicantStatus", user.applicantStatus.toString());
			userJson.put("email", user.email);

			try {

				userJson.put("firstName", user.firstName);
				userJson.put("instName", user.instId.name);
				userJson.put("facultyId", user.facultyId.id);
				userJson.put("facultyName", user.facultyId.name);
				try {
					userJson.put("instId", user.instId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				// userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdClass", user.stdClass);
				// userJson.put("stdPic", user.stdPic);
				// userJson.put("stdId", user.stdId);
				// userJson.put("stdStatus", user.stdStatus.toString());
				// httpStatus.put("Code", "200");
				// httpStatus.put("status", "Success");
				// httpStatus.put("response", userJson);

				LOG.info("List is returned for all students with count:" + 1);

				return ok(userJson);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				userJson.put("status", "No data");

				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("rowCount", 0);
				httpStatus.put("response", userJson);

				LOG.info("List is returned for all students with count:" + 0);

				return ok(httpStatus);

			}

		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		}

	}

	@Transactional
	public static Result allStudentsPaging(Long pageNum, Long pageMax) {
		MDC.put("method", "allStudentsPaging");

		ObjectNode httpStatus = Json.newObject();

		// Paging starts
		Page<Student> pagedList = Student.find.where().order("id desc").findPagingList(pageMax.intValue()).setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<Student> allStudents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();

		if (allStudents.size() > 0) {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Student user : allStudents) {
				userJson = Json.newObject();

				userJson.put("applicantStatus", user.applicantStatus.toString());
				userJson.put("email", user.email);
				userJson.put("facultyId", user.id);
				userJson.put("firstName", user.firstName);
				userJson.put("instId", user.instId.id);
				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("stdId", user.id);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdPic", user.stdPic);
				userJson.put("stdStatus", user.stdStatus.toString());
				userJson.put("dob", user.dob);
				userJson.put("sex", user.sex);
				userJson.put("stdClass", user.stdClass);
				try {
					userJson.put("academicProgram", user.academicProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("academicYear", user.academicYear.academicYear);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("degreeProgram", user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgram", user.studyProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyName", user.facultyId.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyId", user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array.add(userJson);
			}
			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all students with count:" + totalRowCount);

			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		}

	}

	public static Result allStudentsByInstiPaging(Long instId, Long pageNum, Long pageMax) {
		MDC.put("method", "allStudentsByInstiPaging");

		ObjectNode httpStatus = Json.newObject();

		// Paging starts
		Page<Student> pagedList = Student.find.where().eq("institution_id", instId).orderBy("id desc").findPagingList(pageMax.intValue()).setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<Student> allStudents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();

		if (allStudents.size() > 0) {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Student user : allStudents) {
				userJson = Json.newObject();

				userJson.put("applicantStatus", user.applicantStatus.toString());
				userJson.put("email", user.email);
				userJson.put("firstName", user.firstName);
				userJson.put("instId", user.instId.id);
				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("stdId", user.id);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdPic", user.stdPic);
				userJson.put("stdStatus", user.stdStatus.toString());
				userJson.put("dob", user.dob);
				userJson.put("sex", user.sex);
				userJson.put("stdClass", user.stdClass);
				try {
					userJson.put("academicProgram", user.academicProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("academicYear", user.academicYear.academicYear);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("degreeProgram", user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgram", user.studyProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					userJson.put("facultyName", user.facultyId.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}try {
					userJson.put("facultyId", user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				array.add(userJson);
			}

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all students with count:" + totalRowCount);

			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		}

	}

	public static Result allStudentsByInstIdAndFacultyIdPaging(Long instId, Long facultyId, Long pageNum, Long pageMax) {
		MDC.put("method", "allStudentsByInstIdAndFacultyIdPaging");

		ObjectNode httpStatus = Json.newObject();

		// Paging starts
		Page<Student> pagedList = Student.find.where().eq("institution_id", instId).eq("faculty_id", facultyId).orderBy("id desc").findPagingList(pageMax.intValue()).setFetchAhead(false).getPage(pageNum.intValue());

		// fetch and return the list
		List<Student> allStudents = pagedList.getList();

		// get the total row count (from the future)
		int totalRowCount = pagedList.getTotalRowCount();

		if (allStudents.size() > 0) {
			ObjectNode userJson;
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode array = mapper.createArrayNode();
			for (Student user : allStudents) {
				userJson = Json.newObject();

				userJson.put("applicantStatus", user.applicantStatus.toString());
				userJson.put("email", user.email);
				userJson.put("facultyId", user.id);
				userJson.put("firstName", user.firstName);
				userJson.put("instId", user.instId.id);
				userJson.put("lastName", user.lastName);
				userJson.put("nida", user.nida);
				userJson.put("password", user.password);
				userJson.put("phone", user.phone);
				userJson.put("stdId", user.id);
				userJson.put("regNumber", user.regNumber);
				userJson.put("stdPic", user.stdPic);
				userJson.put("stdStatus", user.stdStatus.toString());
				userJson.put("dob", user.dob);
				userJson.put("stdClass", user.stdClass);
				userJson.put("sex", user.sex);
				try {
					userJson.put("academicProgram", user.academicProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("academicYear", user.academicYear.academicYear);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("degreeProgram", user.degreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("noneDegreeProgram", user.noneDegreeProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("studyProgram", user.studyProgram.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyName", user.facultyId.name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					userJson.put("facultyId", user.facultyId.id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array.add(userJson);
			}

			httpStatus.put("rowCount", totalRowCount);
			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", array);

			LOG.info("List is returned for all students with count:" + totalRowCount);

			return ok(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();

			userJson.put("status", "No data");

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.info("List is returned for all students with count:" + 0);

			return ok(httpStatus);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result createStudentJson() {
		MDC.put("method", "createStudentJson");

		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();
		LOG.info("Request to Insert Student received: " + asJson);

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.error("Students saving failed due to form errors: " + httpStatus);

			return badRequest(httpStatus);
		} else {

			
			ObjectNode userJson = Json.newObject();
			Student student = new Student();

			// Student application status
			if (Student.ApplicantStatus.ONGOING.toString().equalsIgnoreCase(asJson.findPath("applicantStatus").textValue())) {
				student.applicantStatus = Student.ApplicantStatus.ONGOING;
			} else {
				student.applicantStatus = Student.ApplicantStatus.DONE;
			}
			// Student account status
			if (Student.StudentStatus.ACTIVE.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.ACTIVE;
			} else if (Student.StudentStatus.ADMITTED.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.ADMITTED;
			} else if (Student.StudentStatus.APPLICANT.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.APPLICANT;
			} else if (Student.StudentStatus.OTHER.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.OTHER;
			} else if (Student.StudentStatus.OTHER.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.OTHER;
			} else {
				student.stdStatus = Student.StudentStatus.ACTIVE;
			}
			if(asJson.has("email")){
				if(asJson.findPath("email").textValue().length()>3){
					student.email = asJson.findPath("email").textValue();
				}
			}

			if(asJson.has("firstName")){
				if(asJson.findPath("firstName").textValue().length() > 0){
					student.firstName = asJson.findPath("firstName").textValue();
				}else{
					httpStatus.put("Code", "401");
					httpStatus.put("status", "firstName is mandatory");
					return badRequest(httpStatus);
				}
					
			}else{
				httpStatus.put("Code", "401");
				httpStatus.put("status", "firstName is mandatory");
				return badRequest(httpStatus);
			}
			
			if (asJson.has("facultyId")) {
				try {
					Faculty facById = Faculty.find.byId(asJson.findPath("facultyId").longValue());
					if(facById.name!=null){
						student.facultyId = facById;
					}else{

						httpStatus.put("Code", "401");
						httpStatus.put("status", "Faculty is not found");
						return badRequest(httpStatus);
					
					}
					
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					httpStatus.put("Code", "401");
					httpStatus.put("status", "Faculty is not found");
					return badRequest(httpStatus);
				}
				
			}

			Institution instById = Institution.find.byId(asJson.findPath("instId").longValue());
			long instId = 0;
			try {
				instId = instById.id;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (instId > 0) {
				student.instId = instById;
			} else {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "institution is not found");
				return badRequest(httpStatus);
			}

			student.lastName = asJson.findPath("lastName").textValue();
			if (!(student.lastName.length() > 0)) {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "lastName is mandatory");
				return badRequest(httpStatus);
			}
			
			if(asJson.has("nida")){
				if(asJson.findPath("nida").textValue().length()>3){
					student.nida = asJson.findPath("nida").textValue();
				}
			}
			student.password = asJson.findPath("password").textValue();
			student.phone = asJson.findPath("phone").textValue();

			student.regNumber = asJson.findPath("regNumber").textValue();

			if (!(student.regNumber.length() > 0)) {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "regNumber is mandatory");
				return badRequest(httpStatus);
			}
			student.stdPic = asJson.findPath("stdPic").textValue();
			student.dob = asJson.findPath("dob").textValue();
			student.sex = asJson.findPath("sex").textValue();
			student.stdClass = asJson.findPath("stdClass").textValue();

			if (asJson.has("academicProgram")) {
				AcademicProgram academicProgram = AcademicProgram.find.byId(asJson.findPath("academicProgram").longValue());
				long acadPrId = 0;
				try {
					acadPrId = academicProgram.id;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (acadPrId > 0) {
					student.academicProgram = academicProgram;
				} else {

					httpStatus.put("Code", "401");
					httpStatus.put("status", "academic program is not found");
					return badRequest(httpStatus);

				}

			} else {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "academic program is not found");
				return badRequest(httpStatus);
			}

			if (asJson.has("academicYear")) {
				String academicYearStr = asJson.findPath("academicYear").textValue();
				InstitutionCalender academicYear = InstitutionCalender.find.where().eq("academic_year", academicYearStr.trim()).eq("institution_id", instById.id).findUnique();
				long academiId = 0;
				try {
					academiId = academicYear.id;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (academiId > 0) {
					student.academicYear = academicYear;
				} else {
					httpStatus.put("Code", "401");
					httpStatus.put("status", "academicYear is not found");
					return badRequest(httpStatus);
				}

			} else {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "academicYear is not found");
				return badRequest(httpStatus);
			}

			if (asJson.has("degreeProgram")) {
				DegreeProgram degreeProgram = DegreeProgram.find.byId(asJson.findPath("degreeProgram").longValue());
				long degId = 0;
				try {
					degId = degreeProgram.id;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (degId > 0) {
					student.degreeProgram = degreeProgram;
				} else {
					httpStatus.put("Code", "401");
					httpStatus.put("status", "degreeProgram is not found");
					return badRequest(httpStatus);
				}

			}

			if (asJson.has("noneDegreeProgram")) {
				NoneDegreeProgram noneDegreeProgram = NoneDegreeProgram.find.byId(asJson.findPath("noneDegreeProgram").longValue());
				long degId = 0;
				try {
					degId = noneDegreeProgram.id;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (degId > 0) {
					student.noneDegreeProgram = noneDegreeProgram;
				} else {
					httpStatus.put("Code", "401");
					httpStatus.put("status", "noneDegreeProgram is not found");
					return badRequest(httpStatus);
				}

			}

			if (asJson.has("studyProgram")) {
				StudyProgramType studyProgram = StudyProgramType.find.byId(asJson.findPath("studyProgram").longValue());
				long degId = 0;
				try {
					degId = studyProgram.id;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (degId > 0) {
					student.studyProgram = studyProgram;
				} else {

					httpStatus.put("Code", "401");
					httpStatus.put("status", "studyProgram is not found");
					return badRequest(httpStatus);
				}

			}

			// save the user account
			try {
				student.save();
				userJson.put("status", "saved");

				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("rowCount", 0);
				httpStatus.put("response", userJson);

				LOG.info("Students saved with response: " + httpStatus);

				return ok(httpStatus);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("error", e.getMessage());

				LOG.debug("Students saving failed due to form errors: " + httpStatus);

				return badRequest(httpStatus);

			}

		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result bulkStudentRegistrationJson() {
		MDC.put("method", "bulkStudentRegistrationJson");

		ObjectNode httpStatus = Json.newObject();
		JsonNode jsonRequest = request().body().asJson();
		LOG.info("Request to Insert Student received: ");
		// ArrayNode results = (ArrayNode) jsonRequest;

		/*
		 * ObjectMapper mapper = new ObjectMapper(); JsonNode rootNode =
		 * mapper.readTree(json); ArrayNode slaidsNode = (ArrayNode)
		 * rootNode.get("SLA");
		 */
		// Iterator<JsonNode> it = results.elements();
		ArrayNode results = (ArrayNode) jsonRequest;

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode entriesWithErrors = mapper.createArrayNode();

		ObjectMapper mapper2 = new ObjectMapper();
		ArrayNode entriesWithDuplicates = mapper2.createArrayNode();
		ArrayNode entriesErrorMsg = mapper2.createArrayNode();

		int countSuccess = 0;
		int countFailed = 0;
		int countDuplciated = 0;
		String duplicatedString = "";

		ObjectNode userJson = Json.newObject();

		if (jsonRequest.isNull()) {
			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("rowCount", 0);
			httpStatus.put("response", userJson);

			LOG.error("Students saving failed due to form errors: " + httpStatus);
			countFailed++;
			return badRequest(httpStatus);
		} else {
			Student student;
			ObjectNode errorJson;
			// while (it.hasNext()) {
			for (JsonNode node : results) {

				errorJson = Json.newObject();
				String errorMsg = "Entry for student:" + node.findPath("firstName").textValue() + " " + node.findPath("lastName").textValue() + " failed due to reasons:";

				// JsonNode node = it.next();
				if (node.isNull()) {
					countFailed++;
				} else {
					// Form<Student> contactForm =
					// Form.form(Student.class).bind(node);
					student = new Student();
					int errorFound = 0;

					// Student application status
					if (Student.ApplicantStatus.ONGOING.toString().equalsIgnoreCase(node.findPath("applicantStatus").textValue())) {
						student.applicantStatus = Student.ApplicantStatus.ONGOING;
					} else {
						student.applicantStatus = Student.ApplicantStatus.DONE;
					}
					// Student account status
					if (Student.StudentStatus.ACTIVE.toString().equalsIgnoreCase(node.findPath("stdStatus").textValue())) {
						student.stdStatus = Student.StudentStatus.ACTIVE;
					} else if (Student.StudentStatus.ADMITTED.toString().equalsIgnoreCase(node.findPath("stdStatus").textValue())) {
						student.stdStatus = Student.StudentStatus.ADMITTED;
					} else if (Student.StudentStatus.APPLICANT.toString().equalsIgnoreCase(node.findPath("stdStatus").textValue())) {
						student.stdStatus = Student.StudentStatus.APPLICANT;
					} else if (Student.StudentStatus.OTHER.toString().equalsIgnoreCase(node.findPath("stdStatus").textValue())) {
						student.stdStatus = Student.StudentStatus.OTHER;
					} else if (Student.StudentStatus.OTHER.toString().equalsIgnoreCase(node.findPath("stdStatus").textValue())) {
						student.stdStatus = Student.StudentStatus.OTHER;
					} else {
						student.stdStatus = Student.StudentStatus.ACTIVE;
					}
					
					if(node.has("email")){
						if(node.findPath("email").textValue().length()>3){
							student.email = node.findPath("email").textValue();
						}
					}
					student.firstName = node.findPath("firstName").textValue();
					if (!(student.firstName.length() > 0)) {

						errorMsg = errorMsg + "firstName is mandatory";
						errorFound = 1;
					}

					if (node.has("facultyId")) {
						Faculty facultyById = Faculty.find.byId(node.findPath("facultyId").longValue());
						long facId = 0;
						try {
							facId = facultyById.id;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (facId > 0) {
							student.facultyId = facultyById;
						} else {

							errorMsg = errorMsg + "faculty is not found";
							errorFound = 1;
						}

					}

					Institution instById = Institution.find.byId(node.findPath("instId").longValue());
					long instId = 0;
					try {
						instId = instById.id;
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (instId > 0) {
						student.instId = instById;
					} else {

						errorMsg = errorMsg + "institution is not found";
						errorFound = 1;

					}
					student.lastName = node.findPath("lastName").textValue();
					if (!(student.lastName.length() > 0)) {
						errorMsg = errorMsg + "lastName is mandatory";
						errorFound = 1;
					}
					if(node.has("nida")){
						if(node.findPath("nida").textValue().length()>3){
							student.nida = node.findPath("nida").textValue();
						}
					}
					student.password = node.findPath("password").textValue();
					student.phone = node.findPath("phone").textValue();

					student.regNumber = node.findPath("regNumber").textValue();
					if (!(student.regNumber.length() > 0)) {
						errorMsg = errorMsg + "regNumber is mandatory";
						errorFound = 1;
					}/*else{
						Student.find.where().eq("", node.findPath("regNumber").textValue()).eq("", node.findPath("instId").longValue()).findUnique();
					}*/
					student.stdPic = node.findPath("stdPic").textValue();
					student.dob = node.findPath("dob").textValue();
					student.sex = node.findPath("sex").textValue();
					student.stdClass = node.findPath("stdClass").textValue();

					if (node.has("academicProgram")) {
						AcademicProgram academicProgram = AcademicProgram.find.byId(node.findPath("academicProgram").longValue());

						long facId = 0;
						try {
							facId = academicProgram.id;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (facId > 0) {
							student.academicProgram = academicProgram;
						} else {
							httpStatus.put("Code", "401");
							errorMsg = errorMsg + "academicProgram is not found";
							errorFound = 1;
						}

					} else {
						httpStatus.put("Code", "401");
						errorMsg = errorMsg + "academicProgram is not found";
						errorFound = 1;
					}

					if (node.has("academicYear")) {
						String academicYearStr = node.findPath("academicYear").textValue();
						InstitutionCalender academicYear = InstitutionCalender.find.where().eq("academic_year", academicYearStr.trim()).eq("institution_id", instById.id).findUnique();

						long academiId = 0;
						try {
							academiId = academicYear.id;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (academiId > 0) {
							student.academicYear = academicYear;
						} else {
							httpStatus.put("Code", "401");
							errorMsg = errorMsg + "academicYear is not found";
							errorFound = 1;
						}

					} else {
						httpStatus.put("Code", "401");
						errorMsg = errorMsg + "academicYear is not found";
						errorFound = 1;
					}

						if (node.has("degreeProgram")) {
							DegreeProgram degreeProgram = DegreeProgram.find.byId(node.findPath("degreeProgram").longValue());
							long academiId = 0;
							try {
								academiId = degreeProgram.id;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (academiId > 0) {
								student.degreeProgram = degreeProgram;
							}else{
								httpStatus.put("Code", "401");
								errorMsg = errorMsg + "degreeProgram is not found";
								errorFound = 1;
							}
							
						}
					
						if (node.has("noneDegreeProgram")) {
							NoneDegreeProgram noneDegreeProgram = NoneDegreeProgram.find.byId(node.findPath("noneDegreeProgram").longValue());
							
							long academiId = 0;
							try {
								academiId = noneDegreeProgram.id;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (academiId > 0) {
								student.noneDegreeProgram = noneDegreeProgram;
							}else{
								httpStatus.put("Code", "401");
								errorMsg = errorMsg + "noneDegreeProgram is not found";
								errorFound = 1;
							}
						}
					
				
						if (node.has("studyProgram")) {
							StudyProgramType studyProgram = StudyProgramType.find.byId(node.findPath("studyProgram").longValue());
							
							long academiId = 0;
							try {
								academiId = studyProgram.id;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (academiId > 0) {
								student.studyProgram = studyProgram;
							}else{
								httpStatus.put("Code", "401");
								errorMsg = errorMsg + "studyProgram is not found";
								errorFound = 1;
							}
						}
					
					// save the user account
					try {
						if (errorFound == 1) {
							countFailed++;
							errorJson.put("errorMsg", errorMsg);
						} else {
							student.save();
							userJson.put("status", "saved");
							countSuccess++;
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						countFailed++;
						countDuplciated++;

						// load the duplicated entry
						ObjectNode duplicateJson = Json.newObject();

						duplicateJson.put("names", student.firstName + " " + student.lastName);
						duplicateJson.put("nida", student.nida);
						duplicateJson.put("regNumber", student.regNumber);
						duplicateJson.put("phone", student.phone);
						entriesWithDuplicates.add(duplicateJson);

						duplicatedString += student.regNumber + ";";
						LOG.error("Students saving failed with error: " + e);
					}

				}
				entriesErrorMsg.add(errorJson);
			}

			userJson.put("savedEntries", countSuccess);
			userJson.put("failedEntries", countFailed);
			userJson.put("failedReason", entriesErrorMsg);
			userJson.put("duplicateEntries", countDuplciated);
			userJson.put("formErrors", entriesWithErrors);
			userJson.put("duplucateReg", duplicatedString);
			userJson.put("duplicateJson", entriesWithDuplicates);

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);

			LOG.info("Students saved with response: " + httpStatus);

			return ok(httpStatus);

		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateStudentJson() {
		MDC.put("method", "updateStudentJson");

		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);

			LOG.debug("Students updating failed due to form errors: " + httpStatus);

			return badRequest(httpStatus);
		} else {
			// Form<Student> contactForm =
			// Form.form(Student.class).bind(asJson);
			/*
			 * if (contactForm.hasErrors()) {
			 * 
			 * httpStatus.put("Code", "401"); httpStatus.put("status",
			 * "Bad request"); httpStatus.put("response",
			 * contactForm.errorsAsJson());
			 * 
			 * LOG.error("Students updating failed due to form errors: " +
			 * httpStatus);
			 * 
			 * return badRequest(httpStatus); } else {
			 */
			ObjectNode userJson = Json.newObject();

			Student student = Student.find.byId(asJson.findPath("stdId").longValue());
			// Student application status
			if (Student.ApplicantStatus.ONGOING.toString().equalsIgnoreCase(asJson.findPath("applicantStatus").textValue())) {
				student.applicantStatus = Student.ApplicantStatus.ONGOING;
			} else {
				student.applicantStatus = Student.ApplicantStatus.DONE;
			}
			// Student account status
			if (Student.StudentStatus.ACTIVE.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.ACTIVE;
			} else if (Student.StudentStatus.ADMITTED.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.ADMITTED;
			} else if (Student.StudentStatus.APPLICANT.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.APPLICANT;
			} else if (Student.StudentStatus.OTHER.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.OTHER;
			} else if (Student.StudentStatus.OTHER.toString().equalsIgnoreCase(asJson.findPath("stdStatus").textValue())) {
				student.stdStatus = Student.StudentStatus.OTHER;
			} else {
				student.stdStatus = Student.StudentStatus.ACTIVE;
			}
			student.email = asJson.findPath("email").textValue();
			student.firstName = asJson.findPath("firstName").textValue();
			if (!(student.firstName.length() > 0)) {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "firstName is mandatory");
				return badRequest(httpStatus);
			}
			student.facultyId = Faculty.find.byId(asJson.findPath("facultyId").longValue());
			student.instId = Institution.find.byId(asJson.findPath("instId").longValue());
			student.lastName = asJson.findPath("lastName").textValue();
			if (!(student.lastName.length() > 0)) {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "lastName is mandatory");
				return badRequest(httpStatus);
			}
			if(asJson.has("nida")){
				if(asJson.findPath("nida").textValue().length()>3){
					student.nida = asJson.findPath("nida").textValue();
				}
			}
			if (!(student.nida.length() > 0)) {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "nida is mandatory");
				return badRequest(httpStatus);
			}
			student.password = asJson.findPath("password").textValue();
			student.phone = asJson.findPath("phone").textValue();
			if (!(student.phone.length() > 0)) {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "phone is mandatory");
				return badRequest(httpStatus);
			}
			student.regNumber = asJson.findPath("regNumber").textValue();
			if (!(student.regNumber.length() > 0)) {
				httpStatus.put("Code", "401");
				httpStatus.put("status", "regNumber is mandatory");
				return badRequest(httpStatus);
			}
			student.stdPic = asJson.findPath("stdPic").textValue();
			student.dob = asJson.findPath("dob").textValue();
			student.sex = asJson.findPath("sex").textValue();
			student.stdClass = asJson.findPath("stdClass").textValue();
			try {
				if (asJson.has("academicProgram")) {
					AcademicProgram academicProgram = AcademicProgram.find.byId(asJson.findPath("academicProgram").longValue());
					student.academicProgram = academicProgram;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			try {
				if (asJson.has("academicYear")) {
					InstitutionCalender academicYear = InstitutionCalender.find.where().eq("academic_year", asJson.findPath("academicYear").textValue()).findUnique();
					student.academicYear = academicYear;
				} else {
					httpStatus.put("Code", "401");
					httpStatus.put("status", "academicYear is not found");
					return badRequest(httpStatus);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				httpStatus.put("Code", "401");
				httpStatus.put("status", "academicYear is not found");
				return badRequest(httpStatus);
			}
			try {
				if (asJson.has("degreeProgram")) {
					DegreeProgram degreeProgram = DegreeProgram.find.byId(asJson.findPath("degreeProgram").longValue());
					student.degreeProgram = degreeProgram;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (asJson.has("noneDegreeProgram")) {
					NoneDegreeProgram noneDegreeProgram = NoneDegreeProgram.find.byId(asJson.findPath("noneDegreeProgram").longValue());
					student.noneDegreeProgram = noneDegreeProgram;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (asJson.has("studyProgram")) {
					StudyProgramType studyProgram = StudyProgramType.find.byId(asJson.findPath("studyProgram").longValue());
					student.studyProgram = studyProgram;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// save the user account
			try {
				student.update();

				userJson.put("status", "updated");

				httpStatus.put("Code", "200");
				httpStatus.put("status", "Success");
				httpStatus.put("response", userJson);

				LOG.info("Students update with response: " + httpStatus);

				return ok(httpStatus);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				LOG.debug("Students updating failed due to form errors: " + e.getMessage());

				userJson.put("status", e.getMessage());

				httpStatus.put("Code", "401");
				httpStatus.put("status", "Bad request");
				httpStatus.put("error", e.getMessage());

				return badRequest(httpStatus);

			}

		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result deleteStudentJson() {
		MDC.put("method", "deleteStudentJson");

		ObjectNode httpStatus = Json.newObject();
		JsonNode asJson = request().body().asJson();

		if (asJson.isNull()) {
			ObjectNode userJson = Json.newObject();
			userJson.put("status", "No data");

			httpStatus.put("Code", "401");
			httpStatus.put("status", "Bad request");
			httpStatus.put("response", userJson);

			LOG.debug("Students deleting failed due to form errors: " + httpStatus);

			return badRequest(httpStatus);
		} else {
			ObjectNode userJson = Json.newObject();
			List<PaymentLog> myPurpose=PaymentLog.find.where().eq("student_id", asJson.findPath("stdId").longValue()).findList();
			if(myPurpose.size()>0){
				httpStatus.put("Code", "401");
				httpStatus.put("status", "Item has dependent records, cann't be deleted");
				return badRequest(httpStatus);
			}else{
			
			Student paymentPurpose = Student.find.byId(asJson.findPath("stdId").longValue());
			// save the user account
			try {
				paymentPurpose.delete();

				userJson.put("status", "deleted");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.debug("Students deleting failed due to form errors: " + e);
				userJson.put("status", e.getMessage());
			}

			httpStatus.put("Code", "200");
			httpStatus.put("status", "Success");
			httpStatus.put("response", userJson);

			LOG.info("Students deteting with response: " + httpStatus);

			return ok(httpStatus);
			}
		}

	}

}
