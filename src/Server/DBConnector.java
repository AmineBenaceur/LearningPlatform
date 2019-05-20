package Server;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.*;
import java.util.Random;


import Shared.*;
// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to access a database , representing a
* DB in a Learning Platform Java application.
* The overall purpose of this class is to store information and retrive it from the data base using prepared statements and queries or updates.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/

public class DBConnector {
	
	/** The jdbc connection. */
	public  static  Connection jdbc_connection;
	
	/** The statement. */
	/*
	 * statement to execute commands.
	 */
	public static  Statement statement;
	
	/** The grade table. */
	/*
	 * Database storage info.
	 */
	public static  String databaseName = "LearningPlatformDB";
	
	/** The user table. */
	public static  String  userTable = "usertable"; 
	
	/** The course table. */
	public static  String  courseTable = "coursetable";
	
	/** The student enrollment table. */
	public static  String studentEnrollmentTable="studentenrollmenttable";
	
	/** The assignment table. */
	public static  String assignmentTable="assignmenttable";
	
	/** The submission table. */
	public static  String submissionTable="submissiontable";
	
	/** The grade table. */
	public static  String gradeTable="gradetable" ;
	
	/** The submission file table. */
	public static String fileTable = "filetable";
	
	/** The submission file table. */
	public static  String submissionFileTable = "submissionfiletable";
	
	/** The login and password info for DB. */
	/*
	 * Database access info.
	 */
	public  static String connectionInfo = "jdbc:mysql://localhost:3307/",  
				  login          = "root",
				  password       = "root";
	
	/**
	 * Instantiates a new DB connector.
	 */
	public DBConnector() {
		try{
			// If this throws an error, make sure you have added the mySQL connector JAR to the project
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// If this fails make sure your connectionInfo and login/password are correct
			jdbc_connection = DriverManager.getConnection(connectionInfo, login, password);
			System.out.println("Connected to: " + connectionInfo + "\n");
		}
		catch(SQLException e) { e.printStackTrace(); }
		catch(Exception e) { e.printStackTrace(); }
		
		//create the database
		createDB();
		selectDB();
		
		//CREATE TABLES
		createUserTable();
		createAssignmentTable();
		createSubmissionTable();
		createGradeTable();
		createCourseTable();
		createStudentEnrollementTable();
		createFileTable();
		createSubmissionFileTable();
	}
	
	/**
	 * Creates the DB.
	 */
	public void createDB()
	{
		try {
			statement = jdbc_connection.createStatement();
			statement.executeUpdate("CREATE DATABASE " + databaseName);
			System.out.println("Created Database " + databaseName);
		} 
		catch( SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Select DB.
	
	 *specifies the database to operate in.
	 */
	public void selectDB() 
	{
		String sql = "USE " + databaseName;
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Wokring in: " + databaseName);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates the user table.
	 */
	public void createUserTable()
	{
		String sql = "CREATE TABLE " + userTable + "(" +
				     "FIRSTNAME VARCHAR(30)NOT NULL, " +
				     "LASTNAME VARCHAR(30) NOT NULL, " + 
				     "TYPE VARCHAR(10) NOT NULL, " + 
				     "EMAIL VARCHAR(50) NOT NULL, " + 
				     "PASSWORD VARCHAR(20) NOT NULL, " +  
				     "ID VARCHAR(8) NOT NULL, " +  
				     "PRIMARY KEY ( id ))";
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created Table " + userTable);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the file table.
	 */
	public void createFileTable()
	{
		String sql = "CREATE TABLE " + fileTable + "(" +
				     "ID VARCHAR(8)NOT NULL, " +
				     "COURSEID VARCHAR(10) NOT NULL, " + 
				     "CONTENT BLOB NOT NULL, " + 
				     "EXT VARCHAR(10) NOT NULL, " +  
				     "PRIMARY KEY ( id ))";
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created Table " + fileTable);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the submission file table.
	 */
	public void createSubmissionFileTable()
	{
		String sql = "CREATE TABLE " + submissionFileTable + "(" +
					"ID VARCHAR(8)NOT NULL, " +
				     "STUDENTID VARCHAR(8)NOT NULL, " +
				     "COURSEID VARCHAR(8) NOT NULL, " + 
				     "ASSIGNMENTID VARCHAR(8) NOT NULL, " + 
				     "CONTENT BLOB NOT NULL, " + 
				     "EXT VARCHAR(10) NOT NULL, " +  
				     "PRIMARY KEY ( id ))";
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created Table " + submissionFileTable);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Creates the assignment table.
	 */
	public void createAssignmentTable()
	{
		String sql = "CREATE TABLE " + assignmentTable + "(" +
				     "DUE VARCHAR(30)NOT NULL, " +
				     "ISACTIVE BIT(1) NOT NULL, " + 
				     "PATH VARCHAR(100) NOT NULL, " + 
				     "ASSIGNMENTNAME VARCHAR(50) NOT NULL, " + 
				     "COURSEID VARCHAR(8) NOT NULL, " +  
				     "ID VARCHAR(8) NOT NULL, " +  
				     "PRIMARY KEY ( id ))";
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created Table " + assignmentTable);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the submission table.
	 */
	public void createSubmissionTable()
	{
		String sql = "CREATE TABLE " + submissionTable + "(" +
				     "DATESUBMITTED VARCHAR(30) NOT NULL, " +
				     "GRADE CHAR(3) NOT NULL, " + 
				     "COMMENT VARCHAR(100) NOT NULL, " + 
				     "PATH VARCHAR(120) NOT NULL, " + 
				     "ASSIGNMENTNAME VARCHAR(50) NOT NULL, " + 
				     "STUDENTID VARCHAR(8) NOT NULL, " +
				     "ASSIGNMENTID VARCHAR(8) NOT NULL, " +
				     "ID VARCHAR(8) NOT NULL, " +  
				     "PRIMARY KEY ( id ))";
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created Table " + submissionTable);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates the grade table.
	 */
	public void createGradeTable()
	{
		String sql = "CREATE TABLE " + gradeTable + "(" +
				     "ASSIGNMENTGRADE CHAR(3)NOT NULL, " +
				     "STUDENTID VARCHAR(8) NOT NULL, " + 
				     "ASSIGNMENTID VARCHAR(8) NOT NULL, " + 
				     "COURSEID VARCHAR(8) NOT NULL, " +  
				     "ID VARCHAR(8) NOT NULL, " +  
				     "PRIMARY KEY ( id ))";
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created Table " + gradeTable);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates the course table.
	 */
	public void createCourseTable()
	{
		String sql = "CREATE TABLE " + courseTable + "(" +
				     "ISACTIVE BIT(1) NOT NULL, " +
				     "COURSENAME VARCHAR(50) NOT NULL, " +
				     "COURSENUMBER VARCHAR(3) NOT NULL, " +
				     "PROFESSORNAME VARCHAR(30) NOT NULL, " + 
				     "PROFESSORID VARCHAR(8) NOT NULL, " +  
				     "ID VARCHAR(8) NOT NULL, " +  
				     "PRIMARY KEY ( id ))";
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created Table " + courseTable);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the student enrollement table.
	 */
	public void createStudentEnrollementTable()
	{
		String sql = "CREATE TABLE " + studentEnrollmentTable + "(" +
				     "COURSEID VARCHAR(8) NOT NULL, " +  
				     "STUDENTID VARCHAR(8) NOT NULL, " +  
				     "ID VARCHAR(8) NOT NULL, " +  
				     "PRIMARY KEY ( id ))";
		try{
			statement = jdbc_connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Created Table " + studentEnrollmentTable);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds the user to DB.
	 *
	 * @param user the user
	 */
	public static void addUserToDB( User user) {


		
		String sql = "INSERT INTO " + userTable + " VALUES ( ?, ?, ?, ?, ?, ?);";
		try{
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				
			ps.setString(1, user.firstName);
			ps.setString(2, user.lastName);
			ps.setString(3, user.type);
			ps.setString(4, user.email );
			ps.setString(5, user.password);
			ps.setString(6, user.id);
			
			ps.executeUpdate();
		
		}
		
		
		catch(SQLException k)
		{
			k.printStackTrace();
		}
		
		
		
	}

	/**
	 * Adds the assignment to DB.
	 *
	 * @param assignment the assignment
	 */
	public static void addAssignmentToDB( Assignment assignment) {

		String sql = "INSERT INTO " + assignmentTable + " VALUES ( ?, ?, ?, ?, ?, ?);";
		try{
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				
			ps.setString(1, assignment.due);
			ps.setBoolean(2, assignment.isActive);
			ps.setString(3, assignment.path);
			ps.setString(4, assignment.assignmentName );
			ps.setString(5, assignment.courseID);
			ps.setString(6, assignment.id);
			
			ps.executeUpdate();
		
		}
		catch(SQLException k)
		{
			k.printStackTrace();
		}
	}
	
	/**
	 * Adds the submission to DB.
	 *
	 * @param sub the sub
	 */
	public static void addSubmissionToDB( Submission sub) {

		String sql = "INSERT INTO " + submissionTable + " VALUES ( ?, ?, ?, ?, ?, ?, ?,?);";
		try{
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
	
			ps.setString(1, sub.dateSubmitted);
			ps.setString(2, sub.grade);
			ps.setString(3, sub.comment);
			ps.setString(4, sub.path );
			ps.setString(5, sub.assignmentName);
			ps.setString(6, sub.studentID);
			ps.setString(7, sub.assignmentID);
			ps.setString(8, sub.id);
			
			ps.executeUpdate();
		
		}
		catch(SQLException k)
		{
			k.printStackTrace();
		}
	}
	
	/**
	 * Adds the grade to DB.
	 *
	 * @param grade the grade
	 */
	public static void addGradeToDB(Grade grade){

		String sql = "INSERT INTO " + gradeTable + " VALUES ( ?, ?, ?, ?, ?);";
		try{
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				
			ps.setString(1, grade.grade);
			ps.setString(2, grade.studentID);
			ps.setString(3, grade.assignmentID);
			ps.setString(4, grade.courseID );
			ps.setString(5, grade.id);

			
			ps.executeUpdate();
		}
		
		
		catch(SQLException k)
		{
			k.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * Adds the course to DB.
	 *
	 * @param course the course
	 */
	public static void addCourseToDB(Course course){

		String sql = "INSERT INTO " + courseTable + " VALUES ( ?, ?, ?, ?, ?, ?);";
		try{
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				
			ps.setBoolean(1, course.isActive);
			ps.setString(2, course.courseName);
			ps.setString(3, course.courseNumber);
			ps.setString(4, course.professorName);
			ps.setString(5, course.professorID);
			ps.setString(6, course.courseID );
			

			
			ps.executeUpdate();
		}
		catch(SQLException k)
		{
			k.printStackTrace();
		}
	}
	

	
	/**
	 * Adds the student enrollement to DB.
	 *
	 * @param enrollment the enrollment
	 */
	public static void addStudentEnrollementToDB( StudentEnrollment enrollment) {

		String sql = "INSERT INTO " + studentEnrollmentTable + " VALUES ( ?, ?, ?);";
		try{
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				
			ps.setString(1, enrollment.courseID);
			ps.setString(2, enrollment.studentIDtoEnroll);
			ps.setString(3, enrollment.id);

			
			ps.executeUpdate();
		
		}
		
		
		catch(SQLException k)
		{
			k.printStackTrace();
		}
	}
	
	/**
	 * Delete student enrollment.
	 *
	 * @param sid the sid
	 * @param cid the cid
	 */
	public static void deleteStudentEnrollment(String sid, String cid) {
		
		String sql = "DELETE FROM " + studentEnrollmentTable + " WHERE STUDENTID = ? AND COURSEID = ? ";
		try{
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
		ps.setString(1, sid);
		ps.setString(2, cid);
		

		
		ps.executeUpdate();
	
	}
	
	
	catch(SQLException k)
	{
		k.printStackTrace();
	}
	}
		
/**
 * Search student.
 *
 * @param by the by: true for id, false for by name. 
 * @param input the input
 * @return the student
 */
//	}
	public static Student searchStudent(String by, String input) {
		
		String sql = "";
		if ( by.equalsIgnoreCase("name")) {
			 sql = "SELECT * FROM  "+ userTable +"  WHERE LASTNAME = ?" ;
		}else if( by.equalsIgnoreCase("id")) {
			sql = "SELECT * FROM  "+ userTable +"  WHERE ID = ? " ;
		}else {
			return null;
		}
			Student found=null;
		ResultSet students = null;
		try {
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
			ps.setString(1, input.trim() );
			
			students = ps.executeQuery();
			if(students.next())
			{
				
				found = new Student(new User(students.getString("FIRSTNAME"),
									students.getString("LASTNAME"),
									students.getString("PASSWORD"),
									students.getString("EMAIL"),
									students.getString("ID")));
			}
		
		} catch (SQLException k) { k.printStackTrace(); }
		
		
		return found;
	}
	
	/**
	 * Search professor.
	 *
	 * @param by the by
	 * @param input the input
	 * @return the professor
	 */
	public static Professor searchProfessor(String by, String input) {
		
		String sql = "";
		if ( by.equalsIgnoreCase("name")) {
			 sql = "SELECT * FROM  "+ userTable +"  WHERE LASTNAME= ?  " ;
		}else if( by.equalsIgnoreCase("id")) {
			sql = "SELECT * FROM  "+ userTable +"  WHERE ID= ? " ;
		}else {
			return null;
		}
			Professor found=null;
		ResultSet students = null;
		try {
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
			ps.setString(1, input.trim() );
		
			students = ps.executeQuery();
			if(students.next())
			{
				
				found = new Professor(new User(students.getString("FIRSTNAME"),
									students.getString("LASTNAME"),
									students.getString("PASSWORD"),
									students.getString("EMAIL"),
									students.getString("ID")));
			}
		
		} catch (SQLException k) { k.printStackTrace(); }
		
		
		return found;
	}
	
	/**
	 * Search course.
	 *
	 * @param by the by
	 * @param input the input
	 * @return the course
	 */
	public static Course searchCourse(String by, String input) {
		
		String sql = "";
		if ( by.equalsIgnoreCase("courseName")) {
			 sql = "SELECT * FROM  "+ courseTable +"  WHERE COURSENAME= ? " ;
		}else if( by.equalsIgnoreCase("id")) {
			sql = "SELECT * FROM  "+ courseTable +"  WHERE ID= ? " ;
		}else {
			return null;
		}
			Course found=null;
		ResultSet courses = null;
		try {
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
			ps.setString(1, input.trim() );
			courses = ps.executeQuery();
			if(courses.next())
			{
				
				found = new Course(courses.getString("ID"),
						courses.getString("COURSENAME"),
						courses.getString("COURSENUMBER"),
						courses.getString("PROFESSORNAME"),
						courses.getString("PROFESSORID"),
						courses.getBoolean("ISACTIVE"));
			}
		
		} catch (SQLException k) { k.printStackTrace(); }
		
		
		return found;
	}
	
	/**
	 * Gets the students in course.
	 *
	 * @param cid the course ID. 
	 * @return the students in course
	 */
	public static ArrayList<Student> getStudentsInCourse(String cid) {
		ArrayList<Student> studentList = null;
		String sql = "";
		
			sql = "SELECT * FROM  "+ studentEnrollmentTable +"  WHERE COURSEID= ? " ;
	
			Student found=null;
		ResultSet students = null;
		try {
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
			ps.setString(1, cid.trim() );
			students = ps.executeQuery();
			if(!students.next()){
				System.out.println("Not found ");
				return studentList;
			}
			else{	
					studentList = new ArrayList<Student>();
					do {
						System.out.println("filling arraylist");
						found = searchStudent("ID", students.getString("STUDENTID") );
						if (found != null)
							studentList.add(found);
			
					}while(students.next());
			}
		} catch (SQLException k) { k.printStackTrace(); }
		
		
		return studentList;
	}
	
	/**
	 * Gets the students courses.
	 *
	 * @param sid the student ID. 
	 * @return the students courses
	 */
	public static ArrayList<Course> getStudentsCourses(String sid){

		ArrayList<Course> courseList = null;
		String sql = "";
		
			sql = "SELECT * FROM  "+ studentEnrollmentTable +"  WHERE STUDENTID= ? " ;
	
			Course found=null;
		ResultSet courses = null;
		try {
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
			ps.setString(1, sid.trim() );
			courses = ps.executeQuery();
			if(!courses.next()){
				courseList = null;
				return courseList;
			}
			else{	
					courseList = new ArrayList<Course>();
					do{
						found = searchCourse("ID", courses.getString("COURSEID") );
						if (found != null)
							courseList.add(found);
			
					}while(courses.next());
			}
		} catch (SQLException k) { k.printStackTrace(); }
		
		
		return courseList;
	}
	
	/**
	 * Gets the courses asssignments.
	 *
	 * @param cid the course ID. 
	 * @return the courses asssignments
	 */
	public static ArrayList<Assignment> getCoursesAsssignments(String cid) {

		ArrayList<Assignment> assignmentList = null;
		String sql = "";
		
			sql = "SELECT * FROM  "+ assignmentTable +"  WHERE COURSEID= ? " ;
	
			Assignment found=null;
		ResultSet assignments = null;
		try {
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
			ps.setString(1, cid.trim() );
			assignments = ps.executeQuery();
			if(!assignments.next()){
				assignmentList = null;
				return assignmentList;
			}
			else{	
					assignmentList = new ArrayList<Assignment>();
					do{
						found = new Assignment(assignments.getString("ID"), assignments.getString("ASSIGNMENTNAME"), assignments.getString("COURSEID"), assignments.getString("PATH"), assignments.getString("DUE"), assignments.getBoolean("ISACTIVE"));
								
						if (found != null)
							assignmentList.add(found);
			
					}while(assignments.next());
			}
		} catch (SQLException k) { k.printStackTrace(); }
		
		
		return assignmentList;
	}
	
	/**
	 * Gets the profs courses.
	 *
	 * @param pid the professor's ID. 
	 * @return the profs courses
	 */
	public static ArrayList<Course> getProfsCourses(String pid){

		ArrayList<Course> courseList = null;
		String sql = "";
		
			sql = "SELECT * FROM  "+ courseTable +"  WHERE PROFESSORID= ? " ;
	
			Course found=null;
		ResultSet courses = null;
		try {
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
			ps.setString(1, pid.trim() );
			courses = ps.executeQuery();
			if(!courses.next()){
				courseList = null;
				return courseList;
			}
			else{	
					courseList = new ArrayList<Course>();
					do{
						found = searchCourse("ID", courses.getString("ID") );
						if (found != null)
							courseList.add(found);
			
					}while(courses.next());
			}
		} catch (SQLException k) { k.printStackTrace(); }
		
		
		return courseList;
	}
	
	/**
	 * Gets the submission list.
	 *
	 * @param input the input
	 * @param by the by
	 * @return the submission list
	 */
	public static ArrayList<Submission> getSubmissionList(String input, Boolean by) {
		ArrayList<Submission> submissionList = null;
		String sql = "";
		if(by)
			sql = "SELECT * FROM  "+ submissionTable +"  WHERE ASSIGNMENTID= ? " ;
		else
			sql = "SELECT * FROM  "+ submissionTable +"  WHERE STUDENTID= ? " ;
			
			
			Submission found=null;
		ResultSet subs = null;
		try {
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
			ps.setString(1, input.trim() );
			subs = ps.executeQuery();
			if(!subs.next()){
				System.out.println("Not found ");
				return submissionList;
			}
			else{	
					submissionList = new ArrayList<Submission>();
					do {
						System.out.println("filling arraylist");
						found = new Submission(subs.getString("ID"), subs.getString("ASSIGNMENTID"), subs.getString("STUDENTID"), "10001000",  subs.getString("GRADE"), subs.getString("PATH"), subs.getString("COMMENT"), subs.getString("DATESUBMITTED")) ;
						submissionList.add(found);
			
					}while(subs.next());
			}
		} catch (SQLException k) { k.printStackTrace(); }
		
		
		return submissionList;
	}
	
	/**
	 * Activate course.
	 *
	 * @param ac the ac (true for set active, false for set non-active. 
	 * @param cid the course ID. 
	 */
	public static void activateCourse(boolean ac, String cid) {
		String sql = "UPDATE " + courseTable + " SET ISACTIVE = ? WHERE ID = ?";
		
		
		try{
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				
			ps.setBoolean(1, ac );
			ps.setString(2, cid);
			ps.executeUpdate();
		}
		catch(SQLException k)
		{
			k.printStackTrace();
		}
	}
	
	/**
	 * Update submissions.
	 *
	 * @param g the grade. 
	 */
	public static void updateSubmissions(Grade g) {
		String sql = "UPDATE " + submissionTable + " SET GRADE = ? WHERE STUDENTID = ? AND ASSIGNMENTID = ? ";
		
		try{
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			
		ps.setString(1, g.grade );
		ps.setString(2, g.studentID);
		ps.setString(3, g.assignmentID);
		
		ps.executeUpdate();
	}
	catch(SQLException k)
	{
		k.printStackTrace();
	}
	}
	
	/**
	 * Activate assignment.
	 *
	 * @param request the request
	 */
	public static void activateAssignment(Request request) {
		String sql = "UPDATE " + assignmentTable + " SET ISACTIVE = ? WHERE ID = ?";
		
		
		try{
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				
			ps.setBoolean(1, request.set );
			ps.setString(2, request.data);
			ps.executeUpdate();
		}
		catch(SQLException k)
		{
			k.printStackTrace();
		}
	}
	
	/**
	 * Upload content.
	 *
	 * @param aid the aid
	 * @param content the content
	 * @param ext the ext
	 * @param cid the cid
	 */
	public static void uploadContent(String aid, byte[] content, String ext, String cid) {
		String sql = "INSERT INTO " + fileTable +  " VALUES (? , ?, ?, ?);";
		
	 
	
		try {
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				ps.setString(1,aid);
				ps.setString(2, cid);
				ps.setBytes(3, content);
				ps.setString(4, ext);
				
				ps.executeUpdate();
				
		}catch(SQLException k)
		{
			k.printStackTrace();
		}
	}
	
	/**
	 * Upload submission content.
	 *
	 * @param id the id
	 * @param content the content
	 * @param ext the ext
	 * @param sid the sid
	 * @param aid the aid
	 * @param cid the cid
	 */
	public static void uploadSubmissionContent(String id, byte[] content, String ext, String sid, String aid, String cid) {
		String sql = "INSERT INTO " + submissionFileTable +  " VALUES (? , ?, ?, ?, ?, ?);";
		
		
	
		try {
				PreparedStatement ps = jdbc_connection.prepareStatement(sql);
				ps.setString(1,id);
				ps.setString(2, sid);
				ps.setString(3, cid);
				ps.setString(4, aid);
				ps.setBytes(5, content);
				ps.setString(6, ext);
				
				ps.executeUpdate();
				
		}catch(SQLException k)
		{
			k.printStackTrace();
		}
	}
	
	/**
	 * Gets the content.
	 *
	 * @param aid the aid
	 * @return the content
	 */
	synchronized public static byte[] getContent(String aid) {
		
		String sql = "";
		
			 sql = "SELECT * FROM "+ fileTable +" WHERE ID = ? " ;
			 Blob blob = null;
			byte[] blobAsBytes = null;
		ResultSet files = null;
		try {
			
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			System.out.println("LOOKING FOR FILE ID" + aid);
			ps.setString(1, aid);
			System.out.println("in getContent:  EXECUTING QUERY........");
			files = ps.executeQuery();
			System.out.println("in getContent: QUERY DONE");
			if(files.next())
			{
				System.out.println("FOUND ASSIGNMENT FILE CONTENT ");
				blobAsBytes = files.getBytes("CONTENT");
//				int blobLength = (int) blob.length();  
//				blobAsBytes = blob.getBytes(1, blobLength);
			}else {
				System.out.println("COULD NOT FIND ASSIGNMENT FILE CONTENT");
			}
		
		} catch (SQLException k) { k.printStackTrace(); }
		
		return blobAsBytes;
		
	}

/**
 * Gets the submission content.
 *
 * @param subID the sub ID
 * @return the submission content
 */
synchronized public static byte[] getSubmissionContent(String subID) {
		
		String sql = "";
		
			 sql = "SELECT * FROM "+ submissionFileTable +" WHERE ID = ? " ;
			 Blob blob = null;
			byte[] blobAsBytes = null;
		ResultSet files = null;
		try {
			
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			System.out.println("LOOKING FOR FILE ID" + subID);
			ps.setString(1, subID);
			System.out.println("in getContent:  EXECUTING QUERY........");
			files = ps.executeQuery();
			System.out.println("in getContent: QUERY DONE");
			if(files.next())
			{
				System.out.println("FOUND ASSIGNMENT FILE CONTENT ");
				blobAsBytes = files.getBytes("CONTENT");
//				int blobLength = (int) blob.length();  
//				blobAsBytes = blob.getBytes(1, blobLength);
			}else {
				System.out.println("COULD NOT FIND SUBMISSION FILE CONTENT");
			}
		
		} catch (SQLException k) { k.printStackTrace(); }
		
		return blobAsBytes;
		
	}

	/**
	 * Gets the content info.
	 *
	 * @param aid the aid
	 * @return the content info
	 */
	synchronized public static Request getContentInfo(String aid) {
		System.out.println("in getContent info");
		String sql = "";
		
			 sql = "SELECT * FROM  "+ fileTable +"  WHERE ID = ? " ;
			 Request r = new Request("INFO");
			
		ResultSet files = null;
		try {
			System.out.println("in getContent info: preparing query.....");
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			System.out.println("in getContent info: prepared! ");
			ps.setString(1, aid);
			System.out.println("in getContent info: ececuting query.....");
			files = ps.executeQuery();
			System.out.println("in getContent info: excecuted query");
			if(files.next())
			{
				System.out.println("FOUND ASSIGNMENT FILE INFO");
				r.setData( files.getString("EXT"), true);
				r.setFrom(aid);
			}else{
				System.out.println("COULD NOT FIND ASSIGNMENT FILE INFO ");
			}
		
		} catch (SQLException k) { k.printStackTrace(); }
		
		return r;
		
	}
	
	
	/**
	 * Gets the submission content info.
	 *
	 * @param subID the sub ID
	 * @return the submission content info
	 */
	synchronized public static Request getSubmissionContentInfo(String subID){
		System.out.println("in getSubmissionContent info");
		String sql = "";
		
			 sql = "SELECT * FROM  "+ submissionFileTable +"  WHERE ID = ? " ;
			 Request r = new Request("INFO");
			
		ResultSet files = null;
		try {
			System.out.println("in getContent info: preparing query.....");
			PreparedStatement ps = jdbc_connection.prepareStatement(sql);
			System.out.println("in getContent info: prepared! ");
			ps.setString(1, subID);
			System.out.println("in getContent info: ececuting query.....");
			files = ps.executeQuery();
			System.out.println("in getContent info: excecuted query");
			if(files.next())
			{
				System.out.println("FOUND ASSIGNMENT FILE INFO");
				r.setData( files.getString("EXT"), true);
				r.setFrom(subID);
			}else{
				System.out.println("COULD NOT Submission FILE INFO ");
			}
		
		} catch (SQLException k) { k.printStackTrace(); }
		
		return r;
		
	}
	
	/**
	 * Gets the random ID.
	 *
	 * @return the random ID
	 */
	/*
	 *Creates a unique 8 digit id.
	 *@returns: unique id
	 */
	public static String getRandomID() {
			Random ran = new Random();
			int num = ran.nextInt(9999999) + 10000000;
			String snum = Integer.toString(num).trim();
			return snum;
		}
	
	/**
	 * The main method.
	 * includes some code to pre-populate the DB. 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		DBConnector dbc = new DBConnector();
		
			//testing
			//PROFESSORS
			String rickID = "10101010";
			System.out.println("RICK id: " + rickID);
			Professor rick = new Professor(new User("Rick", "Sanchez", "ricky100", "ricksanchez@ucalgary.ca", rickID ));
			rick.type ="PROFESSOR";
			dbc.addUserToDB(rick);
			
			
			
			
			String mortyID = "11110000";
			System.out.println("MORTY id: " + mortyID);
			Professor morty = new Professor(new User("Morty", "Smith", "morty100", "mortysanchez@ucalgary.ca", mortyID ));
			morty.type ="PROFESSOR";
			dbc.addUserToDB(morty);
			
			//Students
			String peterID =  getRandomID();
			System.out.println("PETERS id: " + peterID);
			Student peter = new Student(new User("Peter", "Griffin", "peter100", "petergriffin@ucalgary.ca", peterID ));
			peter.type = "STUDENT";
			dbc.addUserToDB(peter);
			
			String glennID =  getRandomID();
			System.out.println("Glenn id: " + glennID);
			Student glenn = new Student(new User("Glenn", "Quagmire", "glenn100", "glennquagmire@ucalgary.ca", glennID ));
			glenn.type = "STUDENT";
			dbc.addUserToDB(glenn);
			
			String louisID =  getRandomID();
			System.out.println("louis id: " + louisID);
			Student louis = new Student(new User("Louis", "Griffin", "louis100", "louisgriffin@ucalgary.ca", louisID ));
			louis.type = "STUDENT";
			dbc.addUserToDB(louis);
			
			String stewieID =  getRandomID();
			System.out.println("stewie id: " + stewieID);
			Student stewie = new Student(new User("Stewie", "Griffin", "stewie100", "stewiegriffin@ucalgary.ca", stewieID ));
			stewie.type = "STUDENT";
			dbc.addUserToDB(stewie);
			
			String boratID = getRandomID();
			System.out.println("BORAT id: " + boratID);
			Student borat = new Student(new User("Borat", "Sagdiyev", "borat100", "BoratSagdiyev@ucalgary.ca", boratID ));
			borat.type = "STUDENT";
			dbc.addUserToDB(borat);
			
			String amineID = "10138295";
			Student amine = new Student(new User("Amine", "Benaceur", "amine100", "abenaceur12@gmail.com", amineID ));
			amine.type = "STUDENT";
			dbc.addUserToDB(amine);
			
			
			
			
			
			
			//COURSES
			String mathID = getRandomID();
			System.out.println("MATH id: " + mathID);
			Course math = new Course(mathID, "MATH" , "271", morty.firstName, morty.id, true  );
			dbc.addCourseToDB(math);
			
			String whiningID = getRandomID();
			System.out.println("WHINING id: " + mathID);
			Course whining = new Course(whiningID, "WHINING" , "101", morty.firstName, morty.id, true  );
			dbc.addCourseToDB(whining);
			
			String quantumID = "11745305";
			System.out.println("QUANTUM id: " + quantumID);
			Course quantum = new Course(quantumID, "Quantum Physics" , "947",rick.firstName, rick.id, true  );
			dbc.addCourseToDB(quantum);
			
			String plutID = getRandomID();
			System.out.println("PLUT id: " + plutID);
			Course plutonium = new Course(plutID, "Plutonium Harvesting" , "769", rick.firstName, rick.id, true  );
			dbc.addCourseToDB(plutonium);
			
			//Enrollments
			StudentEnrollment enrollPeterInQuantum = new StudentEnrollment(getRandomID(), peter.id, quantum.courseID);
			System.out.println("ENROLLED " + peter.firstName + " IN " + quantum.courseName);
			dbc.addStudentEnrollementToDB(enrollPeterInQuantum);
			
			StudentEnrollment enrollStewieInQuantum = new StudentEnrollment(getRandomID(), stewie.id, quantum.courseID);
			System.out.println("ENROLLED " + stewie.firstName + " IN " + quantum.courseName);
			dbc.addStudentEnrollementToDB(enrollStewieInQuantum);
			
			StudentEnrollment enrollboratInQuantum = new StudentEnrollment(getRandomID(), borat.id, quantum.courseID);
			System.out.println("ENROLLED " + borat.firstName + " IN " + quantum.courseName);
			dbc.addStudentEnrollementToDB(enrollboratInQuantum);
			
			StudentEnrollment enrollLouisInWhining = new StudentEnrollment(getRandomID(), louis.id, whining.courseID);
			System.out.println("ENROLLED " + louis.firstName + " IN " + whining.courseName);
			dbc.addStudentEnrollementToDB(enrollLouisInWhining);
			
			StudentEnrollment enrollGlenInWhining = new StudentEnrollment(getRandomID(), glenn.id, whining.courseID);
			System.out.println("ENROLLED " + glenn.firstName + " IN " + whining.courseName);
			dbc.addStudentEnrollementToDB(enrollGlenInWhining);
			
			StudentEnrollment enrollAmineInMath = new StudentEnrollment(getRandomID(), amine.id, math.courseID);
			System.out.println("ENROLLED " + amine.firstName + " IN " + math.courseName);
			dbc.addStudentEnrollementToDB(enrollAmineInMath);
			
			StudentEnrollment enrollAmineInQuantum = new StudentEnrollment(getRandomID(), amine.id, quantum.courseID);
			System.out.println("ENROLLED " + amine.firstName + " IN " + quantum.courseName);
			dbc.addStudentEnrollementToDB(enrollAmineInQuantum );
			
			StudentEnrollment enrollAmineInWhining = new StudentEnrollment(getRandomID(), amine.id, whining.courseID);
			System.out.println("ENROLLED " + amine.firstName + " IN " + whining.courseName);
			dbc.addStudentEnrollementToDB(enrollAmineInWhining);
			
			StudentEnrollment enrollLouisInQuantum = new StudentEnrollment(getRandomID(), louis.id, quantum.courseID);
			System.out.println("ENROLLED " + louis.firstName + " IN " + quantum.courseName);
			dbc.addStudentEnrollementToDB(enrollLouisInQuantum);
			
			
			
		//TESTING 
		User found;
		Course coursefound;
		System.out.println("TESTING: GET USER FOR AMINE " );
		found = dbc.searchStudent("id", "10138295");
		System.out.println(" result: "+ found.toString() );
		
		System.out.println("TESTING: GET COURSE FOR AMINE " );
		coursefound = dbc.searchCourse("id", "11745305");
		System.out.println(" result: "+ coursefound.courseName + " " + coursefound.courseNumber );
		
		System.out.println("TESTING: GET STUDENTS IN COURSE FOR QUANTUM " );
		ArrayList<Student> students = dbc.getStudentsInCourse("11745305");
		Iterator<Student> itr =  students.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next().toString());
		}
		
		System.out.println("TESTING: GET Courses for students for AMINE " );
		ArrayList<Course> courses = dbc.getStudentsCourses("10138295");
		Iterator<Course> itr1 =  courses.iterator();
		while(itr1.hasNext()) {
			System.out.println(itr1.next().toString());
			
			String pEvil = "11001100";
			System.out.println("pevil id: " + pEvil);
			Professor pboi = new Professor(new User("Professor", "Evil", "pass", "abenaceur@live.ca", pEvil ));
			pboi.type ="PROFESSOR";
			dbc.addUserToDB(pboi);
		}
		
//		
		
	}
}
