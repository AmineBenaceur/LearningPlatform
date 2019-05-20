package Shared;
import java.io.*;
// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* Login Information in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server, and to be used to grant access to users.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class LoginInfo implements Serializable{

	/** serial UIUD for the object streams. */
	private static final long serialVersionUID = 5062947749801192679L;
	
	/** The user ID. */
	public String userID;
	
	/** The password. */
	public String password;
	
	/** The type. */
	public String type;
	
	/**
	 * Instantiates a new login info.
	 *
	 * @param u the user
	 * @param p the password
	 * @param t the type of user. 
	 */
	public LoginInfo(String u, String p, String t) {
		userID=u;
		password = p;
		type =t;
	}
}
