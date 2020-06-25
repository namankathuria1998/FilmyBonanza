package src.com.filmybonanza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This is a POJO class containing all the movie details

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private String title , summary , poster , eventId , typeOfEvent, date;

}
