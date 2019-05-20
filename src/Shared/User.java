package Shared;
import java.io.*;

// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* User in a Java application.
* The overall purpose of this class exercise is to build useful objects for a Learning platform built for students and professors for 
* the final project of ENSF409.
*
* @author M.Amouzai, A.Benaceur
* @version 1.0
* @since March 31st 2018
*/

public class User implements Serializable{

	/** serial UIUD for the object streams. */
	private static final long serialVersionUID = 8950799104979937358L;
	
	/** A user's first Name. */
	public String firstName;
	
	/** A user's last name. */
	public String lastName;
	
	/** A user's password. */
	public String password;
	
	/** A user's email adress. */
	public String email;
	
	/** A user's id. */
	public String id;
	
	/** Type of User, either "STUDENT" or "PROFESSOR". */
	public String type;

	/**
	 * SerialVersionUID for sending and receiving the object through object streams.
	 *
	 * @param fn the fn
	 * @param ln the ln
	 * @param p the p
	 * @param em the em
	 * @param i the i
	 */
	
	public User(String fn, String ln, String p, String em, String i) {
		firstName = fn;
		lastName = ln;
		password = p;
		email = em;
		id =i;
	}
	
	/**
	 * Instantiates a new user.
	 *
	 * @param fn the first name. 
	 * @param ln the last name. 
	 * @param p the password. 
	 * @param em the email of the student. 
	 * @param i the ID. 
	 * @param t the type. 
	 */
	public User(String fn, String ln, String p, String em, String i, String t) {
		firstName = fn;
		lastName = ln;
		password = p;
		email = em;
		id =i;
		type =t;
	}

}
