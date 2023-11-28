/*
    Mark Fowler
    CEN-3024C-17125
    11/12/2023

    The CheckInFrame Class is responsible for setting up the GUI and performing the check-in book option
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CheckInFrame extends JFrame {
    private JPanel checkInPanel;
    private JTextField input;
    private JLabel titleLabel;
    private JList matches;
    private JButton searchButton;
    private JButton selectButton;

    public CheckInFrame() {
        setContentPane(checkInPanel);
        setTitle("Check-In System");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = input.getText();
                try {
                    matches.setListData(Main.Search(title));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = (String) matches.getSelectedValue();
                Check_In(selection);

                matches.setListData(Main.ClearData());
                input.setText("");
            }
        });
    }

    /**
        Check_In
        Purpose: Updates the status of the selected book from checked out to checked in. It also sets the book's due date to NULL.
                 If the selected book is already checked in, an error message will pop up
        Return Type: Void
        Arguments: String of the barcode # for the selected book
     */
    public static void Check_In(String book) {
        String user = "root";
        String password = "Toxicity01!!";
        String url = "jdbc:mysql://localhost:3306/Library";
        String[] data;

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = con.prepareStatement("update BookCollection set Book_Status = ?, Due_Date = NULL where Barcode = ?");

            data = book.split(":");
            if(data[4].equalsIgnoreCase(" Checked Out ")) {
                statement.setString(1, "Checked In");
                statement.setString(2, data[0]);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, data[1] + "has been checked in!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "This copy is already checked in!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            con.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}