package kusitms.gallae.dto.program;

import kusitms.gallae.domain.User;
import lombok.Data;

@Data
public class ProgramSimilarReq {
    private Long id;
    private String location;
    private String programType;
}