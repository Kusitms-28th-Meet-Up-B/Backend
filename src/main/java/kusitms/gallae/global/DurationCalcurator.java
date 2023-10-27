package kusitms.gallae.global;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DurationCalcurator {
    public static String getDuration(LocalDateTime endDate){
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime dateTime = LocalDate.of(endDate.getYear(), endDate.getMonth() , endDate.getDayOfMonth())
                .atStartOfDay();
        Long remainDay = Duration.between(today,dateTime).toDays();
        if(remainDay > 0) {
            return String.valueOf(remainDay);
        }else{
            return "마감";
        }
    }
}
