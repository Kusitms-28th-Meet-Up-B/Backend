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

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@ModelAttribute UserRegistrationDto registrationDto) {
        try {
            userService.registerNewUser(registrationDto);
            return ResponseEntity.ok(BaseResponseStatus.SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/manager")
    public ResponseEntity<?> registerManager(@ModelAttribute ManagerRegistratiorDto registrationDto) {
        try {
            userService.registerNewManager(registrationDto);
            return ResponseEntity.ok(BaseResponseStatus.SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
