package Shared;
import java.io.*;
// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* Course in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server of the final project
* of ENSF 409.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Course implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3459758044323257222L;
	
	/** The course ID. */
	public String courseID;
	
	/** The course name. */
	public String courseName;
	
	/** The course number. */
	public String courseNumber;
	
	/** The professor name. */
	public String professorName;
	
	/** The professor ID. */
	public String professorID;
	
	/** The is active. */
	public Boolean isActive;
	
	/**
	 * Instantiates a new course.
	 *
	 * @param cid the course ID. 
	 * @param cname the course name. 
	 * @param cnumber the course number. 
	 * @param pname the professors name. 
	 * @param pid the professor id
	 * @param isA weather the assingment is active or not. 
	 */
	public Course(String cid, String cname, String cnumber, String pname, String pid, Boolean isA) {
		courseID = cid;
		courseName = cname;
		courseNumber = cnumber;
		professorName = pname;
		professorID = pid;
		isActive= isA;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String act = "";
		if(isActive) 
			act = "ACTIVE";
		else
			act="NON-ACTIVE";
		return "COURSE:  " + courseName  + courseNumber + "\t\t"+ " PROFESSOR:  " + professorName + "\t\t" +  " STATUS:  " + act;
	}

}
