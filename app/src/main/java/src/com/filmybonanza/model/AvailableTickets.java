package src.com.filmybonanza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTickets {

    String location_timings , availTickets , seatMatrix;
}
