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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import Shared.Professor;
import Shared.Request;
import Shared.Student;
import Shared.Submission;
import Shared.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// TODO: Auto-generated Javadoc
/**
* Provides data fields and methods to create a Java GUI for professors to view a courses content.
* The overall purpose of this class is to provide GUI components to edit and create/delete various objects through a course view
* This class uses Client controller to send and get messages, requests and various objects with the server.
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/ class StudentCourseView extends JFrame{

		/** The assignments list. list of assignments in this course.  */
		public static  ArrayList<Assignment> assignments;
		
		/** The submissions. list of all submissions for student. */
		public static ArrayList<Submission> submissions;
		
		/** The user. */
		public static User user;
		
		/** The course. */
		public static Course course;
		
		/** The prof. */
		public static Professor prof;
		
		/** The assignment list. */
		public static DefaultListModel assignmentList;
		
		/** The assignment jlist. */
		public static JList assignmentJlist;
		
		/** The assignment scroll pane. */
		public static JScrollPane assignmentScrollPane;
		
		/** The submission list. */
		DefaultListModel submissionList;
		
		/** The submission J list. */
		JList submissionJList;
		
		/** The submission scroll pane. */
		JScrollPane submissionScrollPane;

		/**
		 * Instantiates a new student course view.
		 *
		 * @param u the user logged in.
		 * @param c the course opened. 
		 * @param p the professor teaching the opened course. 
		 */
		public StudentCourseView( User u, Course c, Professor p) {
			 submissionList = new DefaultListModel<String>();
			 submissionJList = new JList(submissionList);
			 submissionScrollPane = new JScrollPane(submissionJList);
		    
			user = u;
			course = c;
			prof = p;
			String courseInfo = course.courseName + " " + course.courseNumber + "\t" + "Instructor: " + prof.firstName + " " + prof.lastName;
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
			JButton upload = new JButton("Submit Assignment");
			JButton download = new JButton("Download Assignment");
			JButton emailProf = new JButton("Email Professor");
			JButton dropBox = new JButton("My Submissions");
			
			browse.setBackground(Color.RED);
			upload.setBackground(Color.RED);
			emailProf.setBackground(Color.RED);
			download.setBackground(Color.RED);
			dropBox.setBackground(Color.RED);
			
			buttonPanel.add(browse);
			buttonPanel.add(upload);
			buttonPanel.add(download);
			buttonPanel.add(emailProf);
			buttonPanel.add(dropBox);
			
			//Assignments ScrollPane
			assignmentList = new DefaultListModel<String>();
			assignmentJlist = new JList(assignmentList);
			assignmentScrollPane = new JScrollPane(assignmentJlist);
			
			//Enrolled Students ScrollPane

			
		    frame.add(assignmentScrollPane, BorderLayout.CENTER);
		    frame.add(buttonPanel, BorderLayout.SOUTH);
		    
		    Container pane = frame.getContentPane();
		    
		    frame.setMinimumSize(new Dimension( 800, 600 ));
			frame.setVisible(true);
			frame.pack();
			//BROWSE ENROLLED STUDENTS 
			
			
			browse.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
				updateAssignments();
			}
			
		});
			
			emailProf.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
					JTextField subjectField = new JTextField();
					JTextField messageField = new JTextField();
					
					subjectField.setSize(150, 150);
					messageField.setSize(150, 150);
					final JComponent[] fill_ins = new JComponent[]{
					        new JLabel("Subject"), subjectField,
					        new JLabel("Message"), messageField
					        };
					String str = "EMail Professor " + prof.firstName + " " + prof.lastName; 
					Object[] insertOptions = { str, "Cancel" };
					
					int result = JOptionPane.showOptionDialog(null, fill_ins, "Search", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
				
					if( result == JOptionPane.OK_OPTION ) {
						emailCourseProf(subjectField.getText().trim(), messageField.getText().trim());
						String str1 = "Sent email to Professor " + prof.firstName + " " +prof.lastName;
						 JOptionPane.showMessageDialog(frame.getContentPane(), str1);
						
					}else {
						String str1 = "message cancelled";
						 JOptionPane.showMessageDialog(frame.getContentPane(), str1);
					}
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
					int index = assignmentJlist.getSelectedIndex();
					if ( index == -1) {
						JOptionPane.showMessageDialog(frame.getContentPane(), "Please select an assignment first. ");
						return;
					}
					Assignment assign = assignments.get(index);
					
					String courseID = course.courseID;
					String path;
					String date;
					File selectedFile = null;
					
					
					
					JTextField comment = new JTextField();
					
					comment.setColumns(100);
				
					JFileChooser fileBrowser = new JFileChooser();
					
					 Object[] insertOptions = { "Upload", "Cancel" };


				final JComponent[] fill_ins = new JComponent[]{
							new JLabel(""),
							new JLabel("Choose File to Submit "),
					        fileBrowser,
					        new JLabel("Enter a comment"),
					        comment, 
					        };
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date d = new Date();
				date = dateFormat.format(d).toString();
				
						String grade = "";
					int result = JOptionPane.showOptionDialog(null, fill_ins, "Upload Submission", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
					
					if( result == JOptionPane.OK_OPTION ) {
						if(fileBrowser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
							selectedFile = fileBrowser.getSelectedFile();
							path = selectedFile.toPath().toString();
							
							
							Submission toUpload = new Submission(ClientController.getRandomID(),assign.id, user.id, course.courseID , grade, path, comment.getText().trim(), date);
							String ext = path.substring(path.lastIndexOf("."),path.length());
							toUpload.assignmentName = assign.assignmentName;
							ClientController.uploadSubmission(toUpload,selectedFile,ext);
					}

				}
			
		});
			//SEARCH STUDENTS
			emailProf.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
			

			}
			
		});
			
			dropBox.addActionListener(new ActionListener(){

				@Override
			public void actionPerformed(ActionEvent e) {
					
					
					 
					
					 
					 submissions = ClientController.getSubmissions(user.id, false);
					 
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
					Object[] insertOptions = { "Go back ", "Close " };
					
					int result = JOptionPane.showOptionDialog(null, fill_ins, "Search", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
					
					if( result == JOptionPane.OK_OPTION ) { 
						
					}
			
			}
			
		});
			
		}
		
	
//		public static void updateAssignments( ) {
//			 = new ArrayList<Course>();
//			courses = ClientController.getCourseList();
//			Iterator courseItr = courses.iterator();
//			list.clear();
//			
//			int i=0;
//			while(courseItr.hasNext()) {
//				String temp = courseItr.next().toString();
//				list.add(i,temp);
//				i++;
//			}
//			System.out.println("List updated");
//		}
		
//		public static void updateDropbox( ) {
//			enrolledStudents = new ArrayList<Student>();
//			enrolledStudents = ClientController.getAllStudentsIn(course.courseID);
//			if (enrolledStudents == null ) {
//				
//				return;
//			}
//			Iterator studentItr = enrolledStudents.iterator();
//			enrolledList.clear();
//			
//			int i=0;
//			while(studentItr.hasNext()) {
//				String temp = studentItr.next().toString();
//				enrolledList.add(i,temp);
//				i++;
//			}
//			System.out.println("Enrolled List updated");
/**
 * Update assignments. Asks server to refresh the list of assignments. 
 */
//		}
		public static void updateAssignments() {
			assignments = new ArrayList<Assignment>();
			assignments = ClientController.getAssignmentsFor(course.courseID);
			if (assignments == null)
				return;
			Assignment tempAssignment = null;
			for(int i = 0; i < assignments.size() ; i++ ) {
				tempAssignment = assignments.get(i);
				if(!tempAssignment.isActive) {
					assignments.remove(i);
				}
			}
			
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
		 * Email course prof. Sends an email to the course's professor. 
		 *
		 * @param subject the subject of the email. 
		 * @param message the message of the email.
		 */
		public static void emailCourseProf(String subject, String message ) {
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
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(prof.email));
					msg.setSubject(subject);
					msg.setText(message);
					Transport.send(msg); // Send the Email Message
					} catch (MessagingException e) {
					e.printStackTrace();
					}
		}
		
	}

