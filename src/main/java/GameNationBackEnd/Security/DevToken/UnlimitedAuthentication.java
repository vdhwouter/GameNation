package GameNationBackEnd.Security.DevToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

public class UnlimitedAuthentication implements Authentication {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new WriteGrant());
    }

    @Override
    public Object getCredentials() {
        return "credentials";
    }

    @Override
    public Object getDetails() {
        return "details";
    }

    @Override
    public Object getPrincipal() {
        return new UnlimitedPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return "test";
    }
}
