package Shared;
// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* User in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server, and to be used to store user info.
*This class extends class User.
* 
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Professor extends User{
	
	/** serial UIUD for the object streams. */
	private static final long serialVersionUID = 4750933373523485143L;




	/**
	 * Instantiates a new professor.
	 *
	 * @param u the u
	 */
	public Professor(User u) {
		super(u.firstName, u.lastName, u.password, u.email, u.id);
		this.type = "PROFESSOR";
	}
	
	/* (non-Javadoc)
	 * returns a string of the professor object. 
	 */
	public String toString() {
		return "ID: "+ this.id +"\t" + "NAME: " + this.firstName + " " + this.lastName + "\t" + "EMAIL: " + this.email ;
	}

}
