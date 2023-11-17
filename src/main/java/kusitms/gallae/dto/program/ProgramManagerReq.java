package kusitms.gallae.dto.program;


import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.User;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class ProgramManagerReq {

    private String programType;

    private Program.ProgramStatus status;

    private User user;

    private Pageable pageable;
}
