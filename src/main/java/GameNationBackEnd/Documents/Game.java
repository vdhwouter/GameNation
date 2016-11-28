package GameNationBackEnd.Documents;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by tijs on 16/11/2016.
 */
@Document
public class Game {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotBlank(message = "Image must not be blank")
    private String imageName;

    public Game(){}

    public Game(String name, String description, String imageName) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImageName(){ return imageName; }

    public boolean equals(Game g){
        return this.id.equals(g.id);
    }
}
