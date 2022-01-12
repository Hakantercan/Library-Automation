import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JPanel loginPanel;
    private JButton memberButton;
    private JPasswordField passwordField1;
    private JTextField textField1;
    private JButton signupButton;
    private JLabel forgetPassword;
    private int memberId;
    Database database;
    public Login(){
        try{
            database = new Database();
            database.close(database.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        add(loginPanel);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Login");
        memberButton.addActionListener(e -> {
            if(memberLogin()){
                MemberMenu memberMenu = new MemberMenu(whatIsTheId());
                memberMenu.setLocation(getX(), getY());
                memberMenu.setVisible(true);
                setVisible(false);
            }
            else{
                JOptionPane.showMessageDialog(null, "Kullanıcı Adı veya Şifre yanlış", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);
            }
        });
        signupButton.addActionListener(e -> {
            SignUp signUp = new SignUp();
            signUp.setLocation(getX(), getY());
            signUp.setVisible(true);
            setVisible(false);
        });
        forgetPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ForgetPassword forgetPassword = new ForgetPassword();
                forgetPassword.setLocation(getX(), getY());
                forgetPassword.setVisible(true);
                setVisible(false);
            }
        });
    }
    public int whatIsTheId(){
        String query = "Select memberid from members where membertc = ?";
        try {
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, getTextField1().getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                memberId = resultSet.getInt("memberid");
            }
            database.close(database.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return  memberId;
    }
    public boolean memberLogin(){
        String Id = getTextField1().getText();
        boolean returnTemp;
        char[] input = passwordField1.getPassword();
        String password = new String(input);
        try {
            String query = "Select membertc, memberpassword from memberslogin where membertc = ? and memberpassword = ?" ;
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.setString(1,Id);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            returnTemp = resultSet.next();
            database.close(database.getConnection());
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return returnTemp;
    }
    public JTextField getTextField1() {
        return textField1;
    }
}
