package Shared;
import java.io.*;

/**
* Provides data fields and methods to create a Java data-type, representing a
* Grade in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server, and to be used to Grade assignments by the professor user.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Grade implements Serializable {
	

	/**
	 * serial UIUD for the object streams
	 */
	private static final long serialVersionUID = -7265809570862696927L;
	public String id;
	public String courseID;
	public String assignmentID;
	public String studentID;
	public String grade;
	
	public Grade(String i,String cID, String aID, String sID, String g) {
		id=i;
		courseID=cID;
		assignmentID=aID;
		studentID=sID;
		grade=g;
	}

}
