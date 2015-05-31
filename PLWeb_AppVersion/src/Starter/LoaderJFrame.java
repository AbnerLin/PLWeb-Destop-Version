package Starter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class LoaderJFrame extends JFrame {

	private static final long serialVersionUID = 3555732372365461392L;

	private PLWebEnvironment env = PLWebEnvironment.getInstance();

	private String diskRoot = env.getDiskRoot();
	private File jEditHome = new File(diskRoot, env.getJEditPath());

	private MessagePrinter msg;

	private JTextField userTextField;
	private JPasswordField pwdTextField;
	private JButton loginBtn;
	private JPanel loginPanel;
	
	private jEditLoader loader;
	private String loginUrl = "http://v2.plweb.org/app_api/loginAuth.groovy";
	private String startupUrl = "http://v2.plweb.org/app_api/getStartupInfo.groovy";
	private HttpRequester httpRequest;
	private JSONParserClass _jsonParse;
	
	
	
	public LoaderJFrame() {

		// title logo
		JLabel labelComp;
		labelComp = new JLabel(new ImageIcon(env.getAdImage()));
		labelComp.setPreferredSize(new Dimension(400, 100));

		// login panel
		loginPanel = new JPanel();
		loginPanel.setPreferredSize(new Dimension(400, 200));
		loginPanel.setBackground(Color.WHITE);
		SpringLayout layout = new SpringLayout();
		loginPanel.setLayout(layout);
		loginPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 0, 10),
				BorderFactory.createTitledBorder("")));

		// info input panel
		JLabel userLabel = new JLabel("E-mail : ");
		userLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		userTextField = new JTextField();
		userTextField.setPreferredSize(new Dimension(170, 25));
		JLabel pwdLabel = new JLabel("Password : ");
		pwdLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		pwdTextField = new JPasswordField();
		pwdTextField.setPreferredSize(new Dimension(170, 25));
		loginBtn = new JButton("login");
		loginBtn.addActionListener(_loginBtn);

		loginPanel.add(userLabel);
		loginPanel.add(userTextField);
		loginPanel.add(pwdLabel);
		loginPanel.add(pwdTextField);
		loginPanel.add(loginBtn);

		layout.putConstraint(SpringLayout.WEST, userLabel, 75,
				SpringLayout.WEST, loginPanel);
		layout.putConstraint(SpringLayout.NORTH, userLabel, 45,
				SpringLayout.NORTH, loginPanel);
		layout.putConstraint(SpringLayout.WEST, userTextField, 160,
				SpringLayout.WEST, loginPanel);
		layout.putConstraint(SpringLayout.NORTH, userTextField, 45,
				SpringLayout.NORTH, loginPanel);
		layout.putConstraint(SpringLayout.WEST, pwdLabel, 42,
				SpringLayout.WEST, loginPanel);
		layout.putConstraint(SpringLayout.NORTH, pwdLabel, 90,
				SpringLayout.NORTH, loginPanel);
		layout.putConstraint(SpringLayout.WEST, pwdTextField, 160,
				SpringLayout.WEST, loginPanel);
		layout.putConstraint(SpringLayout.NORTH, pwdTextField, 90,
				SpringLayout.NORTH, loginPanel);
		layout.putConstraint(SpringLayout.WEST, loginBtn, 268,
				SpringLayout.WEST, loginPanel);
		layout.putConstraint(SpringLayout.NORTH, loginBtn, 135,
				SpringLayout.NORTH, loginPanel);

		// message panel
		DefaultListModel listModel = new MessageListModel();
		JList listComp = new JList(listModel);
		listComp.setFont(new Font("Arial",Font.BOLD,16));
		listComp.setForeground(Color.DARK_GRAY);
		listComp.setBackground(Color.WHITE);
		listComp.setPreferredSize(new Dimension(400, 100));
		listComp.setBorder(BorderFactory.createEmptyBorder());
		listComp.setAutoscrolls(true);
		listComp.setFocusable(false);
		listComp.setVisibleRowCount(100);
		listComp.setSelectedIndex(20);
		listComp.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10,
				Color.WHITE));
		
		// main frame
		setTitle("PLWeb WebStart");
		setLayout(new BorderLayout());
		add(labelComp, BorderLayout.NORTH);
		add(loginPanel, BorderLayout.CENTER);
		add(listComp, BorderLayout.SOUTH);
		setResizable(false);
		pack();
		setVisible(true);

		msg = new MessagePrinter(listModel);
//		msg.println("Welcome.");
	}

	ActionListener _loginBtn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			loginBtn.setEnabled(false);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					msg.println("login...");
					
					Properties props = new Properties();
					props.setProperty("email", userTextField.getText());
					props.setProperty("password", pwdTextField.getText());
					
					httpRequest = new HttpRequester();
					String response = "";
					try {
						response = httpRequest.sendPost(loginUrl, props);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(response.trim().equals("login failed.")) {
						msg.clear();
						loginBtn.setEnabled(true);
						msg.println("E-mail or Password incorrect.");
					} else {
						
						msg.clear();
						msg.println("Login successed.");
						_jsonParse = new JSONParserClass();
						/* set user info */
						try {
							env.setDepartment(_jsonParse.getDataFromObj(response, "Department").toString());
							env.setEnrollment(_jsonParse.getDataFromObj(response, "Enrollment").toString());
							env.setUserId(_jsonParse.getDataFromObj(response, "UserId").toString());
							env.setUname(_jsonParse.getDataFromObj(response, "Uname").toString());
							
							/* set JTree Panel */
							loginPanel.removeAll();
							loginPanel.repaint();
							
							Properties treeProp = new Properties();
							DefaultMutableTreeNode root = new DefaultMutableTreeNode("課程");
							DefaultMutableTreeNode tmp;
							String class_id, course_id, lesson_id;
							
							String classStr = _jsonParse.getDataFromObj(response, "class").toString();
							JSONArray jsonArr = _jsonParse.parseArr(classStr);
							
							JSONArray jsonArrCourse;
							JSONObject jsonObjCourse;
							
							JSONObject tmpObj;
							for(int i = 0; i < jsonArr.size(); i++) {
								tmpObj = (JSONObject) _jsonParse.getDataFromArr(jsonArr.toJSONString(), i);  // course obj
								class_id = tmpObj.get("class_id").toString();
								jsonArrCourse = _jsonParse.parseArr(tmpObj.get("course").toString());  // get course array
								
								if(jsonArrCourse.size() > 0) {
									tmp = new DefaultMutableTreeNode(decodeBase64(tmpObj.get("name").toString())); // set course
									for(int j = 0; j < jsonArrCourse.size(); j++) {
										jsonObjCourse = _jsonParse.parseObj(jsonArrCourse.get(j).toString());  // convert course array's content to jsonobject
										course_id = jsonObjCourse.get("course_id").toString();
										lesson_id = jsonObjCourse.get("lesson_id").toString();
										NodeData nodeObj = new NodeData(class_id, course_id, lesson_id, decodeBase64(jsonObjCourse.get("name").toString()));
										tmp.add(new DefaultMutableTreeNode(nodeObj));  // set lesson
									}
									root.add(tmp);
								}
							}
						
							final JTree tree = new JTree(root);
							JScrollPane treePanel = new JScrollPane(tree);
							treePanel.setPreferredSize(new Dimension(380, 190));
							loginPanel.add(treePanel);
						
							MouseListener ml = new MouseAdapter() {
							    public void mousePressed(MouseEvent e) {
							        int selRow = tree.getRowForLocation(e.getX(), e.getY());
							        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
							        if(selRow != -1 && ((DefaultMutableTreeNode)selPath.getLastPathComponent()).isLeaf()) {
							            if(e.getClickCount() == 2) {
							            	NodeData data = (NodeData) ((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject();
							            	try {
												startUpjEdit(data);
											} catch (ParseException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
							            }
							        }
							    }
							};							
							tree.addMouseListener(ml);
							
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} 
												
					}
				}
			});
			
		}
	};
	
	public void startUpjEdit(NodeData data) throws ParseException {
//    	System.out.println(data.getClassId() + "/" + data.getCourseId() + "/" + data.getLessonId());
    	env.setClassId(data.getClassId());
    	env.setCourseId(data.getCourseId());
    	env.setLessonId(data.getLessonId());
		
    	
//    	System.out.println(env.getUserId() + " " + env.getClassId() + " " + env.getCourseId() + " " + env.getLessonId());
    	
    	Properties props = new Properties();
		props.setProperty("user_id", env.getUserId());
		props.setProperty("class_id", env.getClassId());
		props.setProperty("course_id", env.getCourseId());
		props.setProperty("lesson_id", env.getLessonId());
		
//		httpRequest = new HttpRequester();
		String response = "";
		try {
			response = httpRequest.sendPost(startupUrl, props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}
		
//		System.out.println(response);
		
		JSONObject obj = _jsonParse.parseObj(response);
		
//		System.out.println(obj.get("requestLink"));
		
		env.setLessonPath(obj.get("lessonPath").toString());
		env.setLessonXml(obj.get("lessonxml").toString());
		env.setUrlRequest(obj.get("requestLink").toString());
		env.setUrlLesson(obj.get("xmlLink").toString());
    	
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				autoload();
			}
		});
	}
	
	public String decodeBase64(String str) throws UnsupportedEncodingException {
		return new String(new Base64().decode(str.getBytes()), "UTF-8");
	}
	
	public void autoload() {

		/*
		 * 0. check jEditPackage and Plugins exists, if not and download. 
		 * 1. check jEditPackage and Plugins md5 for self-update. 
		 * 2. downloadLessonXML. 
		 * 3. start jEdit.
		 */

		try {
			// downloadJEditPackage();
			// downloadJEditPlugins();
			
			setVisible(false);
			downloadLessonXML();
			startJEditor();
		} catch (Exception ex) {
			// msg.println(ex.getMessage());
			System.out.println(ex.toString());
		}
	}

	private void startJEditor() {
		loader = new jEditLoader();
		loader.setSettingsPath(jEditHome);
		loader.setWorkspacePath(new File(jEditHome, "workspace"));
		loader.setMenubarVisible(false);
		loader.load();
	}

//	private void downloadJEditPlugins() throws Exception {
//		String pluginPath = env.getPluginPath();
//		File tempPlugins = new File(diskRoot);
//		File filePlugins = new File(diskRoot, pluginPath);
//
//		String[] pluginsAsc = env.getPluginsAsc();
//		int i = 0;
//
//		for (String plugin : env.getPlugins()) {
//			URL urlPlugin = new URL(plugin);
//			String pluginName = new File(urlPlugin.getFile()).getName();
//
//			File tempPlugin = new File(tempPlugins, pluginName);
//			File filePlugin = new File(filePlugins, pluginName);
//
//			boolean downloaded = false;
//
//			if (tempPlugin.exists()) {
//				// msg.println("Plug-in Checksum [" + pluginName + "]: ");
//				String asc = pluginsAsc[i];
//				if (asc != null) {
//					if (asc.trim().equals(
//							Checksum.md5(tempPlugin.getPath()).trim())) {
//						// msg.update("OK");
//						downloaded = true;
//					}
//				}
//			}
//
//			if (!downloaded) {
//				// msg.println("Plug-in Download [" + pluginName + "]: ");
//				HttpDownloaderRunnable run = new HttpDownloaderRunnable(plugin,
//						tempPlugin);
//
//				Thread thread = new Thread(run);
//				thread.start();
//
//				int n = 0;
//				while (thread.isAlive()) {
//					// msg.update(String.valueOf(run.getLength()) + " bytes");
//					Thread.sleep(200);
//					n++;
//				}
//
//				// msg.update(String.valueOf(tempPlugin.length() / 1204) +
//				// " Kbytes");
//			}
//
//			AntTask.copyFile(tempPlugin, filePlugin);
//
//			i++;
//		}
//	}

//	private void downloadJEditPackage() throws Exception {
//
//		File fileJEditLog = new File(jEditHome, "activity.log");
//		if (fileJEditLog.exists() && !fileJEditLog.delete()) {
//			throw new JEditExistsException();
//		}
//
//		URL urlPackage = new URL(env.getUrlPackage());
//		String packageName = new File(urlPackage.getFile()).getName();
//		File filePackage = new File(diskRoot, packageName);
//
//		boolean downloaded = false;
//
//		if (filePackage.exists()) {
//			// msg.println("Package Checksum [" + packageName + "]: ");
//
//			String asc = env.getUrlPackageAsc();
//			if (asc != null) {
//				if (asc.trim().equals(
//						Checksum.md5(filePackage.getPath()).trim())) {
//					// msg.update("OK");
//					downloaded = true;
//				}
//			}
//
//			/*
//			 * if (!downloaded) { msg.update("FAILED"); }
//			 */
//		}
//
//		if (!downloaded) {
//			// msg.println("Package Download [" + packageName + "]: ");
//
//			HttpDownloaderRunnable run;
//			run = new HttpDownloaderRunnable(urlPackage, filePackage);
//
//			Thread thread = new Thread(run);
//			thread.start();
//
//			while (thread.isAlive()) {
//				// msg.update(String.valueOf(run.getLength()) + " bytes");
//				Thread.sleep(200);
//			}
//
//			// msg.update(String.valueOf(filePackage.length() / 1204) +
//			// " Kbytes");
//		}
//
//		// *** 閫��蝮屐Edit
//		// msg.println("Extracting to ".concat(jEditHome.getPath()));
//
//		AntTask.delDir(jEditHome); // 蝘駁���冗
//		AntTask.unzipFile(jEditHome, filePackage); // 閫��蝮�
//	}

	private void downloadLessonXML() throws Exception {
		URL urlLesson = new URL(env.getUrlLesson());
		String lessonPath = env.getLessonPath();
		String lessonFile = env.getLessonFile();
		File fileLesson = new File(diskRoot, lessonPath);
		File fileLessonXml = new File(fileLesson.getParent(), lessonFile);

		// msg.println("Download XML [" + lessonFile + "]: ");

		HttpDownloaderRunnable run;
		run = new HttpDownloaderRunnable(urlLesson, fileLessonXml, true);

		Thread thread = new Thread(run);
		thread.start();

		while (thread.isAlive()) {
			// msg.update(String.valueOf(run.getLength()) + " bytes");
			Thread.sleep(200);
		}

		// msg.update(String.valueOf(fileLessonXml.length() / 1204) +
		// " Kbytes");
	}
}
