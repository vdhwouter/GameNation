package GameNationBackEnd.Documents;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by tijs on 17/11/2016.
 */
@Document
public class UserGame {

    @DBRef
    private User user;

    @DBRef
    private Game game;

    private Integer skill_level;

    public UserGame(){
        user = new User();
        game = new Game();
    }

    public User getUser(){return this.user; }
    public Game getGame(){return this.game; }
    public int getSkill_level(){return  this.skill_level; }

    public void setUser(User user){ this.user=user; }
    public void setGame(Game game){ this.game=game; }
    public void setSkill_level(Integer skill_level){this.skill_level = skill_level; }


}
