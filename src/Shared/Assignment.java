package Shared;
import java.io.*;

// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* Assignment in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Assignment implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4233686001423718551L;
	
	/** The id. */
	public String id;
	
	/** The assignment name. */
	public String assignmentName;
	
	/** The course ID. */
	public String courseID;
	
	/** The path. */
	public String path;
	
	/** The due. */
	public String due;
	
	/** The is active. */
	public Boolean isActive;
	
	/**
	 * Instantiates a new assignment.
	 *
	 * @param i the id
	 * @param an the assignment name.
	 * @param cID the course ID
	 * @param p the path.
	 * @param d when the assignment is due.
	 * @param ac weather the assignment is active(true) or not(false). 
	 */
	public Assignment(String i, String an, String cID, String p, String d, boolean ac) {
		id = i;
		assignmentName = an;
		courseID= cID;
		path=p;
		due=d;
		isActive=ac;
	}
	
	/* (non-Javadoc)
	 * returns a string of the object. 
	 */
	public String toString() {
		String stat = "";
		if(isActive)
			stat = "ACTIVE";
		else
			stat = "NON-ACTIVE ";
		return id + "\t\t" + assignmentName + "\t\t" + "DUE: " + due + "\t\t"+ "STATUS: " +  stat;
	}
}
