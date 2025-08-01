package org.kosa.nest.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

public class FileVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int fileId;
	private String fileLocation;
	private LocalDateTime createdAt;
	private LocalDateTime uploadAt;
	private int adminId;
	private String subject;
	private String tag;
//	2차 구현시 변경
//	private ArrayList<String> tag;
	private String description;

	/**
	 * DB로부터 데이터를 가져와 file 객체 생성시에 사용하는 생성자
	 * @param fileId
	 * @param fileLocation
	 * @param createdAt
	 * @param uploadAt
	 * @param adminId
	 * @param subject
	 * @param tag
	 * @param description
	 */
	public FileVO(int fileId, String fileLocation, LocalDateTime createdAt, LocalDateTime uploadAt, int adminId,
			String subject, String tag, String description) {
		this.fileLocation = fileLocation;
		this.createdAt = createdAt;
		this.uploadAt = uploadAt;
		this.adminId = adminId;
		this.subject = subject;
		this.tag = tag;
		this.description = description;
	}

	/**
	 * 객체를 생성하여 DB에 데이터를 저장하기 위한 생성자
	 * @param fileLocation
	 * @param createdAt
	 * @param adminId
	 * @param subject
	 * @param tag
	 * @param description
	 */
	public FileVO(String fileLocation, LocalDateTime createdAt, int adminId, String subject, String tag,
			String description) {
		super();
		this.fileLocation = fileLocation;
		this.createdAt = createdAt;
		this.adminId = adminId;
		this.subject = subject;
		this.tag = tag;
		this.description = description;
	}
	/**
	 * FileDao의 getFileinfoList() 메소드에서 가져올 정보만 모아놓은 생성자
	 * @param createdAt
	 * @param subject
	 * @param tag
	 */
	public FileVO(String title, String tag, Date fileCreateTime) {
	    this.subject = title;
	    this.tag = tag;
	    this.createdAt = fileCreateTime.toLocalDate().atStartOfDay(); // LocalDateTime으로 변환
	}

	public FileVO(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Subject:").append(subject).append("\n")
			.append("tag:").append(tag).append("\n")
			.append("Last Modified Time:").append(createdAt).append("\n")
			.append("File Address:").append(fileLocation).append("\n")
			.append("Description:").append(description).append("\n")
			.append("Server Upload Time:").append(uploadAt).append("\n");
		return sb.toString();
	}

//	public FileVO(String string, String string2, Date date) {
//	}

	// Getter & Setter (필요 시 자동 생성 가능)
	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUploadAt() {
		return uploadAt;
	}

	public void setUploadAt(LocalDateTime uploadAt) {
		this.uploadAt = uploadAt;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

}
