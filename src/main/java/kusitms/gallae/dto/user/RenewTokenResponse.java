package kusitms.gallae.dto.user;

import lombok.Data;

@Data
public class RenewTokenResponse {

    private String accessToken;

    private String refreshToken;
}
