package GameNationBackEnd.Validation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import GameNationBackEnd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import GameNationBackEnd.Documents.User;
import org.springframework.validation.ValidationUtils;
import GameNationBackEnd.Controllers.UsersController;

/**
 * Created by tijs on 28/11/2016.
 */
public class UserValidator implements org.springframework.validation.Validator {

    @Autowired
    private UserRepository userDB;

    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        User u = (User) obj;
        if (userDB.findByUsernameIgnoreCase(u.getUsername()) != null) {
            e.rejectValue("username", "username "+ u.getUsername() +" already exists");
        }
    }
}
