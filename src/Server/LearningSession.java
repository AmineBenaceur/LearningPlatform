package Server;
import Shared.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.*;
// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java data-type, representing a
*  runnable learning session thread a Learning Platform Java application.
* The overall purpose of this class is to respond to various client request to such as handling request objects, sending course lists, student lists
* and using the DBConnector class to Retrieve objects from the database to send to students.  
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class LearningSession implements Runnable{
	
	/** The user. */
	public static User user;
	
	/** The obj output stream to communicate with client.  */
	public static ObjectOutputStream objOut;
	
	/** The obj input stream to communicate with client. */
	public static ObjectInputStream objIn;
	
	
	/** The shutdown. */
	public static boolean shutdown;
	
	/** The dbc. database Connector with methods for accessing and retriving/storing information in db. */
	public static DBConnector dbc;

	/**
	 * Instantiates a new learning session.
	 *
	 * @param obi the obiect input stream. 
	 * @param obo the object output stream.
	 * @param db the db
	 */
	public LearningSession(ObjectInputStream obi,ObjectOutputStream obo, DBConnector db) {
		objOut=obo;
		objIn =obi;
		dbc = db;
	}
	
	/* (non-Javadoc)
	 * the run method for a learning session. 
	 */
	public void run(){
			communicate();		
		
	}
	
	/**
	 * Communicate, a function that reads objects  handles requests from the client and responds to the messages accordingly. 
	 */
	@SuppressWarnings("unused")
	public void communicate() {
		System.out.println("Communicating with client");
		
		System.out.println("waiting for login info...");
		
		boolean success = false;

		Object object = null;
		while(true) {
			try {
			object = (Object) objIn.readObject();
			}catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		
				if (object instanceof LoginInfo ) {//Handles loginInfo requests
					LoginInfo temp= (LoginInfo) object;
					if(temp == null ) {
						System.out.println("Recieved LoginInfo is null");
						continue;
					}
					System.out.println("LOGIN request recieved successfuly.");
						success = validateInfo(temp);
						if (success) {
							System.out.println(user.firstName + " Has successfully logged in to the session. ");
						}else {
							System.out.println("login was not successful");
							continue;
						}
				}if (object instanceof Request ) {//HANDLES GET REQUESTS
						Request request = (Request) object;
						System.out.println("Get course request recieved successfuly.");
						if(request == null ) {
							System.out.println("Request recieved is NULL");
							continue;
							} else if(request.message.equalsIgnoreCase(new String("quit"))){
								break;
							}else {
							handleRequest(request); 
							
						
				}
				} if (object instanceof Course ) {
					Course c = (Course) object;
					if(c == null ) {
						System.out.println("Course recieved is NULL");
						continue;
					}else {
						System.out.println("Course recieved SUCCESSFULY");
						dbc.addCourseToDB(c);
						System.out.println("ADDED COURSE: " + c.toString());
					}
				}if (object instanceof Assignment ) {
					Assignment a = (Assignment) object;
					if(a == null ) {
						System.out.println("Assignment recieved is NULL");
						continue;
					}else {
						System.out.println("Assignment recieved SUCCESSFULY");
						DBConnector.addAssignmentToDB(a);
						String r = "SEND";
						Request request = new Request(r);
						try {//send confirmation
							objOut.writeObject(request);
							objOut.flush();
							Request extReq = (Request) objIn.readObject();
							byte[] content = (byte[])objIn.readObject();
							if (content != null && extReq != null) {
								if(content.length == extReq.length ) {
								DBConnector.uploadContent(a.id, content, extReq.data.trim(), extReq.from.trim());
								System.out.println("FILE TRANSFER SUCCESSFUL");
								}else {System.out.println("FILE IS CORRUPTED -- FAILED."); }
							}else {
								System.out.println("FILE TRANSFER FAILED.");
							}
						} catch (IOException | ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("ADDED Assignment: " + a.toString());
					}
				}if (object instanceof Grade ) {
					Grade g = (Grade) object;
					if(g == null ) {
						System.out.println("Grade recieved is NULL");
						continue;
					}else {
						System.out.println("Grade recieved SUCCESSFULY");
						DBConnector.addGradeToDB(g);
						DBConnector.updateSubmissions(g);
						System.out.println("ADDED Grade: " + g.toString());
					}
				}if (object instanceof Submission ) {
					Submission s = (Submission) object;
					if(s == null ) {
						System.out.println("Submission recieved is NULL");
						continue;
					}else {
						System.out.println("Submission recieved SUCCESSFULY");
						DBConnector.addSubmissionToDB(s);
						String r = "SEND";
						Request request = new Request(r);
						try {//send confirmation
							objOut.writeObject(request);
							objOut.flush();
							Request extReq = (Request) objIn.readObject();
							byte[] content = (byte[])objIn.readObject();
							if (content != null && extReq != null) {
								if(content.length == extReq.length ) {
								DBConnector.uploadSubmissionContent(s.id, content, extReq.data.trim(), s.studentID, s.assignmentID,s.courseID);
								System.out.println("FILE TRANSFER SUCCESSFUL");
								}else {System.out.println("FILE IS CORRUPTED -- FAILED."); }
							}else {
								System.out.println("FILE TRANSFER FAILED.");
							}
						} catch (IOException | ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("ADDED Submission: " + s.toString());
					}
				}
				
		
		}//end while
		
		try {//clean up
			objIn.close();
			objOut.flush();
			objOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//end communicate
	
	
	/**
	 * Handles objects of type request and responds accordingly. 
	 *
	 * @param request the request
	 */
	synchronized public void handleRequest(Request request) {
		if(request.message.equalsIgnoreCase("GETCOURSELIST")) {
			System.out.println("HandleRequests properly recognized the request");
			sendCoursesToUser();
		}else if(request.message.equalsIgnoreCase("ACTIVATE")) {
			dbc.activateCourse(request.set,request.data);
		}else if(request.message.equalsIgnoreCase("GETSTUDENT")) {
			sendStudent(request);
		}else if(request.message.equalsIgnoreCase("GETPROFESSOR")) {
			sendProfessor(request);
		}else if(request.message.equalsIgnoreCase("GETSTUDENTLIST")) {
			sendStudentsInCourse(request);
		}else if(request.message.equalsIgnoreCase("ENROLL")) {
			System.out.println("Enroll Request Recieved...");
			enrollStudent(request);
		}else if(request.message.equalsIgnoreCase("GETASSIGNMENTLIST")) {
			System.out.println("Assignment List Request Recieved...");
			sendAssignments(request);
		}else if(request.message.equalsIgnoreCase("ACTIVATEASSIGNMENT")) {
			System.out.println("Assignment Activation Request Recieved...");
			DBConnector.activateAssignment(request);
		}else if(request.message.equalsIgnoreCase("DOWNLOAD")) {
			System.out.println("Assignment DOWNLOAD Request Recieved...");
			sendDownload(request);
		}else if(request.message.equalsIgnoreCase("GETSUBMISSIONS")) {
			System.out.println("Submissions Request Recieved...");
			sendSubmissions(request);
		}else if(request.message.equalsIgnoreCase("DOWNLOADSUB")) {
			System.out.println("Submission DOWNLOAD Request Recieved...");
			sendSubmissionDownload(request);
		}
		else {
			System.out.println("HandleRequests DID NOT recognize the request.");
		}
	}

	/**
	 * Send courses to user.
	 */
	synchronized public void sendCoursesToUser() {
		ArrayList<Course> toSend = null;
		if( user == null) {
			System.out.println("User is null, Unable to send courses.");
			return;
		}
		if (user.type == null) {
			System.out.println("this user does not have a type");
			return;
		}
		if(user.type.equalsIgnoreCase("STUDENT")) {
			System.out.println("sending course list to Student: " + user.firstName + "...");
			toSend = dbc.getStudentsCourses(user.id.trim());
		}else if(user.type.equalsIgnoreCase("PROFESSOR")) {
			System.out.println("sending course list to Professor: " + user.firstName + "...");
			toSend = dbc.getProfsCourses(user.id.trim());
		}else {
			System.out.println("User does not have a type, something went wrong.");
		}
		try {	
				System.out.println("sending courses...");
				objOut.writeObject(toSend);
				objOut.flush();
				if (toSend != null)
				System.out.println("Sent Courses to   " + user.firstName + " " + user.lastName);
				else 
				System.out.println("Sent NULL Courses to   " + user.firstName + " " + user.lastName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
			
	}
	
	/**
	 * Validates log in information .
	 *
	 * @param info the info
	 * @return true, if successful
	 */
	synchronized public boolean validateInfo(LoginInfo info ) {
		Boolean toReturn = false;
		Request tryAgain = new Request("TRY AGAIN");
		if(info.type.equalsIgnoreCase("Student")) { //student validation
			
			Student s = dbc.searchStudent("id", info.userID );
			if(s != null && (s.password.equalsIgnoreCase(info.password)) && ( s.type.equalsIgnoreCase(info.type)) ) {
				toReturn = true;
				this.user =(User) s;
			
				try {
					objOut.writeObject(s);
					objOut.flush();
					System.out.println("sucessful login. retuned student " + s.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return toReturn;
			}else {
				try {
					objOut.writeObject(tryAgain);
					objOut.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Validate user: unsuccessful");
				return false;
			}
		} else if (info.type.equalsIgnoreCase("Professor")) { //professor validation
			Professor p = dbc.searchProfessor("id", info.userID);
					if(p != null && (p.password.equalsIgnoreCase(info.password))  && (p.type.equalsIgnoreCase(info.type)) ) {
						toReturn = true;
						this.user =(User) p;
						try {
							objOut.writeObject(p);
							objOut.flush();
							System.out.println("sucessful login. retuned prof " + p.toString());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return toReturn;
					}else {
						try {
							objOut.writeObject(tryAgain);
							objOut.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Validate user: unsuccessful");
						return false;
					}
		}else {
			
			try {
				objOut.writeObject(tryAgain);
				objOut.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return false;
		}
	}
	
	/**
	 * Sends a student to the client.
	 *
	 * @param request the request
	 */
	synchronized void sendStudent(Request request ) {
		String by="";
		if (request.set)
			by = "id";
		if(!request.set)
			by = "name";
		Student s = DBConnector.searchStudent(by, request.data);
		Request tryAgain = new Request(new String("TRY AGAIN"));
		try {
			if (s==null) {
				System.out.println("Student not found");
				objOut.writeObject(tryAgain);
				objOut.flush();
				
			}else {
				System.out.println("FOUND: " + s.toString());
				objOut.writeObject(s);
				objOut.flush();
				System.out.println("STUDENT SENT TO USER");
				
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends  professor to the client.
	 *
	 * @param request the request
	 */
	synchronized void sendProfessor(Request request ) {
		String by="";
		if (request.set)
			by = "id";
		if(!request.set)
			by = "name";
		Professor p = dbc.searchProfessor(by, request.data);
		Request tryAgain = new Request(new String("TRY AGAIN"));
		try {
			if (p==null) {
				System.out.println("Professor not found");
				objOut.writeObject(tryAgain);
				objOut.flush();
				
			}else {
				System.out.println("FOUND: " + p.toString());
				objOut.writeObject(p);
				objOut.flush();
				System.out.println("PROFESSOR SENT TO USER");
				
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send students in course.
	 *
	 * @param request the request
	 */
	synchronized public void sendStudentsInCourse(Request request){
		ArrayList<Student> studentList = dbc.getStudentsInCourse(request.data);
		Request tryAgain = new Request(new String("TRY AGAIN"));
		try {
			if (studentList == null) {
				objOut.writeObject(tryAgain);
				objOut.flush();
			}
			else {
				objOut.writeObject(studentList);
				objOut.flush();
			}
			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	
	/**
	 * Send download.
	 *
	 * @param request the request
	 */
	synchronized public void sendDownload(Request request) {
		System.out.println("in sendDownload");
		Request toSend = DBConnector.getContentInfo(request.data);
		System.out.println("Getting assignment content from db....");
		byte[] content = dbc.getContent(request.data);
		toSend.setLength(content.length);
		
		try {
			objOut.writeObject(toSend);
			objOut.flush();
			System.out.println("SENT ASSIGNMENT INFO");
			objOut.writeObject(content);
			objOut.flush();
			System.out.println("SENT FILE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Send submission download.
	 *
	 * @param request the request
	 */
	synchronized public void sendSubmissionDownload(Request request) {
		System.out.println("in sendDownload");
		Request toSend = DBConnector.getSubmissionContentInfo(request.data);
		System.out.println("Getting Submission content from db....");
		byte[] content = dbc.getSubmissionContent(request.data);
		toSend.setLength(content.length);
		
		try {
			objOut.writeObject(toSend);
			objOut.flush();
			System.out.println("SENT SUBMISSION INFO");
			objOut.writeObject(content);
			objOut.flush();
			System.out.println("SENT Submission FILE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Send assignments.
	 *
	 * @param request the request
	 */
	synchronized public static void sendAssignments(Request request) {
		ArrayList<Assignment> toSend = DBConnector.getCoursesAsssignments(request.data);
		Request tryAgain = new Request(new String("TRY AGAIN"));
		try {
			if (toSend == null) {
				objOut.writeObject(tryAgain);
				objOut.flush();
			}
			else {
				objOut.writeObject(toSend);
				objOut.flush();
			}
			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	/**
	 * Send submissions.
	 *
	 * @param request the request
	 */
	synchronized void sendSubmissions(Request request) {
		Request tryAgain = new Request(new String("TRY AGAIN"));
		ArrayList<Submission> subs = null;
		try {
		subs = DBConnector.getSubmissionList(request.data, request.set);
		if(subs == null) {
			System.out.println("SUBMISSION LIST NOT FOUND");
				objOut.writeObject(tryAgain);
				objOut.flush();
		}else {
			objOut.writeObject(subs);
			objOut.flush();
			System.out.println("SUBMISSION LIST SENT ");
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Enroll student.
	 *
	 * @param request the request
	 */
	synchronized public void enrollStudent(Request request) {
		if(request.set) {
			StudentEnrollment temp = new StudentEnrollment(dbc.getRandomID(), request.data, request.from);
			DBConnector.addStudentEnrollementToDB(temp);
			System.out.println("ADDED StudentEnrollment");
		}else if(!request.set) {
			DBConnector.deleteStudentEnrollment(request.data,request.from);
			System.out.println("DELETED StudentEnrollment");
		}else {
			System.out.println("enroll student request not properly instantantized");
			return;
		}
	}
	
}
