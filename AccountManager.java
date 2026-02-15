import java.sql.*;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter amount: ");
        while(!scanner.hasNextDouble()){
            System.out.println("Please enter a valid amount!!");
            scanner.nextLine();
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("Select * from Accounts where acc_no = ? and pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()) {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE acc_no = ? AND pin = ?";
                    preparedStatement = connection.prepareStatement(credit_query);
                    preparedStatement.setDouble(1, amount);
                    preparedStatement.setLong(2, account_number);
                    preparedStatement.setString(3, pin);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Rs. " + amount + " credited successfully!");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid credentials!");
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void debit_money (long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter amount: ");
        while(!scanner.hasNextDouble()){
            System.out.println("Please enter a valid amount!!");
            scanner.nextLine();
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE acc_no = ? AND pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount <= current_balance){
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE acc_no = ? AND pin = ?";
                        preparedStatement = connection.prepareStatement(debit_query);
                        preparedStatement.setDouble(1, amount);
                        preparedStatement.setLong(2, account_number);
                        preparedStatement.setString(3, pin);
                        int affectedRows = preparedStatement.executeUpdate();

                        if(affectedRows > 0){
                            System.out.println("Rs. " + amount + " debited successfully!");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient balance!");
                    }
                }
                else{
                    System.out.println("Invalid credentials!");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter receiver account number: ");
        while(!scanner.hasNextLong()){
            System.out.println("Enter a valid account number!!");
            scanner.nextLine();
        }
        long receiver_account_number = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter amount: ");
        while(!scanner.hasNextDouble()){
            System.out.println("Please enter a valid amount.");
            scanner.nextLine();
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security pin: ");
        String pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_no = ? AND pin = ?");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");

                    if(amount<=current_balance) {
                        preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? where acc_no = ? and pin = ?");
                        preparedStatement.setDouble(1, amount);
                        preparedStatement.setLong(2, sender_account_number);
                        preparedStatement.setString(3, pin);
                        int affectedRows1 = preparedStatement.executeUpdate();

                        preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? where acc_no = ?");
                        preparedStatement.setDouble(1, amount);
                        preparedStatement.setLong(2, receiver_account_number);
                        int affectedRows2 = preparedStatement.executeUpdate();

                        if (affectedRows1 > 0 && affectedRows2 > 0) {
                            System.out.println("Rs. " + amount + " transferred successfully.");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("Insufficient balance!!");
                    }
                }
                else{
                    System.out.println("Invalid security pin!!");
                }
            }
            else{
                System.out.println("Invalid account number!!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number){

        scanner.nextLine();
        System.out.println("Enter security pin.");
        String pin = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE acc_no = ? and pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance = Rs." + balance);
            }
            else{
                System.out.println("Invalid pin");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete_account(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Accounts WHERE acc_no = ? AND pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, pin);
                int affected_rows = preparedStatement.executeUpdate();

                if (affected_rows > 0) {
                    System.out.println("Account deleted successfully.");
                    connection.commit();
                    connection.setAutoCommit(true);
                } else {
                    System.out.println("Please enter a valid pin.");
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            }
            else{
                System.out.println("Invalid credentials!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

}
