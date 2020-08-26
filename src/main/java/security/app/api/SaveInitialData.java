package security.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import security.app.api.entity.AppUser;
import security.app.api.service.UserService;

import javax.annotation.PostConstruct;

@Component
public class SaveInitialData {
    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() throws Exception {
        if (userService.getUsers().isEmpty()) {
            userService.addUser(new AppUser("admin", "admin"));
            userService.addUser(new AppUser("user", "user"));
            userService.addUser(new AppUser("elmahdi_ezzahir", "12345678", "El Mahdi", "Ezzahir"));
        }
    }
}
