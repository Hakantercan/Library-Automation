import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import static java.lang.System.out;

public class MemberMenu extends JFrame {
    private JPanel memberMenuPanel;
    private JTable table1;
    private JButton searchBookButton;
    private JButton myBookButton;
    private JButton borrowBookButton;
    private JButton logOutButton;
    private JButton bookListButton;
    private JButton giveBookButton;
    private final int memberId;
    private String operationBookISBN;
    private Database database;
    private ArrayList<BookList> books;
    private ArrayList<MyBookList> myBooks;
    public MemberMenu(int id) {
        try {
            database = new Database();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        books = new ArrayList<>();
        myBooks = new ArrayList<>();
        memberId = id;
        books = ArrayListBook(books);
        myBooks = ArrayListMyBook(myBooks, memberId);
        try{
            database.close(database.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        add(memberMenuPanel);
        setTitle("Member Menu");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        logOutButton.addActionListener(e -> {
            Login login = new Login();
            login.setLocation(getX(), getY());
            setVisible(false);
            login.setVisible(true);
        });
        searchBookButton.addActionListener(e -> {
            SearchBookForMembers searchBookForMembers = new SearchBookForMembers(memberId);
            searchBookForMembers.setLocation(getX(), getY());
            searchBookForMembers.setVisible(true);
            setVisible(false);
        });

        borrowBookButton.addActionListener(e -> takeBook(operationBookISBN));

        giveBookButton.addActionListener(e -> {
            try{
                database.setConnection();
                giveBook(operationBookISBN);
                database.close(database.getConnection());
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        });
        bookListButton.addActionListener(e -> createTable());
        myBookButton.addActionListener(e -> createTableForMyBooks());

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int check = table1.getSelectedRow();
                operationBookISBN = (String) table1.getModel().getValueAt(check, 0);
            }
        });
    }

    public void createTable(){
        String[] columnNames = {"ISBN", "Kitap Adı", "Yazar Adı", "Sayfa Sayısı", "Ödünç Durumu"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // or a condition at your choice with row and column
            }
        };
        Object[] rowData = new Object[5];
        for(BookList bookList : books){
            rowData[0] = bookList.getISBN();
            rowData[1] = bookList.getBookName();
            rowData[2] = bookList.getAuthorName();
            rowData[3] = bookList.getPageSize();
            rowData[4] = bookList.isBookState();
            defaultTableModel.addRow(rowData);
        }
        table1.setModel(defaultTableModel);
    }
    public void createTableForMyBooks(){
        String[] columnNames = {"ISBN", "Kitap Adı", "Yazar Adı", "Sayfa Sayısı", "Ödünç Durumu", "Son Teslim Tarihi"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // or a condition at your choice with row and column
            }
        };
        Object[] rowData = new Object[6];
        for(MyBookList myBookList : myBooks){
            rowData[0] = myBookList.getISBN();
            rowData[1] = myBookList.getBookName();
            rowData[2] = myBookList.getAuthorName();
            rowData[3] = myBookList.getPageSize();
            rowData[4] = myBookList.isBookState();
            rowData[5] = myBookList.getLastDeliverDate();
            defaultTableModel.addRow(rowData);
        }
        table1.setModel(defaultTableModel);
    }

    public void giveBook(String operate) throws SQLException {
        String query1 = "Select isbn From membersbook where isbn = ?";
        boolean check = true;
        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query1);
            preparedStatement.setString(1, operate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                check = false;
            }
            database.close(database.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        if(check){
            JOptionPane.showMessageDialog(null, "Bu kitapta bu işlemi uygulayamazsınız", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            String query2 = "Delete From membersbook Where isbn = ?";
            try {
                database.setConnection();
                PreparedStatement preparedStatement = database.getConnection().prepareStatement(query2);
                preparedStatement.setString(1, operationBookISBN);
                preparedStatement.execute();
                JOptionPane.showMessageDialog(null, "Çıkış yaptıktan sonra işleminiz gerçekleşmiş olacaktır", "InfoBox: ",  JOptionPane.INFORMATION_MESSAGE);
                database.close(database.getConnection());
            } catch (SQLException | HeadlessException sqlException) {
                sqlException.printStackTrace();
            }

        }
    }
    public boolean checkBookState(String operate){
        String query = "Select bookstate from books where isbn = ? and bookstate = false";
        boolean check;
        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, operate);
            ResultSet resultSet = preparedStatement.executeQuery();
            check = !resultSet.next();
            database.close(database.getConnection());
            return check;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return true;
        }

    }
    public void takeBook(String operate){
        if(checkBookState(operate)) {
            JOptionPane.showMessageDialog(null, "Bu kitapta bu işlemi uygulayamazsınız", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            String query = "Insert into membersbook (memberid, isbn, lastdeliverdate) values (?, ?, ?)";
            out.println(operate);
            try {
                database.setConnection();
                PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
                preparedStatement.setInt(1, memberId);
                preparedStatement.setString(2, operate);
                OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);
                preparedStatement.setObject(3, odt);
                preparedStatement.execute();
                JOptionPane.showMessageDialog(null, "Kitap alınmıştır", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
                database.close(database.getConnection());
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                //JOptionPane.showMessageDialog(null, "Bu kitapta bu işlemi uygulayamazsınız", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    public ArrayList<BookList> ArrayListBook(ArrayList<BookList> arrayList){
        String query = "Select * From Books";
        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while ((resultSet.next())){
                BookList bookList = new BookList();
                bookList.setISBN(resultSet.getString("isbn"));
                bookList.setBookName(resultSet.getString("bookname"));
                bookList.setAuthorName(resultSet.getString("authorname"));
                bookList.setPageSize(resultSet.getString("pagesize"));
                bookList.setBookState(resultSet.getBoolean("bookstate"));
                arrayList.add(bookList);
                database.close(database.getConnection());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  arrayList;
    }
    public ArrayList<MyBookList> ArrayListMyBook(ArrayList<MyBookList> arrayList, int id){
        String query = "select * from membersbook me inner join books b on me.isbn= b.isbn where me.memberid = ?";
        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while ((resultSet.next())){
                MyBookList myBookList = new MyBookList();
                myBookList.setISBN(resultSet.getString("isbn"));
                myBookList.setBookName(resultSet.getString("bookname"));
                myBookList.setAuthorName(resultSet.getString("authorname"));
                myBookList.setPageSize(resultSet.getString("pagesize"));
                myBookList.setBookState(resultSet.getBoolean("bookstate"));
                myBookList.setLastDeliverDate(resultSet.getString("lastdeliverdate"));
                arrayList.add(myBookList);
            }
            database.close(database.getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return arrayList;
    }
    public void setOperationBookISBN(String operationBookISBN) {
        this.operationBookISBN = operationBookISBN;
    }
}
