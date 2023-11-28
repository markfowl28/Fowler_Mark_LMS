/*
    Mark Fowler
    CEN-3024C-17125
    11/12/2023

    The ListFrame Class is responsible for setting up the GUI and performing print collection option
 */

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ListFrame extends JFrame{
    private JPanel printListPanel;
    private JList list;

    public ListFrame() {
        setContentPane(printListPanel);
        setTitle("Library Collection");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        list.setListData(Print());
    }

    /**
        Print
        Purpose: Reads in every book from the database to be displayed
        Return Type: String[] containing every book from the database
        Arguments: N/A
     */
    public static String[] Print() {
        String user = "root";
        String password = "Toxicity01!!";
        String query = "select * from BookCollection";
        String url = "jdbc:mysql://localhost:3306/Library";
        int x = 0;
        ArrayList<String> collection = new ArrayList<>();
        String[] printCollection = new String[0];

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                StringBuilder bookData = new StringBuilder();
                for (int i = 1; i <= 6; i++) {
                    bookData.append(result.getString(i)).append(" : ");
                }
                collection.add(String.valueOf(bookData));
            }

            printCollection = new String[collection.size()];
            for (String s : collection) {
                printCollection[x] = s;
                x++;
            }
            con.close();
            statement.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return printCollection;
    }
}
