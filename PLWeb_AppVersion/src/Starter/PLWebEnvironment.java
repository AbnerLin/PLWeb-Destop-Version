package Starter;


public class PLWebEnvironment {

	private String diskRoot = ".";
	private String jEditPath = "PLWeb/jEdit/";
	private String adImage = "Image/plweb-ad.png";
	private String pluginPath = "PLWeb/jEdit/workspace/jars/";
	private String adUrl = "http://v2.plweb.org:80";
	
	private String urlPackage;
	private String urlPackageAsc;
	private String urlLesson;
	private String lessonId;
	private String lessonPath;
	private String lessonFile;
	private String classId;
	private String courseId;
	
	private String[] plugins;
	private String[] pluginsAsc;
	

	private static PLWebEnvironment instance = null;

	public static PLWebEnvironment getInstance() {
		if (instance == null) {
			return instance = new PLWebEnvironment();
		} else {
			return instance;
		}
	}

	public PLWebEnvironment() {
		System.setProperty("javaws.plweb.isExam", "false");
		System.setProperty("file.encoding", "UTF-8");
		System.setProperty("user.language", "zh");
		System.setProperty("user.region", "TW");
		System.setProperty("javaws.user.language", "zh");
		System.setProperty("javaws.user.region", "TW");
		System.setProperty("javaws.plweb.urlDataSetting", "http://v2.plweb.org:80/dataSetting.groovy");
		System.setProperty("javaws.plweb.lessonmode", "student");
		
		detectOS();
		setDiskRoot(diskRoot);
		
		
		/* - -- - - -  - - - - -   */
//		setLessonPath("./PLWeb/800/2015300021/MasteryLearning_Java/6"); // 3 
//		System.setProperty("javaws.plweb.lessonxml", "./PLWeb/800/2015300021/MasteryLearning_Java/lesson6.xml"); // 3
//		System.setProperty("javaws.plweb.urlrequest", "http://v2.plweb.org:80/ServerRequest.groovy?s=EF05B25DDA43AC91D633611161A717F4&t=1428825591772"); // 3
//		setUrlLesson("http://v2.plweb.org:80/ServerLesson.groovy?s=BF1744C26A50D7E187EB6C04B9638630&t=1428770260468"); // 3
		
		
		/*
		 * lessonpath     directory path
		 * lessonxml     file path path
		 * urlrequest   request link
		 * urllesson   xml download link
		 * */
		
	}
	
	public void setUrlRequest(String urlrequest) {
		System.setProperty("javaws.plweb.urlrequest", urlrequest);
	}
	
	public void setLessonXml(String lessonXml) {
		System.setProperty("javaws.plweb.lessonxml", "./PLWeb/" + lessonXml);
	}
	
	public void setClassId(String classId) {
		this.classId = classId;
		System.setProperty("javaws.plweb.var.class_id", classId);
	}
	
	public void setCourseId(String courseId) {
		this.courseId = courseId;
		System.setProperty("javaws.plweb.var.course_id", courseId);
	}
	
	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
		System.setProperty("javaws.plweb.var.lesson_id", lessonId);
	}
	
	public void setLessonFile(String lessonFile) {
		this.lessonFile = lessonFile;
	}
	
	public void setUrlLesson(String urlLesson) {
		this.urlLesson = urlLesson;
	}
	
	public void setUname(String uname) {
		System.setProperty("javaws.plweb.uname", uname);  // 1
	}
	
	public void setUserId(String userId) {
		System.setProperty("javaws.plweb.var.user_id", userId);  
	}
	
	public void setEnrollment(String enrollment) {
		System.setProperty("javaws.plweb.enrollment", enrollment);
	}
	
	public void setDepartment(String department) {
		System.setProperty("javaws.plweb.department", department);
	}
	
	public void setDiskRoot(String diskRoot) {
		this.diskRoot = diskRoot;
		System.setProperty("javaws.plweb.diskroot", diskRoot); 
	}
	
	public void setLessonPath(String lessonPath) {
		
		this.lessonPath = "./PLWeb/" + lessonPath;
		System.setProperty("javaws.plweb.lessonpath", lessonPath);
	}
	
	public void detectOS() {
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("javaws.plweb.explorer", "nautils ${root}");
			System.setProperty("javaws.plweb.shell", "bash -c"); 
		} else if (System.getProperty("os.name").startsWith("Windows")) {
			System.setProperty("javaws.plweb.explorer", "start explorer /root, ${root}");
			System.setProperty("javaws.plweb.shell", "cmd /C"); 
		} else if (System.getProperty("os.name").startsWith("Linux")) {
			System.setProperty("javaws.plweb.explorer", "nautils ${root}");
			System.setProperty("javaws.plweb.shell", "bash -c");
		}
	}
	
	
	public String getLessonId() {
		return this.lessonId;
	}
	
	public String getCourseId() {
		return this.courseId;
	}
	
	public String getClassId() {
		return this.classId;
	}
	
	public String getUserId() {
		return System.getProperty("javaws.plweb.var.user_id");
	}

	public String getAdImage() {
		return adImage;
	}

//	public void setAdImage(String adImage) {
//		this.adImage = adImage;
//	}
//
//	public String getAdUrl() {
//		return adUrl;
//	}
//
//	public void setAdUrl(String adUrl) {
//		this.adUrl = adUrl;
//	}
//
//	public String getUrlPackageAsc() {
//		return urlPackageAsc;
//	}
//
//	public void setUrlPackageAsc(String urlPackageAsc) {
//		this.urlPackageAsc = urlPackageAsc;
//	}
//
//	public String[] getPluginsAsc() {
//		return pluginsAsc;
//	}
//
//	public void setPluginsAsc(String[] pluginsAsc) {
//		this.pluginsAsc = pluginsAsc;
//	}
//
//	public String getPluginPath() {
//		return pluginPath;
//	}
//
//	public void setPluginPath(String pluginPath) {
//		this.pluginPath = pluginPath;
//	}
//
//	public String[] getPlugins() {
//		return plugins;
//	}
//
//	public void setPlugins(String[] plugins) {
//		this.plugins = plugins;
//	}
//
	public String getJEditPath() {
		return jEditPath;
	}
//
//	public void setJEditPath(String editPath) {
//		jEditPath = editPath;
//	}
//
	public String getLessonFile() {
		return "lesson" + this.lessonId + ".xml";
	}
//
//	public void setLessonFile(String lessonFile) {
//		this.lessonFile = lessonFile;
//	}

	public String getLessonPath() {
		return lessonPath;
	}
//
//	public void setLessonPath(String lessonPath) {
//		this.lessonPath = lessonPath;
//		System.setProperty("javaws.plweb.lessonpath", lessonPath);
//	}
//
//	public String getLessonId() {
//		return lessonId;
//	}
//
//	public void setLessonId(String lessonId) {
//		this.lessonId = lessonId;
//	}
//
	public String getUrlLesson() {
		return urlLesson;
	}
//
//	public void setUrlLesson(String urlLesson) {
//		this.urlLesson = urlLesson;
//	}
//
//	public String getUrlPackage() {
//		return urlPackage;
//	}
//
//	public void setUrlPackage(String urlPackage) {
//		this.urlPackage = urlPackage;
//	}
//
	public String getDiskRoot() {
		return diskRoot;
	}

	
}
