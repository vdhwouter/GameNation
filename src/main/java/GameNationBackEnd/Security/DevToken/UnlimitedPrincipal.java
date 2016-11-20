package GameNationBackEnd.Security.DevToken;

import java.security.Principal;

public class UnlimitedPrincipal implements Principal {
    @Override
    public String getName() {
        return "test";
    }
}
