package GameNationBackEnd.Security.DevToken;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;

public class UnlimitedRefreshToken implements OAuth2RefreshToken {

    @Override
    public String getValue() {
        return "superrefreshtoken";
    }
}
