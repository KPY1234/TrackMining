package gui;

import db.DatabaseUtilities;

import java.util.Vector;

import java.sql.SQLException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class DatabaseOptionSettingDialog extends JDialog {
	
	
	Vector<String> dbtext = new Vector<String>();

	private final JPanel contentPanel = new JPanel();
	private JTextField urlText;
	private JTextField accountText;
	private JPasswordField passwdText;

	

	/**
	 * Create the dialog.
	 */
	public DatabaseOptionSettingDialog() {
		//setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(BorderFactory
				.createTitledBorder("資料庫連結設定"));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		{
			JLabel lblNewLabel_1 = new JLabel("連結位址(URL)");
			contentPanel.add(lblNewLabel_1);
		}
		{
			urlText = new JTextField();
			urlText.setText("jdbc:mysql://localhost:3306/airdefence");
			contentPanel.add(urlText);
			urlText.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("帳號");
			contentPanel.add(lblNewLabel);
		}
		{
			accountText = new JTextField();
			accountText.setText("TrackMining");
			contentPanel.add(accountText);
			accountText.setColumns(10);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("密碼");
			contentPanel.add(lblNewLabel_2);
		}
		{
			passwdText = new JPasswordField();
			passwdText.setText("icrdc4isr19+");
			contentPanel.add(passwdText);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Connect");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						connect();
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		pack();
	}
	
	
	public void connect(){
		
		String driver="org.gjt.mm.mysql.Driver";
		
		String url = urlText.getText();
		String account = accountText.getText();
		char[] passwd = passwdText.getPassword() ;
		
		String passwdStr = new String(passwd);
		
		try{
			DatabaseUtilities du = new DatabaseUtilities(driver,url,account,passwdStr);
			
			String sql="SELECT trackNo,height,xPos,yPos,trackTime FROM trackinfot_1000000_filter ORDER BY trackNo";
			
			dbtext = du.getQuery(sql);
			
			
		}
		catch(SQLException sqlex){
			sqlex.printStackTrace();
		}
		
		//System.out.println("url="+url+"\taccount="+account+"\tpasswd="+passwdStr);
		
		dispose();
		
	}
	
	
	public Vector<String> getDBText(){
		return dbtext;
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			
			DatabaseOptionSettingDialog dialog = new DatabaseOptionSettingDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
