import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchBookForMembers extends JFrame{
    private JPanel searchPanel;
    private JTextField isbnTextField;
    private JTextField authorNameTextField;
    private JTextField bookNameTextField;
    private JButton backButton;
    private JButton searchButton;
    private JTable table1;
    private JButton searchButton2;
    private JButton searchButton3;
    private String operationIsbn;
    private Database database;
    private ArrayList<SearchBookList> searchBookLists;
    public SearchBookForMembers(int id) {
        try{
            database = new Database();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        searchBookLists = new ArrayList<>();
        try {
            database.close(database.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        add(searchPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setTitle("Search Menu");
        backButton.addActionListener(e -> {
            MemberMenu memberMenu = new MemberMenu(id);
            memberMenu.setOperationBookISBN(operationIsbn);
            memberMenu.setLocation(getX(), getY());
            memberMenu.setVisible(true);
            setVisible(false);
        });
        searchButton.addActionListener(e -> {
            searchBookLists.clear();
            searchBookLists = listByIsbn(searchBookLists, isbnTextField.getText());
            createTable();
        });
        searchButton2.addActionListener(e -> {
            searchBookLists.clear();
            searchBookLists = listByBookName(searchBookLists, bookNameTextField.getText());
            createTable();
        });
        searchButton3.addActionListener(e -> {
            searchBookLists.clear();
            searchBookLists = listByAuthorName(searchBookLists, authorNameTextField.getText());
            createTable();
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int check = table1.getSelectedRow();
                operationIsbn = (String) table1.getModel().getValueAt(check, 0);
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
        for(SearchBookList searchBookList : searchBookLists){
            rowData[0] = searchBookList.getISBN();
            rowData[1] = searchBookList.getBookName();
            rowData[2] = searchBookList.getAuthorName();
            rowData[3] = searchBookList.getPageSize();
            rowData[4] = searchBookList.isBookState();
            defaultTableModel.addRow(rowData);
        }
        table1.setModel(defaultTableModel);
    }
    public ArrayList<SearchBookList> listByBookName(ArrayList<SearchBookList> arrayList, String key){
        String query = "Select * From Books Where bookname LIKE '" + key + "%'";
        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while ((resultSet.next())){
                SearchBookList searchBookList = new SearchBookList();
                searchBookList.setISBN(resultSet.getString("isbn"));
                searchBookList.setBookName(resultSet.getString("bookname"));
                searchBookList.setAuthorName(resultSet.getString("authorname"));
                searchBookList.setPageSize(resultSet.getString("pagesize"));
                searchBookList.setBookState(resultSet.getBoolean("bookstate"));
                arrayList.add(searchBookList);
            }
            database.close(database.getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  arrayList;
    }
    public ArrayList<SearchBookList> listByIsbn(ArrayList<SearchBookList> arrayList, String key){
        String query = "Select * From Books Where isbn LIKE '" + key + "%'";
        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while ((resultSet.next())){
                SearchBookList searchBookList = new SearchBookList();
                searchBookList.setISBN(resultSet.getString("isbn"));
                searchBookList.setBookName(resultSet.getString("bookname"));
                searchBookList.setAuthorName(resultSet.getString("authorname"));
                searchBookList.setPageSize(resultSet.getString("pagesize"));
                searchBookList.setBookState(resultSet.getBoolean("bookstate"));
                arrayList.add(searchBookList);
            }
            database.close(database.getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  arrayList;
    }
    public ArrayList<SearchBookList> listByAuthorName(ArrayList<SearchBookList> arrayList, String key){
        String query = "Select * From Books Where authorname LIKE '" + key + "%'";
        try{
            database.setConnection();
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while ((resultSet.next())){
                SearchBookList searchBookList = new SearchBookList();
                searchBookList.setISBN(resultSet.getString("isbn"));
                searchBookList.setBookName(resultSet.getString("bookname"));
                searchBookList.setAuthorName(resultSet.getString("authorname"));
                searchBookList.setPageSize(resultSet.getString("pagesize"));
                searchBookList.setBookState(resultSet.getBoolean("bookstate"));
                arrayList.add(searchBookList);
            }
            database.close(database.getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  arrayList;
    }
}
