import java.sql.*;
import java.util.Scanner;

public class Accounts {
    Connection conn;
    Scanner sc;

    public Accounts(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public long open_account(String email) {
        if (!account_exist(email)) {
            String open_account_query = "INSERT INTO accounts(account_no, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = sc.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = sc.nextLine();
            try {
                long account_no = generateAccountNumber();
                PreparedStatement pmst = conn.prepareStatement(open_account_query);
                pmst.setLong(1, account_no);
                pmst.setString(2, full_name);
                pmst.setString(3, email);
                pmst.setDouble(4, balance);
                pmst.setString(5, security_pin);
                int affectedRows = pmst.executeUpdate();
                if (affectedRows > 0) {
                    return account_no;
                } else {
                    throw new RuntimeException("Account Creation failed!!");
                }
            } catch (SQLException e) {
                System.out.println("Error opening account: " + e.getMessage());
            }
        }
        throw new RuntimeException("Account Already Exist");
    }

    public long getaccount_number(String email) {
        String query = "SELECT account_no from accounts WHERE email = ?";
        try {
            PreparedStatement pmst = conn.prepareStatement(query);
            pmst.setString(1, email);
            ResultSet res = pmst.executeQuery();
            if (res.next()) {
                return res.getLong("account_no");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account number: " + e.getMessage());
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    private long generateAccountNumber() {
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery("SELECT account_no from accounts ORDER BY account_no DESC LIMIT 1");
            if (res.next()) {
                long last_account_no = res.getLong("account_no");
                return last_account_no + 1;
            } else {
                return 10000100;
            }
        } catch (SQLException e) {
            System.out.println("Error generating account number: " + e.getMessage());
        }
        return 10000100;
    }

    public boolean account_exist(String email) {
        String query = "SELECT account_no from accounts WHERE email = ?";
        try {
            PreparedStatement pmst = conn.prepareStatement(query);
            pmst.setString(1, email);
            ResultSet res = pmst.executeQuery();
            return res.next();
        } catch (SQLException e) {
            System.out.println("Error checking if account exists: " + e.getMessage());
        }
        return false;
    }
}
