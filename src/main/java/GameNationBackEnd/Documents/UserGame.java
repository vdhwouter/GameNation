package GameNationBackEnd.Documents;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by tijs on 17/11/2016.
 */
@Document
public class UserGame {

    @Id
    private String _id;

    @DBRef
    @NotBlank
    private User user;

    @DBRef
    @NotBlank
    private Game game;

    private Integer skill_level;

    public UserGame(){
        user = new User();
        game = new Game();
    }

    public UserGame(User user, Game game, Integer skill_level){
        this.user = user;
        this.game = game;
        this.skill_level = skill_level;
    }

    public String getId() {
        return _id;
    }
    public User getUser(){return this.user; }
    public Game getGame(){return this.game; }
    public int getSkill_level(){return  this.skill_level; }

    public void setUser(User user){ this.user=user; }
    public void setGame(Game game){ this.game=game; }
    public void setSkill_level(Integer skill_level){this.skill_level = skill_level; }


}
