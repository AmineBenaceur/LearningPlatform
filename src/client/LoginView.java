package client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import Shared.*;

/**
* Provides data fields and methods to create a Java data-type, representing a
* GUI log in window in a Learning Platform Java application.
* The overall purpose of this class is to fetch login information and send to the server to validate. If a user enters correct crediedentials 
* the class will then use Client Controller to launch a view for them depending if they are a Student or Professor user.
*
* @author A.Benaceur, M.Amouzai 
* @version 1.0
* @since March 5, 2018
*/
public class LoginView extends JFrame{
	
	/** The frame. */
	/*
	 *A frame for the gui.
	 */
	static Frame frame;
	
	/** The user. */
	public static User user;

	/*
	 * constructs a Login view GUI. calls methods from LoginController in action listener for events.
	 * 
	 */
	
	/**
	 * Instantiates a new login view.
	 */
	public LoginView(){
		
		JFrame frame;
		// ui components
	
		JPanel infoPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JTextField idField = new JTextField();
		JLabel idLabel = new JLabel("ID:",SwingConstants.LEFT );
		JLabel passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
		JTextField passField = new JTextField();
		String[] type = { "Student", " Professor" };
		JComboBox types = new JComboBox(type);
		JButton login = new JButton("LOG IN");
		JButton cancel = new JButton("CANCEL");
		
		idField.setColumns(20);
		passField.setColumns(20);
		idField.setMaximumSize( new Dimension( 300, 20 ) );
		passField.setMaximumSize( new Dimension( 300, 20 ) );
		types.setMaximumSize(new Dimension( 300, 100 ));
		
		//add components to panels
		buttonPanel.add(login);
		buttonPanel.add(cancel);
//		infoPanel.add(idField,BorderLayout.LINE_START);
//		infoPanel.add(passField,BorderLayout.CENTER);
//		infoPanel.add(types,BorderLayout.LINE_END);
		
		Dimension dim;
	
		// frame stuff
		frame = new JFrame("LOGIN VIEW");
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		// frame.setVisible(true);
		frame.setSize(1000, 700);
		frame.setLocation((dim.width / 2 - frame.getWidth() / 2), (dim.height - frame.getHeight()) / 2);
		// frame.add(rp = new RenderPanel() );
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
	
		Container pane = frame.getContentPane();
		
		frame.setLayout(new BorderLayout(1, 3));

		infoPanel.setLayout(
		         new BoxLayout( infoPanel, BoxLayout.Y_AXIS ) );
		
		infoPanel.add( Box.createGlue() );
		 infoPanel.add( idLabel);
        infoPanel.add( idField);
      // infoPanel.add( Box.createGlue() );
        infoPanel.add( passwordLabel);
        infoPanel.add( passField);
        infoPanel.add( Box.createGlue() );
        infoPanel.add( types );
       
        
        infoPanel.setMinimumSize(new Dimension(400, 250));
       JPanel empty = new JPanel();
        empty.setMinimumSize(new Dimension(150, 100));
        pane.add(infoPanel, BorderLayout.CENTER);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		pane.add(empty,BorderLayout.NORTH);
		
		
		
		frame.setMinimumSize(new Dimension( 500, 350 ));
		frame.setVisible(true);
		frame.pack();
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to close this window?", "Close Window?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        		try {
							ClientController.objOut.writeObject(new Request(new String("quit")));
							ClientController.objOut.flush();
							ClientController.objOut.close();
							ClientController.objIn.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            System.exit(0);
		        }
		    }
		});
		
		login.addActionListener(new ActionListener(){

			@Override
		public void actionPerformed(ActionEvent e) {
				String type = "";
				if (types.getSelectedIndex() == 0)
					type = "STUDENT";
				if (types.getSelectedIndex() == 1)
					type = "PROFESSOR";
				Boolean success = false;
				success = ClientController.sendLoginInfo( idField.getText().trim(), passField.getText().trim(),type );
				if(!success) {
					JOptionPane.showMessageDialog(frame.getContentPane(), "Incorrect ID/PASSWORD, try again.");
				}
				
		}
		
	});
	
		
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		LoginView lv = new LoginView();
	}
}
