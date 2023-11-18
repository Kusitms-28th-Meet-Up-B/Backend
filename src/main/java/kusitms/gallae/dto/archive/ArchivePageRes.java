package kusitms.gallae.dto.archive;

import lombok.Data;

import java.util.List;

@Data
public class ArchivePageRes {
    private Integer totalSize;
    private List<ArchiveDtoRes> archives;


}
