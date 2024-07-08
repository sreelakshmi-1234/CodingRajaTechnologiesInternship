import java.sql.*;
import java.util.Scanner;

public class User {
    Connection conn;
    Scanner sc;

    public User(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void register() {
        sc.nextLine(); // Clear the buffer
        System.out.print("Full Name: ");
        String full_name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        if (user_exist(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }
        String register_query = "INSERT INTO user(full_name, email, password) VALUES(?, ?, ?)";
        try {
            PreparedStatement pmst = conn.prepareStatement(register_query);
            pmst.setString(1, full_name);
            pmst.setString(2, email);
            pmst.setString(3, password);
            int affectedRows = pmst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Registration Successful!");
            } else {
                System.out.println("Registration Failed!");
            }
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    public String login() {
        sc.nextLine(); // Clear the buffer
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try {
            PreparedStatement pmst = conn.prepareStatement(login_query);
            pmst.setString(1, email);
            pmst.setString(2, password);
            ResultSet res = pmst.executeQuery();
            if (res.next()) {
                return email;
            } else {
                System.out.println("Incorrect Email or Password!");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            return null;
        }
    }

    public boolean user_exist(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement pmst = conn.prepareStatement(query);
            pmst.setString(1, email);
            ResultSet res = pmst.executeQuery();
            return res.next();
        } catch (SQLException e) {
            System.out.println("Error checking if user exists: " + e.getMessage());
            return false;
        }
    }
}
