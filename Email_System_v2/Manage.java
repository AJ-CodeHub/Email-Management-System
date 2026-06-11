package Email_Management.Email_System_v2;

import java.util.Scanner;
import java.sql.*;

public class Manage {

    static Scanner sc = new Scanner(System.in);

    static void clearScreen(){

        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void deleteEmail(int id){

        String sql =
                "DELETE FROM emails WHERE id=?";

        try(
                Connection conn = Database.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ){

            ps.setInt(1,id);

            ps.executeUpdate();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteUser(User currentUser){

        try(
                Connection conn =
                        Database.connect()
        ){

            PreparedStatement deleteEmails =
                    conn.prepareStatement(
                            "DELETE FROM emails WHERE sender=? OR receiver=?"
                    );

            deleteEmails.setString(
                    1,
                    currentUser.Name
            );

            deleteEmails.setString(
                    2,
                    currentUser.Name
            );

            deleteEmails.executeUpdate();

            PreparedStatement deleteUser =
                    conn.prepareStatement(
                            "DELETE FROM users WHERE username=?"
                    );

            deleteUser.setString(
                    1,
                    currentUser.Name
            );

            deleteUser.executeUpdate();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    static ResultSet getInbox(User currentUser){

        String sql =
                """
                SELECT *
                FROM Emails
                WHERE receiver = ?
                ORDER BY sent_time DESC
                """;

        try{

            Connection conn = Database.connect();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, currentUser.Name);

            return ps.executeQuery();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    static void OpenEmail(int id){

        String sql =
                "SELECT * FROM Emails WHERE id=?";

        try(
                Connection conn = Database.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ){

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                System.out.println("\n=================================");
                System.out.println("FROM: " + rs.getString("sender"));
                System.out.println("TO: " + rs.getString("receiver"));
                System.out.println("TIME: " + rs.getTimestamp("sent_time").toLocalDateTime()
                        .format(java.time.format.DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm")));
                System.out.println("SUBJECT: " + rs.getString("subject"));
                System.out.println("=================================");
                System.out.println(rs.getString("body"));
                System.out.println("=================================\n");
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    static void ViewInbox(User currentUser){

        String sql =
                """
                SELECT *
                FROM Emails
                WHERE receiver = ?
                ORDER BY sent_time DESC
                """;

        try(
                Connection conn = Database.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ){

            ps.setString(1, currentUser.Name);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n=================================");
            System.out.println("             INBOX");
            System.out.println("=================================");

            int count = 0;
            int[] emailIds = new int[100];

            while(rs.next()){

                Timestamp time = rs.getTimestamp("sent_time");
                emailIds[count] = rs.getInt("id");

                System.out.println(
                        (count+1) + ") " +
                                rs.getString("sender") + " | " +
                                rs.getString("subject") + " | " +
                                time.toLocalDateTime()
                                        .format(java.time.format.DateTimeFormatter
                                                        .ofPattern("dd-MM-yyyy HH:mm")
                                        )
                );

                count++;
            }

            if(count == 0){
                System.out.println("Inbox Empty...");
            }

            System.out.println("=================================");
            System.out.print("Open Email #: \n→ ");
            int choice = sc.nextInt();

            if(choice < 1 || choice > count){
                return;
            }

            OpenEmail(
                    emailIds[choice-1]
            );

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    static boolean userExists(String username){

        String sql =
                "SELECT * FROM Users WHERE username=?";

        try(
                Connection conn = Database.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ){

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    static void sendEmail(String sender,
                          String receiver,
                          String subject,
                          String body){
        String sql =
                """
                INSERT INTO Emails
                (sender, receiver, subject, body, sent_time)
                VALUES (?, ?, ?, ?, NOW())
                """;
        try(
                Connection conn = Database.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ){

            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, subject);
            ps.setString(4, body);

            ps.executeUpdate();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    static User getUser(String username, String password){

        String sql = "SELECT * FROM Users WHERE username=? AND password=?";

        try(
                Connection conn = Database.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                User u = new User();

                u.Name = rs.getString("username");
                u.Password = rs.getString("password");

                return u;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    static String addUser(String username, String password){

        String sql = "INSERT INTO Users(username,password) VALUES(?,?)";

        try(
                Connection conn = Database.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ){

            ps.setString(1, username);
            ps.setString(2, password);

            ps.executeUpdate();

            return "User registered successfully!";

        }
        catch(SQLException e){

            if(e.getErrorCode() == 1062){
                return "User already exists!";
            }

            e.printStackTrace();
        }

        return "Something went wrong!";


    }

    static void ComposeMail(User currentUser){

        System.out.println("\n=================================");
        System.out.println("         COMPOSE MAIL");
        System.out.println("=================================");
        System.out.print("Receiver: \n→ ");
        String receiverName = sc.next();
        sc.nextLine();

        if(!userExists(receiverName)){
            System.out.println("User was not found");
            return;
        }

        System.out.print("Subject: \n→ ");
        String subject = sc.nextLine();

        System.out.print("Body: \n→ ");
        String body = sc.nextLine();

        sendEmail(
                currentUser.Name,
                receiverName,
                subject,
                body
        );

        System.out.println("\n=================================");
        System.out.println("     EMAIL SENT SUCCESSFULLY");
        System.out.println("=================================\n");

    }

    static void Welcome(User currentUser) {

        while(true) {
            int n;

            System.out.println("\n=================================");
            System.out.println(" WELCOME, " + currentUser.Name.toUpperCase());
            System.out.println("=================================");
            System.out.println("1) Compose Mail");
            System.out.println("2) View Inbox");
            System.out.println("3) Logout");
            System.out.println("=================================");
            System.out.print("→ ");
            n = sc.nextInt();
            System.out.println();

            switch (n) {
                case 1:
                    ComposeMail(currentUser);
                    break;
                case 2:
                    ViewInbox(currentUser);
                    break;
                case 3:
                    System.out.println("See you later!\n");
                    return;
                default:
                    System.out.println("Invalid Option!\n");
            }
        }
    }

    static void SignUp(){

        System.out.println("=================================");
        System.out.println("            SIGN UP ");
        System.out.println("=================================");
        System.out.print("Enter the Username: \n→ ");
        String Name = sc.next();

        System.out.print("Enter the Password: \n→ ");
        String Password = sc.next();

        addUser(Name, Password);

    }

    static User LogIn(){

        System.out.println("=================================");
        System.out.println("             LOGIN ");
        System.out.println("=================================");
        System.out.print("Enter the Username: \n→ ");
        String Name = sc.next();

        System.out.print("Enter the Password: \n→ ");
        String Password = sc.next();

        User u = getUser(Name, Password);

        if(u != null){
            System.out.println("=================================");
            System.out.println("         LOGIN SUCCESSFUL");
            System.out.println("=================================");
            return u;
        }

        System.out.println("No User Found!\n");
        return null;


    }

    static void Home(){

        System.out.println("=================================");
        System.out.println("         EMAIL SYSTEM");
        System.out.println("=================================");
        System.out.println("1) Sign Up");
        System.out.println("2) Log In");
        System.out.println("=================================");
        System.out.print("→ ");
        int n = sc.nextInt();

        switch (n){
            case 1 : clearScreen(); SignUp(); break;
            case 2 :
                clearScreen();
                User currentUser = LogIn();

                if(currentUser != null){
                    clearScreen();
                    Welcome(currentUser);
                }
                break;
            default: System.out.println("Invalid Option!\n");
        }
    }

    static void main() {
        while(true){
            clearScreen();
            Home();
        }
    }
}