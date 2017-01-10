package GameNationBackEnd.Documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class User{
	@Id
	private String _id;

	@Indexed(unique = true)
	@Email(message = "Email must be a valid email")
	@NotBlank(message = "Email must not be blank")
	private String email;

	@Indexed(unique = true)
	@NotBlank(message = "Username must not be blank")
	private String username;

	@NotBlank(message = "Password must not be blank")
	private String password;

	private String firstname;
	private String lastname;
	private String description;

	private int level;
	private String discord;
	private String teamspeak;

    private String avatar;


	// only set if authenticated user has sent or received a friend request to this user
	// it shows the current relationship state
	// and can be used to show UI for cancel/accept/show friend request
	private Friend relation;

	public User(){}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.avatar = "img/avatar-member.jpg";
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
	public int getLevel() { return level; }
	public Friend getRelation() { return relation; }
    public String getAvatar() {return avatar; }

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
	public void setLevel(int level) { this.level = level; }
	public void setRelation(Friend relation) { this.relation = relation; }
    public void setAvatar (String avatar) {this.avatar = avatar;}
}
