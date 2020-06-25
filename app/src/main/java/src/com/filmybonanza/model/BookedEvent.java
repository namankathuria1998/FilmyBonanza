package src.com.filmybonanza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedEvent {

    String eventId , typeOfEvent , dateOfbooking , timeOfBooking , poster , title , uid,date ,timings,location;
}
