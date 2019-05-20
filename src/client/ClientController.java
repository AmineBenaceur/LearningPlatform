package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.*;
import javax.swing.JFrame;

import Shared.*;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientController.
 */
public class ClientController {
	
	/** The user that logged in.  */
	public static User user;
	
	/** The login view. */
	public static JFrame loginView;
	
	/** The gui. */
	public static JFrame gui;
	
	/** The course view. */
	public static JFrame courseView;
	
	/** The assignment view. */
	public static JFrame assignmentView;
	
	/** The obj out. used to send objects to the server.  */
	public static ObjectOutputStream objOut;
	
	/** The obj in. Used to read Objects from the server.  */
	public static ObjectInputStream objIn;
	
	/** The keyboard. used to Read user input.  */
	public static Scanner keyboard;
	
	
	/** The app email. used for sending emails.  */
	public static String appEmail = "learningplatformapp409@gmail.com";
	
	/** The app email pass. used for sending emails.  */
	public static String appEmailPass = "BobtheBuilder";
	
	/**
	 * Sets the streams.
	 *
	 * @param obi the obiect input stream. 
	 * @param obo the object output stream. 
	 * @param k the keyboard stream. 
	 */
	public static void setStreams(ObjectInputStream obi, ObjectOutputStream obo, Scanner k) {
		objIn=obi;
		objOut=obo;
		keyboard = k;
	
		
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
	 * Gets the user.
	 *
	 * @return the user
	 */
	public static User getUser() {
		return user;
	}
	
	/**
	 * Sets the login view.
	 *
	 * @param view the new login view
	 */
	public static void  setLoginView(JFrame view) {
		loginView = view;
	}
	
	/**
	 * Sets the gui. 
	 *
	 * @param view the new gui. 
	 */
	public static void  setGUI(JFrame view) {
		gui = view;
	}
	
	/**
	 * Sets the course view.
	 *
	 * @param view the new course view
	 */
	public static void setCourseView(JFrame view) {
		courseView = view;
	}
	
	/**
	 * Sets the assignment view.
	 *
	 * @param view the new assignment view
	 */
	public static void  setAssignmentView(JFrame view) {
		assignmentView = view;
	}

	/**
	 * Send login info.
	 *
	 * @param u the username. 
	 * @param p the password. 
	 * @param t the type of user to be logged in. 
	 * @return true, if successful
	 */
	public static boolean sendLoginInfo( String u, String p , String t) {
		try {
			String stu ="STUDENT";
			String pro = "PROFESSOR";
			LoginInfo info = new LoginInfo(u,p, t);
			objOut.writeObject(info);
			objOut.flush();
			Object object = (Object)  objIn.readObject();
			
			System.out.println("login info sent");
			if(object instanceof Request || object == null){
				System.out.println("Login failed, try again");
				return false;
			}
			User us = (User) object;
			
			if(us.type.equalsIgnoreCase(stu) &&  t.equalsIgnoreCase(stu)) {
				Student returned = (Student) object;
				
				System.out.println("Student login info returned");
				if(returned != null && us.type.equalsIgnoreCase(stu)) {
					System.out.println(returned.toString());
					user = (User ) returned;
					Client.setUser((User) returned);
					ClientController.setUser((User)returned);
					ClientController.setGUI( new StudentGUI(returned));
					return true;
				}else {
					return false;
				}
			}else if(us.type.equalsIgnoreCase(pro) && t.equalsIgnoreCase(pro)) {
				Professor returned = (Professor) object;
				System.out.println("Student login info returned");
				if(returned != null && us.type.equalsIgnoreCase(pro) ) {
					System.out.println(returned.toString());
					user = (User )returned;
					Client.setUser(returned);
					ClientController.setUser(returned);
					ClientController.setGUI(new ProfessorGUI(returned));
					return true;
				}else {
					return false;
				}
			}
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Gets the random 8-digit ID.
	 *
	 * @return the random ID
	 */
	public static String getRandomID() {
		Random ran = new Random();
		int num = ran.nextInt(9999999) + 10000000;
		String snum = Integer.toString(num).trim();
		return snum;
	}
	
	/**
	 * Sends course to server.
	 *
	 * @param c the c
	 */
	public static void sendCourse(Course c) {
		try {
			objOut.writeObject(c);
			objOut.flush();
			System.out.println(" New Course sent to server. ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets the course list.
	 *
	 * @return the course list
	 */
	public static ArrayList<Course> getCourseList() {
		ArrayList<Course> returned = null;
		try{
			String r = new String("GETCOURSELIST");
			Request request = new Request(r);
			System.out.println("courses for student request sent");
			objOut.writeObject(request);
			objOut.flush();
			System.out.println("Get Courses request sent");
			returned = (ArrayList<Course>) objIn.readObject();
			System.out.println("Courses returned");
			if(returned != null) {
				System.out.println("Successfully recieved Course list from server.");
				System.out.println(returned.toString());
			}
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return returned;
	}
	
	/**
	 * Gets the student.
	 *
	 * @param info the info to be searched for
	 * @param by the type of info to be searched for. True means by ID and false means by lastname.
	 * @return the student
	 */
	// by id =true
	public static Student getStudent(String info, Boolean by) {
		Request request = new Request(new String("GETSTUDENT"));
		request.setData(info, by);
		Object o=null;
		try {
			objOut.writeObject(request);
			objOut.flush();
			o = (Object) objIn.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( o instanceof Student ) {
			System.out.println("STUDENT SUCCESSFULY RETURNED");
			return (Student) o;
			
			}
		else {
			System.out.println("STUDENT NOT FOUND, NOTHING RETURNED");
			return null;
			}
	}
	
	/**
	 * Gets the professor.
	 *
	 * @param info the info
	 * @param by the by
	 * @return the professor
	 */
	public static Professor getProfessor(String info, Boolean by) {
		Request request = new Request(new String("GETPROFESSOR"));
		request.setData(info, by);
		Object o=null;
		try {
			objOut.writeObject(request);
			objOut.flush();
			o = (Object) objIn.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( o instanceof Professor ) {
			System.out.println("PROFESSOR SUCCESSFULY RETURNED");
			return (Professor) o;
			
			}
		else {
			System.out.println("PROFESSOR NOT FOUND, NOTHING RETURNED");
			return null;
			}
	}

	/**
	 * Gets the all students in.
	 *
	 * @param cid the cid
	 * @return the all students in
	 */
	public static ArrayList<Student> getAllStudentsIn(String cid){
		ArrayList<Student> returned = null;
		Object o = null;
		try{
			String r = new String("GETSTUDENTLIST");
			Request request = new Request(r);
			request.setData(cid, true);
			System.out.println("get students in course request sent");
			objOut.writeObject(request);
			objOut.flush();
			o = (Object) objIn.readObject();
			
		
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (o instanceof ArrayList<?>) {
			returned = (ArrayList<Student>) o;
			System.out.println("Courses returned");
			return returned;
		}else {
			return null;
		}
	}
	
	/**
	 * Gets the assignments for.
	 *
	 * @param cid the cid
	 * @return the assignments for
	 */
	public static ArrayList<Assignment> getAssignmentsFor(String cid){
		ArrayList<Assignment> returned = null;
		String r = new String("GETASSIGNMENTLIST");
		Request request = new Request(r);
				
				Object o = null;
		try{
			request.setData(cid, true);
			System.out.println("get Assignment request sent");
			objOut.writeObject(request);
			objOut.flush();
			o = (Object) objIn.readObject();
			
		
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (o instanceof ArrayList<?>) {
			returned = (ArrayList<Assignment>) o;
			System.out.println("Assignment returned");
			return returned;
		}else {
			return null;
		}
	}
	
	/**
	 * Enroll student.
	 *
	 * @param id the id
	 * @param from the from
	 * @param enroll the enroll
	 */
	public static  void enrollStudent(String id, String from, Boolean enroll) {
		String str = "ENROLL";
		Request request = new Request(str);
		request.setData(id,enroll);
		request.setFrom(from);
		
		try {
			objOut.writeObject(request);
			objOut.flush();
			System.out.println("ENROLL/UNENROLL REQUEST SENT");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Upload assignment.
	 *
	 * @param a the assignment
	 * @param selectedFile the selected file
	 * @param ext the extension of the file. 
	 */
	public static void uploadAssignment(Assignment a, File selectedFile, String ext ) {
		//send assignment
		Object o = null;
		byte[] content;
		long length = selectedFile.length();
		content = new byte[(int) length]; // Converting Long to Int
		int checkLength = content.length;
		
		try {
		FileInputStream fis = new FileInputStream(selectedFile);
		BufferedInputStream bos = new BufferedInputStream(fis);
		bos.read(content, 0, (int)length);
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch(IOException e){
		e.printStackTrace();
		}//end read file 
		
		try {
			objOut.writeObject(a);
			objOut.flush();
			o = objIn.readObject();
			
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (o instanceof Request ) {
			Request request = (Request) o;
			if(request.message.equalsIgnoreCase("SEND")) {//READ FILE
				System.out.println("FREE TO SEND CONFIRMATION RECIEVED.");
				
				//send extention
				String r = "EXT";
				request = new Request(r);
				request.setData(ext, true);
				request.setFrom(a.courseID);
				request.setLength(checkLength);
				//SEND FILE
				try{
					objOut.writeObject(request);
					objOut.flush();
					System.out.println("file info sent to server.");
					objOut.writeObject(content);
					objOut.flush();
					System.out.println("File sent to server.");
					} catch(IOException e){
					e.printStackTrace();
					}
				//done
			}
		}
	}
		
		/**
		 * Upload submission.
		 *
		 * @param s the submission.
		 * @param selectedFile the selected file.
		 * @param ext the extension.
		 */
		public static void uploadSubmission(Submission s, File selectedFile, String ext) {
			//send assignment
			Object o = null;
			byte[] content;
			long length = selectedFile.length();
			content = new byte[(int) length]; // Converting Long to Int
			int checkLength = content.length;
			
			try {
			FileInputStream fis = new FileInputStream(selectedFile);
			BufferedInputStream bos = new BufferedInputStream(fis);
			bos.read(content, 0, (int)length);
			} catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch(IOException e){
			e.printStackTrace();
			}//end read file 
			
			try {
				objOut.writeObject(s);
				objOut.flush();
				o = objIn.readObject();
				
			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//readRequest
			if (o instanceof Request ) {
				Request request = (Request) o;
				if(request.message.equalsIgnoreCase("SEND")) {//READ FILE
					System.out.println("FREE TO SEND CONFIRMATION RECIEVED.");
					
					//send extention
					String r = "EXT";
					request = new Request(r);
					request.setData(ext, true);
					request.setFrom(s.courseID);
					request.setLength(checkLength);
					request.setID(s.studentID, s.assignmentID);
					//SEND FILE
					try{
						objOut.writeObject(request);
						objOut.flush();
						System.out.println("file info sent to server.");
						objOut.writeObject(content);
						objOut.flush();
						System.out.println("File sent to server.");
						} catch(IOException e){
						e.printStackTrace();
						}
					//done
				}
			}
		
		}	
		
		/**
		 * Download submission.
		 *
		 * @param s the submission.
		 * @param dir the directory to be downloaded to.
		 */
		public static void downloadSubmission(Submission s, File dir) {
			Request request = new Request("DOWNLOADSUB");
			Object o,ob = null;
			Request info = null;
			byte[] content = null;
			try {
				request.setData(s.id, true);
				objOut.writeObject(request);//send request
				objOut.flush();
				System.out.println("Download request Sent ");
				o = objIn.readObject();
				System.out.println("Reading info... ");
				if ( o instanceof Request ) {
					info = (Request) o;
					if(info.message.equalsIgnoreCase("INFO") && info != null) {
					System.out.println("Content info successfully recieved");
					ob = objIn.readObject();
					if(ob instanceof byte[] && ob !=null) {
						content = (byte[]) ob;
						if (content.length == info.length ) {
							System.out.println("Content data successfully recieved");
							ClientController.saveFile(content, dir, s.assignmentID, request.data);
						}else {
							System.out.println("Submission content is corrupted");
						}
					}
					}else {
						System.out.println("content Info Request is null or wrong");
					}
				}else {
					System.out.println("Failed to read request from stream");
				}
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		/**
		 * Gets the submissions. an arraylist of submissions.
		 *
		 * @param input the input
		 * @param by the by, either true meaning by assignment ID or false meaning by course ID.
		 * @return the submissions
		 */
		public static ArrayList<Submission> getSubmissions(String input, Boolean by){
			Request r = null;
			ArrayList<Submission> subs = null;
			r = new Request("GETSUBMISSIONS");
			r.setData(input, by);
			Object o = null;
			try {
				objOut.writeObject(r);
				objOut.flush();
				o = objIn.readObject();
				if(o instanceof Request ) {
					return subs;
				}else {
					subs = (ArrayList<Submission>) o;
					System.out.println("SUBMISSIONS RETURNED");
				}
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return subs;
		}
		
		
		
	
	
	/**
	 * Save file.
	 *
	 * @param content the content
	 * @param dir the dir
	 * @param name the name
	 * @param ext the ext
	 */
	public static void saveFile(byte[] content,File dir, String name, String ext) {
		File newFile = new File( dir.toString() + "/" + name + ext);
		try{
		if(! newFile.exists())
		newFile.createNewFile();
		FileOutputStream writer = new FileOutputStream(newFile);
		BufferedOutputStream bos = new BufferedOutputStream(writer);
		bos.write(content);
		bos.close();
		} catch(IOException e){
		e.printStackTrace();
		}
	}
	
	/**
	 * Download assignment.
	 *
	 * @param a the assignment.
	 * @param dir the chosen directory.
	 */
	public static void downloadAssignment(Assignment a, File dir) {
		Request request = new Request("DOWNLOAD");
		Object o,ob = null;
		Request info = null;
		byte[] content = null;
		try {
			request.setData(a.id, true);
			objOut.writeObject(request);//send request
			objOut.flush();
			System.out.println("Download request Sent ");
			o = objIn.readObject();
			System.out.println("Reading info... ");
			if ( o instanceof Request ) {
				info = (Request) o;
				if(info.message.equalsIgnoreCase("INFO") && info != null) {
				System.out.println("Content info successfully recieved");
				ob = objIn.readObject();
				if(ob instanceof byte[] && ob !=null) {
					content = (byte[]) ob;
					if (content.length == info.length ) {
						System.out.println("Content data successfully recieved");
						ClientController.saveFile(content, dir, a.assignmentName, request.data);
					}else {
						System.out.println("Assignment content is corrupted");
					}
				}
				}else {
					System.out.println("content Info Request is null or wrong");
				}
			}else {
				System.out.println("Failed to read request from stream");
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}


	

