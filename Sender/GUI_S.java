import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUI_S extends JFrame{

	private static final long serialVersionUID = 1L;
	
	//Buttons
	JButton SendButton;
	JButton IsAliveButton;

	
	//Labels
	JLabel IP_Label;
	JLabel S_Port_Label;
	JLabel R_Port_Label;
	
	JLabel File_Name;
	JLabel Packets_Label;
	JLabel Timeout_Label;
	
	JLabel Title_Label;
	JLabel Names_Label;
	
	
	//TextFields
	JTextField IP_Text;
	JTextField S_Port_Text;
	JTextField R_Port_Text;
	
	JTextField File_Text;
	JTextField Packets_Text;
	JTextField Timeout_Text;
	
	//Panels
	JPanel Panel1;
	JPanel Panel2;
	
	//Check Box
	JCheckBox checkBox1;
	

	
	
	GUI_S(){
		//Step 1: Create the frame by extending JFrame
		this.setTitle("Sender & Reciever Application");
		
		//Step 2: Close Operation On Frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Step 3: Set the Frame Size
		this.setSize(400,670);
		
				
		//Step 4: Set Layout for Frame
		this.setLayout(new BorderLayout());
		
		//Step 5: Create Buttons and Name and Color and Check Box
		SendButton = new JButton("SEND");
		IsAliveButton = new JButton("IS ALIVE");

		checkBox1 = new JCheckBox("Reliable?");
		
		SendButton.setBackground(new Color(110, 215, 208));
		IsAliveButton.setBackground(new Color(110, 215, 208));

			
		
		//Step 6: Create Text Fields
		IP_Text = new JTextField();
		S_Port_Text = new JTextField();
		R_Port_Text = new JTextField();
		
		File_Text = new JTextField();
		Packets_Text = new JTextField();
		Timeout_Text = new JTextField();
		
		//Step 7: Create Labels and Set Labels,Font, Text Size
		IP_Label = new JLabel("IP Address of the Reciever");
		S_Port_Label = new JLabel("UDP Port Number used by Sender");
		R_Port_Label = new JLabel ("UDP Port Number used by Reciever");
		
		File_Name = new JLabel("File Transfer Name");
		Packets_Label = new JLabel("Current Number of Sent in order packets");
		Timeout_Label = new JLabel("<Timeout>");
		
		Title_Label = new JLabel("Sender & Reciever Application");
		Names_Label = new JLabel("Created by Sumeet Sandhu & Daniel Zhou");
		
		
		
		//Step 8: Create and Set Layout and Color For Panels
		Panel1 = new JPanel();
		Panel2 = new JPanel();
		Panel1.setBackground(new Color(219, 238, 236));
		Panel2.setBackground(new Color(204, 255, 229));
		
		Panel1.setPreferredSize(new Dimension(400,500));
		Panel2.setPreferredSize(new Dimension(600,170));
		
		//Step 9: Add Panels to Frame
		this.add(Panel1, BorderLayout.NORTH);
		this.add(Panel2, BorderLayout.SOUTH);
		
		
		//Step 10: Add Layout to Panels
		
		//Panel1.setLayout(new GridLayout(9,1));
		Panel1.setLayout(null);
		Panel2.setLayout(null);
		
		
		//Step 11: Set Sizes for Panel 1 Labels & Text Fields $ Set Sizes for Panel 2 Buttons
		
		Title_Label.setBounds(50, 0, 300, 50);
		Title_Label.setFont(new Font(null, Font.BOLD, 20));
		
		Names_Label.setBounds(80, 20, 300, 50);
		Names_Label.setFont(new Font(null, Font.ITALIC, 12));
		
		
		IP_Label.setBounds(50, 50, 300, 50);
		IP_Label.setFont(new Font(null, Font.PLAIN, 14));
		
		IP_Text.setBounds(50, 90, 300, 30);
		
		S_Port_Label.setBounds(50, 110, 300, 50);
		S_Port_Label.setFont(new Font(null, Font.PLAIN, 14));
		S_Port_Text.setBounds(50,150, 300,30);
		
		R_Port_Label.setBounds(50,170,300,50);
		R_Port_Label.setFont(new Font(null, Font.PLAIN, 14));
		R_Port_Text.setBounds(50,210,300, 30);
	
		Packets_Label.setBounds(50,230,300,50);
		Packets_Label.setFont(new Font(null, Font.PLAIN, 14));
		Packets_Text.setBounds(50,275,300, 30);
		
		Timeout_Label.setBounds(50,300,300,50);
		Timeout_Label.setFont(new Font(null, Font.PLAIN, 14));
		Timeout_Text.setBounds(50,340,300, 30);
		
		File_Name.setBounds(50,370,300,50);
		File_Name.setFont(new Font(null, Font.PLAIN, 14));
		File_Text.setBounds(50,410,300, 30);
		
		
		SendButton.setBounds(10, 50, 190, 50);
		IsAliveButton.setBounds(190, 50, 190, 50);
		checkBox1.setBounds(130, 110, 100, 50);
		checkBox1.setBackground(new Color(110, 215, 208));

		
		//Step 12: Add Components to Panel 1
		
			//Title & Name
		Panel1.add(Title_Label);
		Panel1.add(Names_Label);
		
			//IP & Port Number
		Panel1.add(IP_Label);
		Panel1.add(IP_Text);
		
		Panel1.add(S_Port_Label);
		Panel1.add(S_Port_Text);
		
		Panel1.add(R_Port_Label);
		Panel1.add(R_Port_Text);
		
			//Packets and time out
		Panel1.add(Packets_Label);
		Panel1.add(Packets_Text);
		
		Panel1.add(Timeout_Label);
		Panel1.add(Timeout_Text);
		
			//File Name
		Panel1.add(File_Name);
		Panel1.add(File_Text);
	
		//Step 13: Add Components to Panel 2
		IsAliveButton.addActionListener(e -> {
			try {
				IsAliveButtonHandler(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		SendButton.addActionListener(e->{
			try {
				SendButtonHandler(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		Panel2.add(SendButton);
		Panel2.add(IsAliveButton);
		Panel2.add(checkBox1);

		

		//Step 14: Set Frame to visible
		this.setVisible(true);
		
		
	}
	
	public void SendButtonHandler(ActionEvent e1) throws UnknownHostException, IOException {
		String ip = IP_Text.getText();
		int sender_port = Integer.parseInt(S_Port_Text.getText());
		int receiver_port = Integer.parseInt(R_Port_Text.getText());
		int timeout = Integer.parseInt(Timeout_Text.getText());
		String file_name = File_Text.getText();
		boolean is_reliable = checkBox1.isSelected();
		SenderHandler.setInOrderPacketLabel(Packets_Text);
		SenderHandler.start_sending(ip, sender_port, receiver_port, timeout, file_name, is_reliable);
	}
	
	public void IsAliveButtonHandler(ActionEvent e) throws UnknownHostException, IOException {
		String ip = IP_Text.getText();
		int sender_port = Integer.parseInt(S_Port_Text.getText());
		int receiver_port = Integer.parseInt(R_Port_Text.getText());
		boolean check_alive = SenderHandler.is_alive(ip,sender_port,receiver_port);
		if(check_alive == true) {
			JOptionPane.showMessageDialog(this, "Reciever is Alive and ready");
		}else {
			JOptionPane.showMessageDialog(this, "Reciever is NOT Alive");
		}
	}
}
