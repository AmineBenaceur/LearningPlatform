package Shared;
// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* Student in a Learning Platform Java application.
* The overall purpose of this class is to store information to display and send between the client and server, and to be used to store user info.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Student extends User{
	
	/** serial UIUD for the object streams. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new student.
	 *
	 * @param u the u
	 */
	public Student(User u) {
		super(u.firstName, u.lastName, u.password, u.email, u.id);
		this.type = "STUDENT";
	}
	
	/* (non-Javadoc)
	 * returns a string of the student object. 
	 */
	public String toString() {
		return "ID: "+ this.id + "\t\t" + "NAME: " + this.firstName + " " + this.lastName + "\t\t"+ "EMAIL: " + this.email ;
	}
}
