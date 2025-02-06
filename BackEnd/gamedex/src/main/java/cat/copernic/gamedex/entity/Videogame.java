package cat.copernic.gamedex.entity;

import java.time.LocalDate;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "videogame")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Videogame {
    
    @Id
    private String gameId;

    private String nameGame;

    private String descriptionGame;

    private LocalDate releaseYear;
    
    private Binary gamePhoto;

    private int ageRecomendation;

    private String developer;

    private String nameCategory;

}
