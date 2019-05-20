package client;

import java.io.*;
import java.net.*;
import java.util.*;
import Shared.*;

// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
* Client connecting a Learning Platform Java application.
* The overall purpose of this class is to use various GUI components to display various information to the user and use 
* the methods within ClientController to communicate with the server .  
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Client {
	
	/** The keyboard : to read user input.   */
	public static Scanner keyboard;
	
	/** The s. Socket for connecting streams to server. */
	public static Socket s;
	
	/** The portnum. port number to listen on.  */
	public static int portnum = 3307 ;
	
	/** The obj out. An object output stream for client server communication.  */
	public static ObjectOutputStream objOut;
	
	/** The obj in. An object input stream for client server communication. */
	public static ObjectInputStream objIn;
	
	/** The server. name of the server running the application.  */
	public static String server = "localhost";
	
	/** The user. User to be logged in.  */
	public static User user;

	/**
	 * Instantiates a new client.
	 *
	 * @param number the number
	 */
	Client(int number){
		portnum = number;
		try{
			keyboard = new Scanner(System.in);
			s = new Socket( server, portnum);
			objIn = new ObjectInputStream(s.getInputStream());
	        objOut =  new ObjectOutputStream(s.getOutputStream());
	        System.out.println("COnnected");

		} catch (IOException e){
			System.out.println("Error initializing client " + e.getMessage());
			System.exit(1);
		}

	}
	
	/**
	 * Communicate.
	 */
	public static void Communicate() {
		ClientController.setStreams(objIn, objOut, keyboard);

		}
	
	/**
	 * Sets the user.
	 *
	 * @param u the new user
	 */
	public static void setUser(User u) {
		user = u;
	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Client client = new Client(3307);
		ClientController.setStreams(objIn, objOut, keyboard);
		LoginView view = new LoginView();

		
	}


}
