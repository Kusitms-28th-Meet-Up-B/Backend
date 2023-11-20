package kusitms.gallae.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserPostsPageRes {
    private List<UserPostDto> userPosts;
    private int totalPages;
}
