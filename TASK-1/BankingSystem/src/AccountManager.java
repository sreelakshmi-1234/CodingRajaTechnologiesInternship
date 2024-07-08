import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    Connection conn;
    Scanner sc;

    AccountManager(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void credit_money(long account_no) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            conn.setAutoCommit(false);
            if (account_no != 0) {
                PreparedStatement pmst = conn.prepareStatement("SELECT * FROM accounts WHERE account_no = ? and security_pin = ? ");
                pmst.setLong(1, account_no);
                pmst.setString(2, security_pin);
                ResultSet res = pmst.executeQuery();

                if (res.next()) {
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";
                    PreparedStatement pmst1 = conn.prepareStatement(credit_query);
                    pmst1.setDouble(1, amount);
                    pmst1.setLong(2, account_no);
                    int affectedRows = pmst1.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Rs." + amount + " credited Successfully");
                        conn.commit();
                        conn.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid Security Pin!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during credit: " + e.getMessage());
        }
        conn.setAutoCommit(true);
    }

    public void debit_money(long account_no) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try {
            conn.setAutoCommit(false);
            if (account_no != 0) {
                PreparedStatement pmst = conn.prepareStatement("SELECT * FROM accounts WHERE account_no = ? and security_pin = ? ");
                pmst.setLong(1, account_no);
                pmst.setString(2, security_pin);
                ResultSet res = pmst.executeQuery();

                if (res.next()) {
                    double current_balance = res.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
                        PreparedStatement pmst1 = conn.prepareStatement(debit_query);
                        pmst1.setDouble(1, amount);
                        pmst1.setLong(2, account_no);
                        int rowsAffected = pmst1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs." + amount + " debited Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    System.out.println("Invalid Pin!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during debit: " + e.getMessage());
        }
        conn.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_no) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Receiver Account no: ");
        long receiver_account_no = sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try {
            conn.setAutoCommit(false);
            if (sender_account_no != 0 && receiver_account_no != 0) {
                PreparedStatement pmst = conn.prepareStatement("SELECT * FROM accounts WHERE account_no = ? AND security_pin = ? ");
                pmst.setLong(1, sender_account_no);
                pmst.setString(2, security_pin);
                ResultSet res = pmst.executeQuery();

                if (res.next()) {
                    double current_balance = res.getDouble("balance");
                    if (amount <= current_balance) {

                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";

                        PreparedStatement creditpmst = conn.prepareStatement(credit_query);
                        PreparedStatement debitpmst = conn.prepareStatement(debit_query);

                        // Set Values for debit and credit prepared statements
                        creditpmst.setDouble(1, amount);
                        creditpmst.setLong(2, receiver_account_no);
                        debitpmst.setDouble(1, amount);
                        debitpmst.setLong(2, sender_account_no);
                        int rowsAffected1 = debitpmst.executeUpdate();
                        int rowsAffected2 = creditpmst.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs." + amount + " Transferred Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    System.out.println("Invalid Security Pin!");
                }
            } else {
                System.out.println("Invalid account number");
            }
        } catch (SQLException e) {
            System.out.println("Error during transfer: " + e.getMessage());
        }
        conn.setAutoCommit(true);
    }

    public void getBalance(long account_no) {
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try {
            PreparedStatement pmst = conn.prepareStatement("SELECT balance FROM accounts WHERE account_no = ? AND security_pin = ?");
            pmst.setLong(1, account_no);
            pmst.setString(2, security_pin);
            ResultSet resultSet = pmst.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: " + balance);
            } else {
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving balance: " + e.getMessage());
        }
    }
}
