package GameNationBackEnd.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by tijs on 16/11/2016.
 */
@Document
public class Game implements Comparable<Game>{
    @Id
    private String _id;

    @Indexed(unique = true)
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
        return _id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImageName(){ return imageName; }


    public boolean equals(Game g){
        return this._id.equals(g._id);
    }

    @Override
    public int compareTo(Game g){
        return this.name.compareTo(g.name);
    }



}
