package gui;

import db.DatabaseUtilities;
import core.RadarTrack;
import core.track.TrackUtilities;
import core.cluster.DirectlyGetCluster;
import core.cluster.DBSCAN;
import core.cluster.RecursiveInterSection;
import core.cluster.PixelsDistance;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JProgressBar;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;

public class WelcomeFrame extends JFrame {
	private final Action action = new SwingAction();
	private JTextField trackSizeText;
	private JTextField dataSizeText;
	
	int CLUSTER_TYPE=0;
	
	TrackUtilities tu;
	DatabaseUtilities du;
	
	DirectlyGetCluster dgc;
	DBSCAN dbs;
	RecursiveInterSection ris;
	PixelsDistance pd;
	
	HashMap<Integer,RadarTrack> trackMap;
	private JTextField textField;
	

	/**
	 * Create the frame.
	 */
	public WelcomeFrame() {
		
		tu = new TrackUtilities();
		
		try{
			du = new DatabaseUtilities();
		}
		catch(SQLException sqlex){
			sqlex.printStackTrace();
		}
		
		setSize(new Dimension(100000000, 100000000));
		setBackground(new Color(204, 153, 51));
		setType(Type.POPUP);
		setForeground(new Color(51, 0, 255));
		setFont(new Font("AR MingtiM BIG-5", Font.PLAIN, 12));
		setTitle("軌跡資料分析系統");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnDatasource = new JMenu("DataSource");
		menuBar.add(mnDatasource);
		
		JMenuItem mntmDatabase = new JMenuItem("DataBase");
		mntmDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getDataFromDatabase();
			}
		});
		mnDatasource.add(mntmDatabase);
		
	
		
		JMenu mnMachinelearning = new JMenu("Function");
		menuBar.add(mnMachinelearning);
		
		JMenu mnNewMenu = new JMenu("Track Generate");
		mnMachinelearning.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("From Database");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generateTrackFromDB();
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmFromText = new JMenuItem("From Points Text");
		mntmFromText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generateTrackFromPointsText();
			}
		});
		mnNewMenu.add(mntmFromText);
		
		JMenuItem mntmFromTracksText = new JMenuItem("From Tracks Text");
		mntmFromTracksText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generateTrackFromTracksText();
			}
		});
		mnNewMenu.add(mntmFromTracksText);
		
		JMenuItem mntmTrackFilter = new JMenuItem("Track Filter");
		mntmTrackFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trackFilter();
			}
		});
		mnMachinelearning.add(mntmTrackFilter);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Fix Track Length");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fixTrackLength();
			}
		});
		mnMachinelearning.add(mntmNewMenuItem_1);
		

		
		JMenu mnCluster = new JMenu("Cluster");
		mnMachinelearning.add(mnCluster);
		
		JMenuItem mntmDbscan = new JMenuItem("DBSCAN");
		mntmDbscan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DBSCANClustering();
			}
		});
		
		JMenuItem mntmGetClusterText = new JMenuItem("Get From Cluster Text");
		mntmGetClusterText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getFromClusterText();
			}
		});
		mnCluster.add(mntmGetClusterText);
		mnCluster.add(mntmDbscan);
		
		JMenuItem mntmRecursiveintersection = new JMenuItem("RecursiveIntersection");
		mntmRecursiveintersection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RecusiveInterSectionClustering();
			}
		});
		mnCluster.add(mntmRecursiveintersection);
		
		JMenuItem mntmPixelsdistance = new JMenuItem("PixelsDistance");
		mntmPixelsdistance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PixelsDistanceClustering();
			}
		});
		mnCluster.add(mntmPixelsdistance);
		
		JMenuItem mntmTestData = new JMenuItem("Testing");
		mntmTestData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Testing();
			}
		});
		mnMachinelearning.add(mntmTestData);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 5, 10, 10));
		
		JLabel lblNewLabel_1 = new JLabel("\u8CC7\u6599\u91CF\uFF1A");
		panel.add(lblNewLabel_1);
		
		dataSizeText = new JTextField();
		dataSizeText.setEditable(false);
		panel.add(dataSizeText);
		dataSizeText.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("\u8ECC\u8DE1\u6578\uFF1A");
		panel.add(lblNewLabel_3);
		
		trackSizeText = new JTextField();
		trackSizeText.setEditable(false);
		panel.add(trackSizeText);
		trackSizeText.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("視覺化");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dataVisualize();
			}
		});
		panel.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JLabel label = new JLabel("進度%");
		panel_1.add(label);
		
		JProgressBar progressBar = new JProgressBar();
		panel_1.add(progressBar);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new GridLayout(1, 3, 10, 10));
		
		JLabel clusterLabel = new JLabel("分群數量：");
		panel_3.add(clusterLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		panel_3.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		panel_3.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("");
		panel_3.add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("視覺化");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clusteringVisualize();
			}
		});
		panel_3.add(btnNewButton);
	
		
	
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	
	public void getDataFromDatabase(){
		
		DatabaseOptionSettingDialog dosd = new DatabaseOptionSettingDialog();
		dosd.setModal(true);
		dosd.setVisible(true);
		
		Vector<String> dbtext = dosd.getDBText();
		
		dataSizeText.setText(Integer.toString(dbtext.size()));
		
	}
	
	public void generateTrackFromDB(){
		
		System.out.println("Generate Tracks from Database...");
		
		try{
			
			trackMap = tu.getTrackFromDatabase(du);
			trackSizeText.setText(Integer.toString(trackMap.size()));
		}
		catch(SQLException sqlex){
			sqlex.printStackTrace();
		}
	}
	
	public void generateTrackFromPointsText(){
		
		System.out.println("Generate Tracks from Text...");
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File","txt","csv");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    
	    String fileName = null;
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	     
	    	fileName = chooser.getSelectedFile().getPath();
	    }
	    
	    System.out.println(fileName);
	    
	    if(fileName!=null){
	    	trackMap = tu.getTrackFromPointsText(fileName);
			trackSizeText.setText(Integer.toString(trackMap.size()));
	    	
	    }
		
		
	}
	
	public void generateTrackFromTracksText(){
		System.out.println("Generate Tracks from Text...");
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File","txt","csv");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    
	    String fileName = null;
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	     
	    	fileName = chooser.getSelectedFile().getPath();
	    }
	    
	    System.out.println(fileName);
	    
	    if(fileName!=null){
	    	trackMap = tu.getTrackFromTracksText(fileName);
			trackSizeText.setText(Integer.toString(trackMap.size()));
	    }
		
	}
	
	
	public void trackFilter(){
		
		trackMap = tu.getFilterTrack(trackMap);
		trackSizeText.setText(Integer.toString(trackMap.size()));
		
	}
	
	public void fixTrackLength(){
		
		trackMap = tu.getFixLengthTrack(20, trackMap);
		trackSizeText.setText(Integer.toString(trackMap.size()));
	}
	
	public void getFromClusterText(){
		
		CLUSTER_TYPE=99;
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File","txt","csv");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    
	    String fileName = null;
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	     
	    	fileName = chooser.getSelectedFile().getPath();
	    }
	    
	    System.out.println(fileName);
	    
	    if(fileName!=null){
	    	
	    	dgc = new DirectlyGetCluster(fileName);
	    }
	    
		
		
		
		
	}
	
	public void DBSCANClustering(){
		
		CLUSTER_TYPE=1;
		
		dbs = new DBSCAN(tu.getInstances(trackMap),0.05,5);
		dbs.printFile();
		
		
	}
	
	public void RecusiveInterSectionClustering(){
		
		CLUSTER_TYPE=2;
		
		double oriLon=113.64;
		double oriLat=16;
		double maxLon=128.35;
		double maxLat=31;
	
		int grids=1024;
		
		ris = new RecursiveInterSection(oriLon,oriLat,maxLon,maxLat,grids,trackMap);
		ris.printFile();
	}
	
	public void PixelsDistanceClustering(){
		
		CLUSTER_TYPE=3;
		
		double oriLon=113.64;
		double oriLat=16;
		double maxLon=128.35;
		double maxLat=31;
	
		int grids=256;
		
		pd = new PixelsDistance(oriLon,oriLat,maxLon,maxLat,grids,trackMap);
		pd.printFile();
		
	}
	
	public void dataVisualize(){
		TrackVisualizeFrame vf = new TrackVisualizeFrame(trackMap);
		vf.setVisible(true);
	}
	
	public void clusteringVisualize(){
		
		if(CLUSTER_TYPE==99){
			ClusterVisualizeFrame cvf = new ClusterVisualizeFrame(dgc);
			cvf.setVisible(true);
		}
		
		else if(CLUSTER_TYPE==1){
			ClusterVisualizeFrame cvf = new ClusterVisualizeFrame(dbs);
			cvf.setVisible(true);
		}
			
		else if(CLUSTER_TYPE==2){
			ClusterVisualizeFrame cvf = new ClusterVisualizeFrame(ris);
			cvf.setVisible(true);
		}
		
		else if(CLUSTER_TYPE==3){
			ClusterVisualizeFrame cvf = new ClusterVisualizeFrame(pd);
			cvf.setVisible(true);
		}
		
		
	}
	
	public void Testing(){
		
		TestingFrame tf = new TestingFrame();
		tf.setVisible(true);
		
		
	}
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeFrame frame = new WelcomeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
}
