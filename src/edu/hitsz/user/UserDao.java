package edu.hitsz.user;

import java.util.ArrayList;

public interface UserDao {
    ArrayList<User> getAllUsers();
    void doAdd(User user);
    void doDelete(User u);
}
