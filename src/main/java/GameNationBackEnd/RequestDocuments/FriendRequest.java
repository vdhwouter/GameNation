package GameNationBackEnd.RequestDocuments;

import GameNationBackEnd.Documents.User;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by lucas on 2/12/2016.
 */
public class FriendRequest {
    @JsonInclude
    public String user;

    public FriendRequest() {}
    public FriendRequest(User user) {
        this.user = user.getId();
    }
}
