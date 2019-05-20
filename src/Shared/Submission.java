package Shared;
import java.io.*;

// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* Submission in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server, and to used
* to store information about assignments that are submitted by students in courses.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Submission implements Serializable {

		/** serial UIUD for the object streams. */
	private static final long serialVersionUID = 1L;
	
	/** The assignment ID. */
	public String assignmentID;
	
	/** The assignment name. */
	public String assignmentName;
	
	/** The student ID. */
	public String studentID;
	
	/** The course ID. */
	public String courseID;
	
	/** The grade. */
	public String grade;
	
	/** The path. */
	public String path;
	
	/** The comment. */
	public String comment;
	
	/** The date submitted. */
	public String dateSubmitted;
	
	/** The id. */
	public String id;
		
		/**
		 * Instantiates a new submission.
		 *
		 * @param i the iD.
		 * @param aid the assignment id
		 * @param sid the student ID. 
		 * @param cid the course ID. 
		 * @param gr the grade. 
		 * @param p the path. 
		 * @param com the comment on the submission. 
		 * @param date the date submitted. 
		 */
		public Submission(String i, String aid, String sid, String cid, String gr, String p, String com, String date) {
			id = i;
			assignmentID = aid;
			studentID = sid;
			courseID= cid;
			grade = gr;
			path = p;
			comment = com;
			dateSubmitted = date;
		}
	   
   	/* (non-Javadoc)
   	 * @return a String of the submission Object. 
   	 */
   	public String toString() {
		   return "     STUDNET ID: " +  studentID + "\t\t" + "ASSIGNMENT ID: " + assignmentID + "\t\t" + "GRADE: " +grade + "\t\t" + "DATE SUBMITTED: " +dateSubmitted + "     ";
	   }
}
