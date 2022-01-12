import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUp extends JFrame {
    private JTextField tcTextField;
    private JPanel signUpPanel;
    private JTextField mailTextField;
    private JTextField surnameTextField;
    private JTextField nameTextField;
    private JButton signUpButton;
    private Database database;
    public SignUp() {
        add(signUpPanel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Sign Up");
        setSize(500,500);
        try {
            database = new Database();
            database.close(database.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        signUpButton.addActionListener(e -> {
            signUpFunction();
            Login login = new Login();
            login.setLocation(getX(), getY());
            setVisible(false);
            login.setVisible(true);
        });
    }

    public void signUpFunction(){

        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement("call insertmember(?,?,?,?,?)");
            preparedStatement.setString(1,tcTextField.getText());
            preparedStatement.setString(2, nameTextField.getText());
            preparedStatement.setString(3, surnameTextField.getText());
            preparedStatement.setString(4, mailTextField.getText());
            preparedStatement.setInt(5, 0);
            preparedStatement.execute();
            JOptionPane.showMessageDialog(null, "Başarıyla eklendi", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Bir hata oluştu", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);

        }
    }
}
