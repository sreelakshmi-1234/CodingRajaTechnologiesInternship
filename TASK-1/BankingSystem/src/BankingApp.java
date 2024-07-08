import java.util.Scanner;
import java.sql.*;

public class BankingApp {
    public static void main(String args[]) {1
        
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
            return; // Exit the application if the driver is not found
        }

        try {
            // Establish a connection to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "password");
            Scanner sc = new Scanner(System.in);
            User ur = new User(conn, sc);
            Accounts ac = new Accounts(conn, sc);
            AccountManager am = new AccountManager(conn, sc);

            String email;
            long account_no;

            while (true) {
                System.out.println("***  WELCOME TO BANKING SYSTEM  ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int ch = sc.nextInt();
                switch (ch) {
                    case 1:
                        ur.register();
                        System.out.println();
                        System.out.println();
                        break;
                    case 2:
                        email = ur.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In!");
                            if (!ac.account_exist(email)) {
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if (sc.nextInt() == 1) {
                                    account_no = ac.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account number is: " + account_no);
                                } else {
                                    break;
                                }
                            }
                            account_no = ac.getaccount_number(email);
                            int ch1 = 0;
                            while (ch1 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");
                                ch1 = sc.nextInt();
                                switch (ch1) {
                                    case 1:
                                        am.debit_money(account_no);
                                        break;
                                    case 2:
                                        am.credit_money(account_no);
                                        break;
                                    case 3:
                                        am.transfer_money(account_no);
                                        break;
                                    case 4:
                                        am.getBalance(account_no);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password!");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
