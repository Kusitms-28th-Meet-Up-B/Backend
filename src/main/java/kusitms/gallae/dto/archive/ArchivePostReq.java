package kusitms.gallae.dto.archive;

import lombok.Data;

@Data
public class ArchivePostReq {
    private String title;

    private String category;

    private String body;

    private String hashTags;

    private String fileName;

    private String fileUrl;
    private String writer;
}
