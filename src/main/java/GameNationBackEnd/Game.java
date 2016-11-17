package GameNationBackEnd;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by tijs on 16/11/2016.
 */
@Document
public class Game {
    private String _id;
    private String name;
    private String description;
    private String imageName;

    public Game(){}
    public Game(String name, String description, String imageName) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
    }

    public String GetImagePath(){
        return "./img/" + imageName;
    }
    public String getId() {
        return  _id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public String getImageName(){ return imageName; }
}
