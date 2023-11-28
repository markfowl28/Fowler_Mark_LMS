/*
    Mark Fowler
    CEN-3024C-17125
    11/12/2023

    The CheckOutFrame Class is responsible for setting up the GUI and performing the check-out book option
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class CheckOutFrame extends JFrame {
    private JPanel checkOutPanel;
    private JTextField input;
    private JButton searchButton;
    private JList matches;
    private JButton confirmSelectionButton;
    private JLabel titleLabel;

    public CheckOutFrame() {
        setContentPane(checkOutPanel);
        setTitle("Check-Out System");
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
        confirmSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = (String) matches.getSelectedValue();
                Check_Out(selection);
                matches.setListData(Main.ClearData());
                input.setText("");
            }
        });
    }

    /**
        Check_Out
        Purpose: Updates the status of the selected book from checked in to checked out. It also sets the book's due date to 4 weeks out from the current date.
                 If the selected book is already checked out, an error message will pop up
        Return Type: Void
        Arguments: String of the barcode # for the selected book
     */
    public static void Check_Out(String book) {
        String user = "root";
        String password = "Toxicity01!!";
        String url = "jdbc:mysql://localhost:3306/Library";
        String[] data;

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = con.prepareStatement("update BookCollection set Book_Status = ?, Due_Date = ? where Barcode = ?");

            data = book.split(":");

            if(data[4].equalsIgnoreCase(" Checked In ")) {
                statement.setString(1, "Checked Out");
                statement.setDate(2, Date.valueOf(LocalDate.now().plusDays(28)));
                statement.setString(3, data[0]);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, data[1] + "has been checked out!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "This copy is already checked out!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            con.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
