package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


import Shared.Assignment;
import Shared.Course;
import Shared.Grade;
import Shared.Request;
import Shared.Student;
import Shared.Submission;
import Shared.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//import javax.activation.*;

// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java GUI for professors to view a courses content.
* The overall purpose of this class is to provide GUI components to edit and create/delete various objects through a course view
* This class uses Client controller to send and get messages, requests and various objects with the server.
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class ProfessorCourseView extends JFrame{

		/** The submissions. list of submissions for a given course's assignment. */
		public static ArrayList<Submission> submissions;
		
		/** The assignments. list of assignments for a course. */
		public static  ArrayList<Assignment> assignments;
		
		/** The enrolled students. list of students enrolled in a course.  */
		public static ArrayList<Student> enrolledStudents;
		
		/** The all students. list of all students in the system to search from.  */
		public static ArrayList<Student> allStudents;
		
		/** The user. */
		public static User user;
		
		/** The course. */
		public static Course course;
		
		/** The assignment list. */
		public static DefaultListModel assignmentList;
		
		/** The assignment jlist. */
		public static JList assignmentJlist;
		
		/** The assignment scroll pane. */
		public static JScrollPane assignmentScrollPane;
		
		/** The enrolled list. */
		public static DefaultListModel enrolledList;
		
		/** The enrolled J list. */
		public static JList enrolledJList;
		
		/** The enrolled scroll pane. */
		public static JScrollPane enrolledScrollPane;
		
		/**
		 * Instantiates a new professor course view.
		 *
		 * @param u the user (professor) in the session.
		 * @param c the course being opened. 
		 */
		public ProfessorCourseView( User u, Course c) {
			
		    
			user = u;
			course = c;
			String courseInfo = course.courseName + " " + course.courseNumber + "\t" + "Instructor: " + user.firstName + " " + user.lastName;
			JFrame frame = new JFrame(courseInfo);
			JPanel buttonPanel = new JPanel();
			JScrollPane coursesPane = new JScrollPane();
			
			if(user!=null) {
				String welcomeMessage = "Welcome " + ClientController.user.firstName + " "+ ClientController.user.lastName + "!" ;  
				JLabel infoLabel = new JLabel(welcomeMessage, SwingConstants.CENTER );
				frame.add(infoLabel, BorderLayout.NORTH);
				infoLabel.setSize(200, 300);
			}
			JButton browse = new JButton("Browse Assignments");
			JButton enrolled = new JButton("Browse Class List");
			JButton upload = new JButton("Upload Assignment");
			JButton search = new JButton("Search Students");
			JButton activate = new JButton("Activate/Deactivate Assignment");
			JButton download = new JButton("Download Assignment");
			JButton dropbox =  new JButton("Open DropBox");
			
			browse.setBackground(Color.RED);
			upload.setBackground(Color.RED);
			search.setBackground(Color.RED);
			activate.setBackground(Color.RED);
			
			buttonPanel.add(browse);
			buttonPanel.add(upload);
			buttonPanel.add(activate);
			buttonPanel.add(search);
			buttonPanel.add(enrolled);
			buttonPanel.add(download);
			buttonPanel.add(dropbox);
			
			//Assignments ScrollPane
			assignmentList = new DefaultListModel<String>();
			assignmentJlist = new JList(assignmentList);
			assignmentScrollPane = new JScrollPane(assignmentJlist);
			
			//Enrolled Students ScrollPane
			enrolledList = new DefaultListModel<String>();
			enrolledJList = new JList(enrolledList);
		    enrolledScrollPane = new JScrollPane(enrolledJList );
		    
			
		    frame.add(assignmentScrollPane, BorderLayout.CENTER);
		    frame.add(buttonPanel, BorderLayout.SOUTH);
		    
		    Container pane = frame.getContentPane();
		    
		    frame.setMinimumSize(new Dimension( 800, 600 ));
			frame.setVisible(true);
			frame.pack();
			//BROWSE ENROLLED STUDENTS 
			enrolled.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
//					JButton email = new JButton("Send E-Mail");
//					JButton unEnroll = new JButton("UN-ENROLL from Course ");
//					JPanel optionPanel = new JPanel();
//					optionPanel.add(email);
//					optionPanel.add(unEnroll);
					updateEnrolledStudents();
					if (enrolledStudents == null || enrolledStudents.size() == 0 ) {
						JOptionPane.showMessageDialog(frame.getContentPane(), "No Students enrolled yet. ");
						return;
					}
					JTextField subjectField = new JTextField();
					JTextField messageField = new JTextField();
					
					subjectField.setSize(150, 150);
					messageField.setSize(150, 150);
					
					
					final JComponent[] fill_ins = new JComponent[]{
								new JLabel( "CLASS SIZE : " + enrolledStudents.size() + " ENROLLED IN : " + course.courseName + " " + course.courseNumber ),
						        enrolledScrollPane ,
						        new JLabel("Subject"), subjectField,
						        new JLabel("Message"), messageField
						        };

						Object[] insertOptions = { "E-Mail ALL", "Un-Enroll Student" };
						
						int result = JOptionPane.showOptionDialog(null, fill_ins, "Search", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
					
						if( result == JOptionPane.OK_OPTION ) {
							//Emailing all students
							emailAllEnrolledStudents( subjectField.getText().trim(), messageField.getText().trim());
							String str = "Sent email to all enrolled students.";
							 JOptionPane.showMessageDialog(frame.getContentPane(), str);
							
						}else {
							int index = enrolledJList.getSelectedIndex();
							if (index == -1 ) {
								 String str = "Please select a student to Un-Enroll first.";
								 JOptionPane.showMessageDialog(frame.getContentPane(), str);
							}else {
								Student selected = enrolledStudents.get(index);
								ClientController.enrollStudent(selected.id, course.courseID , false);
								updateEnrolledStudents();
							}
							
							
						}
			}
			
		});
			
			browse.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
				updateAssignments();
			}
			
		});
		
			
			download.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
					JFileChooser fileBrowser = new JFileChooser();
					 Object[] insertOptions = { "Download", "Cancel" };
					 File selectedFile = null;
					 File selectedDir = null;
					 Assignment toDownload;
					 final JComponent[] fill_ins = new JComponent[]{
								new JLabel(""),
						        new JLabel("Choose directory to download assignment" ),
						        fileBrowser
						        };
					 int result = JOptionPane.showOptionDialog(null, fill_ins, "DOWNLOAD", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
						
					 if( result == JOptionPane.OK_OPTION ) {
						 int index = assignmentJlist.getSelectedIndex();
						 if( index == -1 ) {
							 JOptionPane.showMessageDialog(frame.getContentPane(), "Please select an assignment to download first. ");
								return;
							 
						 }
						 	
							if(fileBrowser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
								selectedDir = fileBrowser.getCurrentDirectory();
								toDownload = assignments.get(index);
								
								if ( toDownload != null ) {
									System.out.println("Assignment to download ID: " + toDownload.id + "   " + toDownload.assignmentName);
								ClientController.downloadAssignment(toDownload,selectedDir);
								}else {
									System.out.println("selected Assignment is null");
								}
						}
				}
			
		});
		    
			upload.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
					
					
					String courseID = course.courseID;
					String path;
					String due;
					Boolean isActive;
					File selectedFile = null;

					JTextField assignmentNameField = new JTextField();
					JTextField dueField = new JTextField();
					assignmentNameField.setColumns(50);
					dueField.setColumns(16);
					
					String[] type = { "Active" , " Not Active "};
					JComboBox types = new JComboBox(type);
					JFileChooser fileBrowser = new JFileChooser();
					
					 Object[] insertOptions = { "Upload", "Cancel" };


				final JComponent[] fill_ins = new JComponent[]{
							new JLabel(""),
					        new JLabel("Enter The Assignment Name "),
					        assignmentNameField,
					        new JLabel("Enter the due date "),
					        dueField,
					        new JLabel("Choose Assignment Status "),
					        types, 
					        new JLabel("Choose Assignment Status "),
					        fileBrowser
					        };


					int result = JOptionPane.showOptionDialog(null, fill_ins, "Insert a New Course", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
					
					if( result == JOptionPane.OK_OPTION ) {
						if(fileBrowser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
							selectedFile = fileBrowser.getSelectedFile();
							path = selectedFile.toPath().toString();
							due = dueField.getText().trim();
							if (types.getSelectedIndex() == 0)
								isActive = true;
							else 
								isActive=false;
							Assignment toUpload = new Assignment( ClientController.getRandomID() , assignmentNameField.getText().trim(), courseID, path, due, isActive);
							String ext = path.substring(path.lastIndexOf("."),path.length());
							
							
							ClientController.uploadAssignment(toUpload,selectedFile,ext);
					}
				}
			
		});
			//SEARCH STUDENTS
			search.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
			
					


					JTextField infoField = new JTextField();
					JTextArea foundArea = new JTextArea("");
					
					
					foundArea.setColumns(50);
					foundArea.setRows(1);
					
					String[] type = {  "ID","LAST NAME"};
					JComboBox types = new JComboBox(type);

					 Object[] insertOptions = { "Search", "Cancel" };


				final JComponent[] fill_ins = new JComponent[]{
							new JLabel("Search For a Student."),
					        new JLabel("By:  "),
					        types,
					        new JLabel("Enter Student info"),
					        infoField,
					        new JLabel("Result: "),
					        foundArea, };


					int result = JOptionPane.showOptionDialog(null, fill_ins, "Search", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
					
					if( result == JOptionPane.OK_OPTION ) {
						Boolean stat=true;
						if(types.getSelectedIndex() == 0 ) {
							stat = true;
							if (infoField.getText().trim().length() != 8  ) {
								JOptionPane.showMessageDialog(frame.getContentPane(), "ID field must be exactly 8 numbers long. Please try again. ");
								return;
							}
						}
						if(types.getSelectedIndex() == 1 ) {
							stat = false;
							if (infoField.getText().trim().length() > 30  ) {
								JOptionPane.showMessageDialog(frame.getContentPane(), "Last Name field must not be longer than 30 characters. Please try again. ");
								return;
							}
						}
						
						Student found = ClientController.getStudent(infoField.getText().trim(), stat);
						String str="";
						
						if (found != null) {
						 str = found.toString();
						 foundArea.setText(str);
						 
						 
						 
						foundArea.setRows(2);
						 
						 final JComponent[] fill = new JComponent[]{
									new JLabel("Student found: "),
							        foundArea,
						 	};
						 
						 
						 String etc = "Enroll Student in " + course.courseName ;
						 String ef = "E-Mail " + found.firstName;
						
						 Object[] insertOptions1 = { etc, ef };

							int result1 = JOptionPane.showOptionDialog(null, fill, "Search", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions1, null);
							
							if( result1 == JOptionPane.OK_OPTION ) {
								System.out.println("LOLOLOLOLOLOOLOLOOOOO");
								System.out.println("HAAAAAAAAAAAAAAAA");
								
								if (enrolledStudents != null && enrolledStudents.contains(found)) {
									String str1 = found.firstName + " " +found.firstName + "Is already enrolled in" + course.courseName ; 
									JOptionPane.showMessageDialog(frame.getContentPane(), str1);
								}else {
									ClientController.enrollStudent(found.id, course.courseID, true );
									updateEnrolledStudents();
								}
							}else {
								
							}
						}
						 else {// search cancelled
							 str = "No matching Student found in DB";
							 JOptionPane.showMessageDialog(frame.getContentPane(), str);
						
						 }
					}else{
						
					}
			}
			
		});
			
			activate.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
					
					String[] type = { "ACTIVE", " NON-ACTIVE "};
					JComboBox types = new JComboBox(type);

					 Object[] insertOptions = { "Save", "Cancel" };


				final JComponent[] fill_ins = new JComponent[]{
							new JLabel(""),
					        new JLabel("Select an assignment"),
					        assignmentScrollPane,
					        new JLabel("Set as: "),
					        types, };


					int result = JOptionPane.showOptionDialog(null, fill_ins, "Save", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
					
					if( result == JOptionPane.OK_OPTION ) {
						int index = assignmentJlist.getSelectedIndex();
						Assignment toChange = assignments.get(index);
						Boolean setAs = toChange.isActive;
						if(types.getSelectedIndex() == 0)
							setAs = true;
						if(types.getSelectedIndex() == 1)
							setAs = false;
						
						Request request = new Request(new String("ACTIVATEASSIGNMENT"));
						request.setData(toChange.id, setAs);
						
						System.out.println("Request sent to server:  set "+ toChange.assignmentName + " to: "+ setAs);
						//assignmentJlist.remove(index);
						try {
							ClientController.objOut.writeObject(request);
							ClientController.objOut.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						updateAssignments();
					}else{
						
					}
			}
			
		});
			
			
			dropbox.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
				int index = assignmentJlist.getSelectedIndex();
				 if( index == -1 ) {
					 JOptionPane.showMessageDialog(frame.getContentPane(), "Please select an assignment to download first. ");
						return;
				 }
				 Assignment assign = assignments.get(index);
				 
				 DefaultListModel submissionList = new DefaultListModel<String>();
				 JList submissionJList = new JList(submissionList);
				 JScrollPane submissionScrollPane = new JScrollPane(submissionJList);
				 
				 submissions = ClientController.getSubmissions(assign.id, true);
				 
				if(submissions != null) {
					submissionList.clear();
					for (int i = 0 ; i < submissions.size() ; i ++){
						submissionList.add(i, submissions.get(i).toString());
					}
				}
				
				final JComponent[] fill_ins = new JComponent[]{
						new JLabel("Select a Submission"),
				        submissionScrollPane,
				        };
				Object[] insertOptions = { "DOWNLOAD SUBMISSION ", "GRADE SUBMISSION" };
				
				int result = JOptionPane.showOptionDialog(null, fill_ins, "Search", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
				
				if( result == JOptionPane.OK_OPTION ) {//DOWNLOAD SUBMISSION 
					int subIndex = submissionJList.getSelectedIndex();
					if ( subIndex == -1) {
						JOptionPane.showMessageDialog(frame.getContentPane(), "Please select a submission first. ");
						return;
					}
					JFileChooser fileBrowser = new JFileChooser();
					 final JComponent[] fill_ins1 = new JComponent[]{
								new JLabel(""),
						        new JLabel("Choose directory to download assignment" ),
						        fileBrowser
						        };
					 int result1 = JOptionPane.showOptionDialog(null, fill_ins1, "DOWNLOAD", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
						File selectedDir = null;
						Submission toDownload =null;
					 if( result1 == JOptionPane.OK_OPTION ) {
						 if(fileBrowser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
								selectedDir = fileBrowser.getCurrentDirectory();
								toDownload = submissions.get(subIndex);
								
								if ( toDownload != null ) {
									System.out.println("Assignment to download ID: " + toDownload.id + "   " + toDownload.assignmentName);
								ClientController.downloadSubmission(toDownload,selectedDir);
								}else {
									System.out.println("selected Assignment is null");
								}
						 
					 }
					
				}else {
					int subIndex = submissionJList.getSelectedIndex();
					if ( subIndex == -1) {
						JOptionPane.showMessageDialog(frame.getContentPane(), "Please select a submission first. ");
						return;
					}
					
					JTextField markField = new JTextField();
					JTextField commentField = new JTextField();
					markField.setColumns(3);
					commentField.setColumns(100);
					
					final JComponent[] fill_ins1 = new JComponent[]{
							new JLabel(""),
					        new JLabel("Enter a Grade" ),
					        markField,
					        new JLabel("Enter a Comment" ),
					        commentField
					        
					        };
					
					Object[] insertOptions1 = { "Grade Submission ", "Cancel" };
				 int result1 = JOptionPane.showOptionDialog(null, fill_ins1, "OK", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions1, null);
					File selectedDir = null;
					Submission toMark =null;
				 if( result1 == JOptionPane.OK_OPTION ) {
					 toMark = submissions.get(subIndex);
					String mark =  markField.getText().trim();
					 Grade toSend = new Grade(ClientController.getRandomID(),course.courseID, toMark.assignmentID, toMark.studentID, mark);
					 try {
						 System.out.println("GRADE SENT");
						ClientController.objOut.writeObject(toSend);
						ClientController.objOut.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				 }
				 
				}
				 
			
				 
			}
			
		});
			Color myColor = new Color(100, 125, 250);

			frame.getContentPane().setBackground(myColor); 
			assignmentScrollPane.setBackground(myColor);
		}
		
	

		
		/**
		 * Update enrolled students.
		 * asks the server to refresh the enrolled students in the course. 
		 */
		public static void updateEnrolledStudents( ) {
			enrolledStudents = new ArrayList<Student>();
			enrolledStudents = ClientController.getAllStudentsIn(course.courseID);
			if (enrolledStudents == null ) {
				
				return;
			}
			Iterator studentItr = enrolledStudents.iterator();
			enrolledList.clear();
			
			int i=0;
			while(studentItr.hasNext()) {
				String temp = studentItr.next().toString();
				enrolledList.add(i,temp);
				i++;
			}
			System.out.println("Enrolled List updated");
		}
		
		/**
		 * Update assignment list, asks the server for a refreshed list of assignments from the course.
		 */
		public static void updateAssignments() {
			assignments = new ArrayList<Assignment>();
			assignments = ClientController.getAssignmentsFor(course.courseID);
			if(assignments == null || assignments.size() == 0)
				return;
			Iterator assignmentItr = assignments.iterator();
			assignmentList.clear();
			
			int i=0;
			while(assignmentItr.hasNext()) {
				String temp = assignmentItr.next().toString();
				assignmentList.add(i,temp);
				i++;
			}
			System.out.println("Assignment List updated");
			
		}
		
		/**
		 * Email all enrolled students.
		 * sends an email to all the students enrolled in this course. 
		 *
		 * @param subject the subject
		 * @param message the message
		 */
		public static void emailAllEnrolledStudents(String subject, String message ) {
			Properties properties = new Properties();
			properties.put("mail.smtp.starttls.enable", "true"); // Using TLS
			properties.put("mail.smtp.auth", "true"); // Authenticate
			properties.put("mail.smtp.host", "smtp.gmail.com"); // Using Gmail Account
			properties.put("mail.smtp.port", "587"); // TLS uses port 587
			Session session = Session.getInstance(properties,
					new javax.mail.Authenticator(){
					 protected PasswordAuthentication getPasswordAuthentication() {
					 return new PasswordAuthentication(ClientController.appEmail, ClientController.appEmailPass);
					 }
					});
					// Create and send the Email:
					try {
					Message msg = new MimeMessage(session);
					msg.setFrom(new InternetAddress(ClientController.appEmail));
					for(int i = 0; i < enrolledStudents.size();i++) {//ADD ALL EMAIL ADRESS TO MSG
						msg.addRecipient(Message.RecipientType.CC, new InternetAddress(enrolledStudents.get(i).email));
					}
					
					msg.setSubject(subject);
					msg.setText(message);
					Transport.send(msg); // Send the Email Message
					} catch (MessagingException e) {
					e.printStackTrace();
					}
		}
		
		
	}

