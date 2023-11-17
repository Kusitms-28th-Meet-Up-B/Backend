package kusitms.gallae.dto.program;


import lombok.Data;

import java.util.List;

@Data
public class ProgramPageMangagerRes {

    private Integer totalSize;
    private List<ProgramManagerRes> programs;
}
