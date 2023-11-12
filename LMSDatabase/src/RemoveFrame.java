/*
    Mark Fowler
    CEN-3024C-17125
    11/12/2023

    The RemoveFrame Class is responsible for setting up the GUI and performing the remove book option
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.sql.*;

public class RemoveFrame extends JFrame{
    private JLabel titleLabel;
    private JTextField titleInput;
    private JLabel IDLabel;
    private JTextField IDInput;
    private JList matches;
    private JButton confirmSelectionButton;
    private JButton searchDButton;
    private JButton searchTitleButton;
    private JPanel removePanel;

    public RemoveFrame() {
        setContentPane(removePanel);
        setTitle("Remove Books From the Database");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        searchTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleInput.getText();
                String id = IDInput.getText();

                if (!Objects.equals(id, "")) {
                    try {
                        matches.setListData(SearchID(id));
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    try {
                        matches.setListData(Main.Search(title));
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                matches.getPreferredScrollableViewportSize();
            }
        });
        confirmSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = (String) matches.getSelectedValue();
                Remove(selection);

                matches.setListData(Main.ClearData());
                titleInput.setText("");
                IDInput.setText("");
            }
        });
    }

    /*
        SearchID
        Purpose: Allows the user to search for the barcode # of a book in the database
        Return Type: String[] containing any matches
        Arguments: String of the barcode # entered by the user
     */
    public static String[] SearchID(String input) throws SQLException {
        String user = "root";
        String password = "Toxicity01!!";
        String url = "jdbc:mysql://localhost:3306/Library";
        boolean error = true;
        int x = 0;
        ArrayList<String> matches = new ArrayList<>();
        String [] results;

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = con.prepareStatement("select * from BookCollection where Barcode = ?");
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

    /*
        Remove
        Purpose: Removes the selected book from the database
        Return Type: Void
        Arguments: String of the barcode # for the user selected book
     */
    public static void Remove(String input) {
        String user = "root";
        String password = "Toxicity01!!";
        String url = "jdbc:mysql://localhost:3306/Library";
        String[] data;

        try {
            data = input.split(":");

            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = con.prepareStatement("delete from BookCollection where Barcode = ?");
            statement.setString(1, data[0]);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Book has been removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            con.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
