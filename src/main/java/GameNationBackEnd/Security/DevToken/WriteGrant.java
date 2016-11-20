package GameNationBackEnd.Security.DevToken;

import org.springframework.security.core.GrantedAuthority;

public class WriteGrant implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return "write";
    }
}
