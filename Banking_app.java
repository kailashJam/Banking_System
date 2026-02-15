import java.sql.*;
import java.util.Scanner;

public class Banking_app {

    public static void main(String[] args) {
        Connection connection = DB_Connection.getConnection();
        Scanner scanner = new Scanner(System.in);
        User user = new User(connection, scanner);
        Accounts accounts = new Accounts(connection, scanner);
        AccountManager accountManager = new AccountManager(connection, scanner);

        String email;
        long account_number;
        int choice;

        control_loop:
        while (true) {
            System.out.println("\n\n=== BANKING SYSTEM MENU ===");
            System.out.println();
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");

            while(!scanner.hasNextInt()){
                System.out.println("Please enter a valid choice!!");
                scanner.nextLine();
            }
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    user.register();
                    break;
                case 2:
                    email = user.login();
                    if (email != null) {
                        System.out.println();
                        System.out.println("User logged in.");
                        if (!accounts.account_exists(email)) {
                            System.out.println();

                            System.out.println("1. Open a new Bank Account.");
                            System.out.println("2. Exit.");

                            while(!scanner.hasNextInt()){
                                System.out.println("Please enter a valid choice.");
                                scanner.nextLine();
                            }

                            if (scanner.nextInt() == 1) {
                                account_number = accounts.open_account(email);
                                System.out.println("Account Created Successfully!!");
                                System.out.println("Your Account Number is: " + account_number);
                            }
                        }

                        account_number = accounts.getAccount_number(email);
                        int choice2 = 0;
                        while (true) {
                            System.out.println();
                            System.out.println("1: Credit Money");
                            System.out.println("2: Debit Money");
                            System.out.println("3: Transfer Money");
                            System.out.println("4: Check balance");
                            System.out.println("5: Logout");
                            System.out.println("6: Delete your account and user data.");
                            System.out.println("Enter A Choice: ");

                            while(!scanner.hasNextInt()){
                                System.out.println("Enter a valid choice!!");
                                scanner.nextLine();
                            }
                            choice2 = scanner.nextInt();
                            try {
                                switch (choice2) {
                                    case 1:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        continue control_loop;
                                    case 6:
                                        accountManager.delete_account(account_number);
                                        user.delete_user(email);
                                        continue control_loop;
                                    default:
                                        System.out.println("Enter A Valid Choice!");
                                        break;
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println("Incorrect email or password!!!");
                        break;
                    }
                case 3:
                    System.out.println("Thank You For Using Banking System.");
                    System.out.println("Exiting system.");
                    return;
                default:
                    System.out.println("Enter Valid Choice.");
                    break;
            }
        }
    }
}
