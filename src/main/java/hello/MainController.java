package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @Autowired
    private UserRepository repo;

    @RequestMapping("/{name}")
    public String profile(@PathVariable String name, Model model) {
        repo.deleteAll();

        repo.insert(new User("kjell", "This is Kjell's description"));
        repo.insert(new User("matthias", "This is Matthias' description"));
        repo.insert(new User("wouter", "This is Wouter's description"));
        repo.insert(new User("tijs", "This is Tijs' description"));
        repo.insert(new User("lucas", "This is Lucas' description"));

        User user = repo.findByName(name);

        if (user != null) {
            model.addAttribute("user", user);
            return "profile";
        }

        return "no_profile";
    }

    @RequestMapping("/login")
    public String login() {
        return "under_construction";
    }

    @RequestMapping("/register")
    public String register() {
        return "under_construction";
    }
}
