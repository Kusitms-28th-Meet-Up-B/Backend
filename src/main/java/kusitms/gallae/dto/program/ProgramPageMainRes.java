package kusitms.gallae.dto.program;


import lombok.Data;

import java.util.List;

@Data
public class ProgramPageMainRes {
    private Integer totalSize;
    private List<ProgramMainRes> programs;
}
