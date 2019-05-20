package client;

/**
* Provides various methods and components to create a Java GUI for a professor user in a Learning Platform Java application.
* The overall purpose of this class is to provide a user friendly GUI for the User to create/delete/alter various objects. This class
* uses methods from the ClientController class. 
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.*;

import Shared.Assignment;
import Shared.Course;
import Shared.Request;
import Shared.Student;
import Shared.User;



// TODO: Auto-generated Javadoc
/**
 * The Class ProfessorGUI.
 */
public class ProfessorGUI extends JFrame {
	
	/** The courses the professor is teaching.  */
	public static  ArrayList<Course> courses;
	
	/** The user(professor) logged in.  */
	public static User user;
	
	/** The list used to keep track and display courses. */
	public static DefaultListModel list;
	
	/** The jlist used to keep track and display courses. */
	public static JList jlist;
	
	/** The scroll pane used to keep track and display courses. */
	public static JScrollPane scrollPane1;
	
	/**
	 * Instantiates a new professor GUI.
	 *
	 * @param u the user logged into the session. 
	 */
	public ProfessorGUI(User u) {
		
		user = u;
		JFrame frame = new JFrame("Professor GUI");
		JScrollPane coursesPane = new JScrollPane();
		
		if(user!=null) {
			String welcomeMessage = "Welcome " + ClientController.user.firstName + " "+ ClientController.user.lastName + "!" ;  
			JLabel infoLabel = new JLabel(welcomeMessage, SwingConstants.CENTER );
			frame.add(infoLabel, BorderLayout.NORTH);
			infoLabel.setMinimumSize(new Dimension(200, 300));
			
		}
		JPanel buttonPanel = new JPanel();
		JButton openCourse = new JButton("Open Course");
		JButton browse = new JButton("Browse My Courses");
		JButton addCourse = new JButton("Add a Course");
		JButton activate = new JButton("Activate / Deactivate Course");
		buttonPanel.add(browse);
		buttonPanel.add(addCourse);
		buttonPanel.add(activate);
		buttonPanel.add(openCourse);
		
		
		list = new DefaultListModel<String>();
		jlist = new JList(list);
	    scrollPane1 = new JScrollPane(jlist);
		
	    frame.add(scrollPane1, BorderLayout.CENTER);
	    frame.add(buttonPanel, BorderLayout.SOUTH);
	    
	    Container pane = frame.getContentPane();
	    getContentPane().setBackground(Color.YELLOW);  
	    frame.setMinimumSize(new Dimension( 800, 600 ));
		frame.setVisible(true);
		frame.pack();
		
		browse.addActionListener(new ActionListener(){

			@Override
		public void actionPerformed(ActionEvent e) {
				
				updateCourses();
		}
		
	});
		//ADDCOURSE LISTENER
		addCourse.addActionListener(new ActionListener(){

			@Override
		public void actionPerformed(ActionEvent e) {
		
				


				JTextField courseNameField = new JTextField();
				JTextField courseNumberField = new JTextField();
				courseNameField.setColumns(50);
				courseNumberField.setColumns(3);
				
				String[] type = { "Active", " Not Active "};
				JComboBox types = new JComboBox(type);

				 Object[] insertOptions = { "Insert", "Cancel" };


			final JComponent[] fill_ins = new JComponent[]{
						new JLabel(""),
				        new JLabel("Enter The Course Name "),
				        courseNameField,
				        new JLabel("Enter The Course Number"),
				        courseNumberField,
				        new JLabel("Choose Course Status "),
				        types, };


				int result = JOptionPane.showOptionDialog(null, fill_ins, "Insert a New Course", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
				
				if( result == JOptionPane.OK_OPTION ) {
					Boolean stat=true;
					if(types.getSelectedIndex() == 0 ) 
						stat = true;
					if(types.getSelectedIndex() == 1 ) 
						stat = false;
					Course temp = new Course(ClientController.getRandomID() , courseNameField.getText().trim(), courseNumberField.getText().trim(), user.firstName, user.id, stat );
					String str = temp.toString();
					System.out.println("new course sent to server: " + str);
					try {
						ClientController.objOut.writeObject(temp);
						ClientController.objOut.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}else{
					
				}
		}
		
	});
			//ACTIVATE LISTENER
		activate.addActionListener(new ActionListener(){

			@Override
		public void actionPerformed(ActionEvent e) {
				
				String[] type = { "ACTIVE", " NON-ACTIVE "};
				JComboBox types = new JComboBox(type);

				 Object[] insertOptions = { "Save", "Cancel" };


			final JComponent[] fill_ins = new JComponent[]{
						new JLabel(""),
				        new JLabel("Select a course"),
				        scrollPane1,
				        new JLabel("Set as: "),
				        types, };


				int result = JOptionPane.showOptionDialog(null, fill_ins, "Save", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, insertOptions, null);
				
				if( result == JOptionPane.OK_OPTION ) {
					int index = jlist.getSelectedIndex();
					Course toChange = courses.get(index);
					Boolean setAs = toChange.isActive;
					if(types.getSelectedIndex() == 0)
						setAs = true;
					if(types.getSelectedIndex() == 1)
						setAs = false;
					
					Request request = new Request(new String("ACTIVATE"));
					request.setData(toChange.courseID, setAs);
					
					System.out.println("Request sent to server:  set "+ toChange.courseName + " to: "+ setAs);
					list.remove(index);
					try {
						ClientController.objOut.writeObject(request);
						ClientController.objOut.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}else{
					
				}
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
				ClientController.setCourseView( new ProfessorCourseView(user, toOpen));
		
		}
		
	});
		Color myColor = new Color(100, 125, 250);

		frame.getContentPane().setBackground(myColor); 
	}

	/**
	 * Update courses.
	 */
	public static void updateCourses( ) {
		courses = new ArrayList<Course>();
		courses = ClientController.getCourseList();
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
