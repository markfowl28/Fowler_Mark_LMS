/*
    Mark Fowler
    CEN-3024C-17125
    11/12/2023

    This program is designed to manage a collection of books through a local database
    The program allows a user to be able to check in, check out, remove, or see the current list of books in the collection
 */

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args)  {
        MainFrame myFrame = new MainFrame();
    }

    /**
        ClearData
        Purpose: Clears the matches after confirming selection for removing, checking out, and checking in books
        Return Type: String[]
        Arguments: N/A
     */
    public static String[] ClearData() {
        return new String[]{""};
    }

    /**
        Search
        Purpose: Searches the database for any book titles matching the title inputted by the user
        Return Type: String[] containing matches
        Arguments: String of the book title entered
     */
    public static String[] Search(String input) throws SQLException {
        String user = "root";
        String password = "Toxicity01!!";
        String url = "jdbc:mysql://localhost:3306/Library";
        boolean error = true;
        int x = 0;
        ArrayList<String> matches = new ArrayList<>();
        String [] results;

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = con.prepareStatement("select * from BookCollection where Title = ?");
            statement.setString(1, input);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                StringBuilder bookData = new StringBuilder();
                for (int i = 1; i <= 6; i++) {
                    bookData.append(result.getString(i)).append(" : ");
                }
                matches.add(String.valueOf(bookData));
                error = false;
            }
            results = new String[matches.size()];

            if(error) {
                JOptionPane.showMessageDialog(null, "Error: " + input + " could not be located", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                for (String s : matches) {
                    results[x] = s;
                    x++;
                }
            }
            con.close();
            statement.close();
            result.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}

