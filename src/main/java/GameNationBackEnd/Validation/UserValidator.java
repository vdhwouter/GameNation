package GameNationBackEnd.Validation;

import org.springframework.validation.Errors;
import GameNationBackEnd.Documents.User;
import org.springframework.validation.ValidationUtils;
import GameNationBackEnd.Controllers.UsersController;

/**
 * Created by tijs on 28/11/2016.
 */
public class UserValidator implements org.springframework.validation.Validator {

    public UsersController userC = new UsersController();

    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        User u = (User) obj;
        if (userC.GetUser(u) != null) {
            e.rejectValue("username", "username "+ u.getUsername() +" already exists");
        }
    }
}
