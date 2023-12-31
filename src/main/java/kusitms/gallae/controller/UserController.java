package kusitms.gallae.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.user.ManagerRegistratiorDto;
import kusitms.gallae.dto.user.UserPostDto;
import kusitms.gallae.dto.user.UserPostsPageRes;
import kusitms.gallae.dto.user.UserRegistrationDto;
import kusitms.gallae.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/user")
    public ResponseEntity<BaseResponse> registerUser(@ModelAttribute UserRegistrationDto registrationDto) throws IOException {
        userService.registerNewUser(registrationDto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
    }

    @PostMapping("/register/manager")
    public ResponseEntity<BaseResponse> registerManager(@ModelAttribute ManagerRegistratiorDto registrationDto) throws IOException {
        userService.registerNewManager(registrationDto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));

    }

    @Operation(summary = "닉네임 중복 체크", description = """
            중복되면 true,
            아니면 False;
            
            """)
    @GetMapping("/checkNickname")
    public  ResponseEntity<BaseResponse<Boolean>> checkNickName(
            @Parameter(description = "닉네임", example = "이름")
            @RequestParam(value = "NickName")
            String nickName
    ) {
        return ResponseEntity.ok(new BaseResponse<>(userService.checkDuplicateNickName(nickName)));
    }

    @Operation(summary = "로그인 중복 체크", description = """
            중복되면 true,
            아니면 False;
            
            """)
    @GetMapping("/checkLoginId")
    public  ResponseEntity<BaseResponse<Boolean>> checkLoginId(
            @Parameter(description = "아이", example = "이름")
            @RequestParam(value = "loginId")
            String loginId
    ) {
        return ResponseEntity.ok(new BaseResponse<>(userService.checkDuplicateLoginId(loginId)));
    }


    @Operation(summary = "내가 작성한 자료실")
    @GetMapping("/myPosts/archive")
    public ResponseEntity<BaseResponse<UserPostsPageRes>> getMyPostByArchive(
            Principal principal,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pagingSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        return ResponseEntity.ok(new BaseResponse<>(userService.getUserPostByArchive(principal.getName(), pageRequest)));
    }

    @Operation(summary = "내가 작성한 지언후기")
    @GetMapping("/myPosts/review")
    public ResponseEntity<BaseResponse<UserPostsPageRes>> getMyPostByReview(
            Principal principal,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pagingSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        return ResponseEntity.ok(new BaseResponse<>(userService.getUserPostByReview(principal.getName(), pageRequest)));
    }

}
