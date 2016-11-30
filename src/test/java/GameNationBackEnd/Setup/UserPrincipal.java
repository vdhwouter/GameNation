package GameNationBackEnd.Setup;

import GameNationBackEnd.Documents.User;

import java.security.Principal;

/**
 * Created by lucas on 30/11/2016.
 */
public class UserPrincipal implements Principal {

    private String name;

    public UserPrincipal(String name) {
        this.name = name;
    }

    public UserPrincipal(User user) {
        this.name = user.getUsername();
    }

    @Override
    public String getName() {
        return name;
    }
}