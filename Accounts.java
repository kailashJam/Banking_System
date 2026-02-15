import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email) throws RuntimeException{
        long account_number = 0L;
        if(!account_exists(email)){
            System.out.println();
            scanner.nextLine();
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter age: ");
            while(!scanner.hasNextInt()){
                System.out.println("Please enter valid age!!");
                scanner.nextLine();
            }
            int age = scanner.nextInt();
            System.out.print("Enter initial balance: ");
            while(!scanner.hasNextDouble()){
                System.out.println("Please enter valid balance!!");
                scanner.nextLine();
            }
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter pin: ");
            String pin = scanner.nextLine();
            String open_account_query = "Insert into accounts (name, age, email, balance, pin) values (?, ?, ?, ?, ?)";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, age);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0) {
                    account_number = getAccount_number(email);
                }
                else {
                    throw new RuntimeException("Account creation failed!!!");
                }

            }
            catch (SQLException e){
                e.printStackTrace();
            }

        }
        return account_number;
    }

    public boolean account_exists(String email){
        String exist_check_query = "SELECT * FROM accounts WHERE email = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(exist_check_query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public long getAccount_number(String email){
        long account_number = 0L;
        try{
            String acc_no_query = "SELECT acc_no FROM accounts WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(acc_no_query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                account_number = resultSet.getLong("acc_no");
                return account_number;
            }
            else{
                System.out.println("Invalid email!!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return account_number;
    }
}
