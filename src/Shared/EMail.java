package Shared;
import java.io.*;
// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* EMAIL in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server, and to be used to send emails.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class EMail implements Serializable{

	/** serial UIUD for the object streams. */
	private static final long serialVersionUID = 8337985095440766249L;
	
	/** The id. */
	String id;
	
	/** The from user ID. */
	String fromID;
	
	/** The to user ID. */
	String toID;
	
	/** The subject. */
	String subject;
	
	/** The message. */
	String message;
	
	/**
	 * Instantiates a new e mail.
	 *
	 * @param i the ID. 
	 * @param fID the from user ID. 
	 * @param tID the to user ID. 
	 * @param s the subject. 
	 * @param m the message.
	 */
	public EMail(String i,String fID,String tID,String s, String m) {
		id=i;
		fromID=fID;
		toID=tID;
		subject=s;
		message=m;
	}

}
