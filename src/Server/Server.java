package Server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;


/**
* Provides data fields and methods to create a Java data-type, representing a
* Server running a Learning Platform Java application.
* The overall purpose of this class is to connect to clients, and launch a runnable learning session for each client from the thread pool.  
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class Server {
	/**
	* a scanner to read input from the user's keyboard.
	*/
	static Scanner keyboard;
	/**
	* an id incremented to keep track of how many games started.
	*/
	static int id;
	
	/**
	* an integer number to store the portnumber of the socket.
	*/
	public static int portnum;
	/**
	*  the name of the server.
	*/
	public static String server = "localhost";
	
	/**
	* boolean for when to shutdown the server. 
	*/
	public static boolean shutdown = false;
	/**
	* sockets to communcate with client.
	*/
	public static Socket s;
	/**
	* server socket to  accept sockets.
	*/
	public static ServerSocket ss;

	/**
	* object output stram to send objects and request messages . 
	*/
	public static ObjectOutputStream objOut;
	
	/** The object input streams to send objects and request messages. */
	public static ObjectInputStream objIn;
	
	/** The dbc. */
	public static DBConnector dbc;

	/**
	 * Instantiates a new server.
	 *
	 * @param number the number
	 */
	public Server(int number){
		portnum = number;
		try{	
			ss = new ServerSocket(portnum);
			dbc = new DBConnector();

		}catch(IOException e){
			System.out.println("ERROR Constructing server");
		}

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		System.out.println("SERVER RUNNNING...");

		ExecutorService pool = Executors.newFixedThreadPool(10);
		Server server = new Server(3307);

		while(!shutdown){
			LearningSession ls = null;
			Thread t = null;

			try{
			s = ss.accept();
		
			 objOut =  new ObjectOutputStream(s.getOutputStream());
			objIn = new ObjectInputStream(s.getInputStream());
	       
			}catch(IOException e ){
				e.printStackTrace();
			}
			
			ls = new LearningSession(objIn,objOut,dbc);
			t = new Thread(ls);
			pool.submit(t);
			id++;
			System.out.println("Learning session # " + id + " STARTED...");

			
		}
		pool.shutdown();
		System.out.println("SHUTTTING DOWN...");
	}
}
