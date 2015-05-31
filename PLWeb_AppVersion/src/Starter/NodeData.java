package Starter;

public class NodeData {
	
	private String class_id;
	private String course_id;
	private String lesson_id;
	private String title;
	
	public NodeData(String class_id, String course_id, String lesson_id, String title) {
		this.class_id = class_id;
		this.course_id = course_id;
		this.lesson_id = lesson_id;
		this.title = title;
	}
	
	public String getClassId() {
		return class_id;
	}
	
	public String getCourseId() {
		return course_id;
	}
	
	public String getLessonId() {
		return lesson_id;
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
