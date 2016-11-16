package GameNationBackEnd;

public class User {
	private String _id;
	private String username;
	private String email;
	private String password;
	private String firstname;
	private String lastname;
	private String teamspeak;
	private String discord;
	private String description;

	public User(){

	}

	public User(String username, String email, String password, String firstname, String lastname, String teamspeak, String discord, String description) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.teamspeak = teamspeak;
		this.discord = discord;
		this.description = description;
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
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public String getTeamspeak() { return teamspeak; }
	public String getDiscord() { return discord; }
	public String getDescription() { return description; }

	public void setId(String id) {
		this._id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setTeamspeak(String teamspeak) { this.teamspeak = teamspeak; }
	public void setDiscord(String discord) { this.discord = discord; }
	public void setDescription(String description) { this.description = description; }
}
