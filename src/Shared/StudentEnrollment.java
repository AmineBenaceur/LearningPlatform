package Shared;
import java.io.*;

// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* StudentEnrollment in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server, and to be used to keep track of
* which students are enrolled in which courses.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class StudentEnrollment implements Serializable{

	/** serial UIUD for the object streams. */
	private static final long serialVersionUID = -2702492842657164834L;
	
	/** The id. */
	public String id;
	
	/** The student I dto enroll. */
	public String studentIDtoEnroll;
	
	/** The course ID. */
	public String courseID;
	
	/**
	 * Instantiates a new student enrollment.
	 *
	 * @param id the id of the enrollment object. 
	 * @param ste the student to enroll's ID. 
	 * @param cID the course ID.
	 */
	public StudentEnrollment(String id, String ste, String cID) {
		this.id=id;
		studentIDtoEnroll=ste;
		courseID=cID;
	}
}
