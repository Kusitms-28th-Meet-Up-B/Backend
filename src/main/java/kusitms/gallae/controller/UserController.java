package kusitms.gallae.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.user.ManagerRegistratiorDto;
import kusitms.gallae.dto.user.UserRegistrationDto;
import kusitms.gallae.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
}
