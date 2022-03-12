package UserDao;

import pl.coderslab.entity.User;

import java.sql.*;
import java.util.Scanner;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

public class UserDao {

    public static final String READ_USER_QUERY = "select * from users where id = ?";

    public static void main(String[] args) {

        menu();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String komenda = scanner.nextLine();

            if (equalsIgnoreCase(komenda, "u")) {
                delete();

            } else if (equalsIgnoreCase(komenda, "e")) {
                update();

            } else if (equalsIgnoreCase(komenda, "x")) {
                break;

            } else if (equalsIgnoreCase(komenda, "a")) {
                addUser();

            } else {
                menu();
            }
        }

    }

    // DODAWANIE UŻYTKOWNIKA
    public static void addUser() {

        Scanner scanEmail = new Scanner(System.in);
        System.out.println("Podaj adres email");
        String email = scanEmail.nextLine();
        Scanner scanUserName = new Scanner(System.in);
        System.out.println("Podaj nazwę użytkownika");
        String userName = scanUserName.nextLine();
        Scanner scanPassword = new Scanner(System.in);
        System.out.println("Podaj hasło");
        String password = scanPassword.nextLine();

        try (Connection connection = DbUtil.getConnection()) {

            int firstProductId = DbUtil.insert(connection, "insert into users (email, username, password) values (?, ?, ?);",
                    email, userName, password);
            System.out.println("Użytkownik został dodany");
            menu();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    // USUWANIE PODANEGO WIERSZA
    public static void delete() {

        Scanner scanName = new Scanner(System.in);
        System.out.println("Podaj id użytkownika, którego chcesz usunąć");
        int deleteID = scanName.nextInt();

        try (Connection connection = DbUtil.getConnection()) {

            DbUtil.remove(connection, "users", deleteID);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Użytkownik o numerze id " + deleteID + " został usunięty");
        menu();
    }

    // UPDATE
    public static void update() {

        System.out.println("Podaj id rekordu do zmiany");
        Scanner updateID = new Scanner(System.in);
        int id = updateID.nextInt();
        System.out.println("Podaj nowy email");
        Scanner emailUpdate = new Scanner(System.in);
        String email = emailUpdate.nextLine();
        System.out.println("Podaj nową nazwę użytkownika");
        Scanner userNameUpdate = new Scanner(System.in);
        String userName = userNameUpdate.nextLine();
        System.out.println("Podaj nowe hasło");
        Scanner passwordUpdate = new Scanner(System.in);
        String password = passwordUpdate.nextLine();

        try (Connection connection = DbUtil.getConnection()) {

            DbUtil.updateData(connection, "users", id, email, userName, password);
            System.out.println("Dane zostały zaktualizowane");
            menu();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INTERFEJS
    public static void menu() {
        try (Connection connection = DbUtil.getConnection()) {

            DbUtil.printData(connection, "select * from users", "id", "email", "username", "password");

        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n\n Proszę wybrać jedną z opcji: \n\n U - usunięcie rekordu \n E - edycja rekordu " +
                "\n A - dodanie rekordu" + "\n X - wyjście z programu \n");
    }

    public String hashPassword(String password) {
        return org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
    }
    public static final String CREATE_USER_QUERY = "insert into users (username, email, password) value (?, ? , ?)";

    public User create(User user) {

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));

            statement.executeUpdate();

            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }

    }

    public User read(int userId) throws SQLException {

        User readUser = new User();

        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(READ_USER_QUERY);

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                readUser.setId(userId);
                readUser.setPassword(resultSet.getString("password"));
                readUser.setUserName(resultSet.getString("username"));
                readUser.setEmail(resultSet.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    return readUser;
    }


}
