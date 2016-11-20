package GameNationBackEnd.Security.DevToken;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.*;

/**
 * Created by lucas on 20/11/2016.
 */
public class UnlimitedToken implements OAuth2AccessToken {

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<String, Object>();
    }

    @Override
    public Set<String> getScope() {
        return new HashSet<String>(Arrays.asList("read", "write"));
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return new UnlimitedRefreshToken();
    }

    @Override
    public String getTokenType() {
        return "bearer";
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public Date getExpiration() {
        return new Date(1000*1000*1000*1000);
    }

    @Override
    public int getExpiresIn() {
        return 1000*1000*1000;
    }

    @Override
    public String getValue() {
        return "supertoken";
    }
}

