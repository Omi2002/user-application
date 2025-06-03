package user.com.user.services;

import java.util.List;
import java.util.Optional;

// import org.hibernate.query.Page;


import user.com.user.model.User;

public interface UserServiceInterface {
    public Optional<User> getUserById(int id);

    public User addUser(User user);

    public void deleteUser(Integer id);

    public User updateUser(User user, Integer id);

    //public List<User> getAllUsers();

    public List<User> getAllUsers();
}
