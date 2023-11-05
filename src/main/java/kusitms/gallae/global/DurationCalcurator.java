package kusitms.gallae.global;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DurationCalcurator {
    public static String getDuration(LocalDate endDate){
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        Long remainDay = Duration.between(today,endDate.atStartOfDay()).toDays();
        if(remainDay > 0) {
            return String.valueOf(remainDay);
        }else{
            return "마감";
        }
    }
}
