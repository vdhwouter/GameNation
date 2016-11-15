package GameNationBackEnd;

public class User {
	private String _id;
	private String username;
	private String email;
	private String password;
	private String firstname;
	private String lastname;
	private String steam;
	private String discord;
	private String description;

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	 public User(String username, String email, String password, String firstname, String lastnam, String description) {
	 	this.username = username;
	 	this.email = email;
	 	this.password = password;
	 	this.firstname = firstname;
	 	this.lastname = lastname;
	 	this.description = description;
	 }

	 public User(){

	 }

	public String getId() {
		return  _id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
