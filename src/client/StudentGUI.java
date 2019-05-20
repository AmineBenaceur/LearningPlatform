package client;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import Shared.Course;
import Shared.Professor;
import Shared.Student;
import Shared.User;
import javax.swing.*;
// TODO: Auto-generated Javadoc
/**
* Provides various methods and components to create a Java GUI for a student user in a Learning Platform Java application.
* The overall purpose of this class is to provide a user friendly GUI for the User to create/delete/alter various objects. This class
* uses methods from the ClientController class. 
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class StudentGUI extends JFrame {
	
	/** The courses. a list of courses the student is enrolled in.  */
	public static  ArrayList<Course> courses;
	
	/** The user. The user who had just logged in.  */
	public static User user;
	
	/** The list. a list used to display the courses.  */
	public static DefaultListModel list;
	
	/** The jlist. a JList used to display the courses.  */
	public static JList jlist;
	
	/** The scroll pane 1. a JScrollPane used to display the courses.  */
	public static JScrollPane scrollPane1;
	
	/**
	 * Instantiates a new student GUI.
	 *
	 * @param u the user(Student) that logged in. 
	 */
	public StudentGUI(User u) {
		user = u;
		
		JFrame frame = new JFrame("STUDENT GUI");
		JPanel buttonPanel = new JPanel();
		JScrollPane coursesPane = new JScrollPane();
		if(user!=null) {
			String welcomeMessage = "Welcome " + ClientController.user.firstName + " "+ ClientController.user.lastName + "!" ;  
			JLabel infoLabel = new JLabel(welcomeMessage, SwingConstants.CENTER );
			frame.add(infoLabel, BorderLayout.NORTH);
			infoLabel.setSize(200, 300);
		}
		JButton browse = new JButton("Browse My Courses");
		JButton openCourse = new JButton("Open Course");
		buttonPanel.add(browse);
		buttonPanel.add(openCourse);
		
		list = new DefaultListModel<String>();
		jlist = new JList(list);
	    scrollPane1 = new JScrollPane(jlist);
		
	    frame.add(scrollPane1, BorderLayout.CENTER);
	    frame.add(buttonPanel, BorderLayout.SOUTH);
	
	    Container pane = frame.getContentPane();
	    
	    frame.setMinimumSize(new Dimension( 800, 600 ));
		frame.setVisible(true);
		frame.pack();
		
		browse.addActionListener(new ActionListener(){

			@Override
		public void actionPerformed(ActionEvent e) {
				
				updateCourses();
				

		}
		
	});
		
		//OPENCOURSE LISTENER
		openCourse.addActionListener(new ActionListener(){

			@Override
		public void actionPerformed(ActionEvent e) {
				int index = jlist.getSelectedIndex();
				
				if(index == -1) {
					JOptionPane.showMessageDialog(frame.getContentPane(), "Please select a course then press open.");
					return;
				}
				
				Course toOpen = courses.get(index);
				Professor prof = ClientController.getProfessor(toOpen.professorID, true);
				ClientController.setCourseView( new StudentCourseView(user, toOpen, prof));
		
		}
		
	});
	    
	}
	
	/**
	 * Update courses.
	 * asks the server for a refreshed list of courses.
	 */
	public static void updateCourses( ) {
		
		courses = ClientController.getCourseList();
		Course tempCourse = null;
		for(int i = 0; i < courses.size() ; i++ ) {
			tempCourse = courses.get(i);
			if(!tempCourse.isActive) {
				courses.remove(i);
			}
		}
		Iterator courseItr = courses.iterator();
		list.clear();
		
		int i=0;
		while(courseItr.hasNext()) {
			String temp = courseItr.next().toString();
			list.add(i,temp);
			i++;
		}
		System.out.println("List updated");
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Student peter = new Student(new User("Peter", "Griffin", "peter100", "petergriffin@ucalgary.ca", "100000" ));
		ProfessorGUI pgui = new ProfessorGUI(peter);
	}
}
