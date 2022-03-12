package UserDao;

import pl.coderslab.entity.User;

import java.sql.SQLException;

public class MainDao {

    public static void main(String[] args) throws SQLException {

//        User newUser1 = new User();
//        newUser1.setUserName("New User");
//        newUser1.setEmail("New Email");
//        newUser1.setPassword("123");
//
//        UserDao userDao1 = new UserDao();
//        userDao1.create(newUser1);

        UserDao userDao2 = new UserDao();
        userDao2.read(7);
    }
}
