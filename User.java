import java.sql.*;
import java.util.Scanner;

public class User {

    private Connection connection;
    private Scanner scanner;

    public User (Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if(user_exists(email)){
            System.out.println("User already exists for this email address!!");
            return;
        }

        String register_query = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Registered successfully!!");
            }
            else{
                System.out.println("User registration failed!!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String login() {
        scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return email;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exists (String email){
        String query = "SELECT * FROM user WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void delete_user(String email){

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE email = ?");
            preparedStatement.setString(1, email);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("User deleted successfully!!");
            }
            else{
                System.out.println("User deletion failed!!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
