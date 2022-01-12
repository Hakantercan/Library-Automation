import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;


public class ForgetPassword extends JFrame {


    private JPanel forgetPasswordPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton changeButton;
    private JButton backButton;
    private JButton sendPasswordButton;
    private Database database;
    public ForgetPassword(){
        add(forgetPasswordPanel);
        setSize(500, 500);
        setTitle("Forget Password");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        try{
            database = new Database();
            database.close(database.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        changeButton.addActionListener(e -> updatePassword(check()));
        backButton.addActionListener(e -> {
            Login login = new Login();
            login.setLocation(getX(), getY());
            login.setVisible(true);
            setVisible(false);
        });
        sendPasswordButton.addActionListener(e -> {
            if(mailAddress() == null){
                JOptionPane.showMessageDialog(null, "Kullanıcı Adı Yanlış", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                sendMail(mailAddress());
            }
        });
    }
    public void sendMail(String mailAddress){
        // TODO Auto-generated method stub
        final String username = "libraryversion1.0@gmail.com";
        final String password = "library";

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
       // props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        Authenticator authenticator = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getDefaultInstance(props, authenticator);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailAddress, false));
            Random random=new Random();
            int newPassword = random.nextInt(900000) + 99999;
            message.setSubject("Şifre Sıfırlama");
            message.setText("Yeni Şifreniz: " + newPassword);
            Transport.send(message);
            JOptionPane.showMessageDialog(null, "Mailinize yeni şifreniz iletildi", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public String mailAddress(){
        String query = "Select membermail from members where membertc = ?";
        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, textField1.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("membermail");
            }
            else{

            }
            database.close(database.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }
    public void updatePassword(Boolean check){
        if(check){
            String query = "update memberslogin set memberpassword = ?  where membertc= ?";
            char[] input = passwordField2.getPassword();
            String password = new String(input);
            try {
                database.setConnection();
                PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
                preparedStatement.setString(1, password);
                preparedStatement.setString(2, textField1.getText());
                preparedStatement.execute();
                JOptionPane.showMessageDialog(null, "Başarıyla Değiştirildi", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Kullanıcı Adı veya Şifre yanlış", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public boolean check(){
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
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
        return returnTemp;
    }

    public JTextField getTextField1() {
        return textField1;
    }
}
