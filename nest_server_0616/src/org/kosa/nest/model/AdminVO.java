package org.kosa.nest.model;

public class AdminVO {

	private int id;
	private String email;
	private String password;
	
	public AdminVO(int id, String email, String password) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
	}

	public AdminVO(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("admin id:").append(id).append("\n")
			.append("email:").append(email).append("\n");
		return sb.toString();
	}
		
}
