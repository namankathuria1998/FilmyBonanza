package src.com.filmybonanza;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatAvailability {

        String location_timings_seat , isLockAcquired , isBooked;

//        @DynamoDBVersionAttribute
        String uid;
}
