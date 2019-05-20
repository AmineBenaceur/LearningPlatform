package Shared;

import java.io.Serializable;
// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* Request in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server, and to be used as request to communicate messages for communication.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Request implements Serializable {
	
	/** The message. */
	public String message;
	
	/** The data. */
	public String data;
	
	/** The from. */
	public String from;
	
	/** The length. */
	public int length;
	
	/** The set. used to communcate information.  */
	public Boolean set;
	
	/** The assignment id. */
	public String aid;
	
	/** The student id. */
	public String sid;
	
	/** Serial UID for socket communication. */
	private static final long serialVersionUID = 7518510684887037567L;

	/**
	 * Instantiates a new request.
	 *
	 * @param m the message of the request. 
	 */
	public Request(String m ) {
		message = new String(m);
	}

	/**
	 * Sets the data.
	 *
	 * @param d the d
	 * @param s the s
	 */
	public void setData(String d, Boolean s) {
		data = d;
		set = s;
	}
	
	/**
	 * Sets the from.
	 *
	 * @param f the new from
	 */
	public void setFrom(String f) {
		from = f;
	}
	
	/**
	 * Sets the length.
	 *
	 * @param l the new length
	 */
	public void setLength(int l) {
		length = l;
	}
	
	/**
	 * Sets the ID.
	 *
	 * @param s the s
	 * @param a the a
	 */
	public void setID(String s, String a ) {
		aid =a;
		sid=s;
	}
}
