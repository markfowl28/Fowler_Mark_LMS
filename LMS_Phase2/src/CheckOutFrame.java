/*
    Mark Fowler
    CEN-3024C-17125
    10/24/2023

    The CheckOutFrame Class is responsible for setting up the GUI and performing the check-out books option
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = input.getText();
                matches.setListData(Check_OutSearch(title));
            }
        });
        confirmSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = (String) matches.getSelectedValue();
                Check_OutMatch(selection);
            }
        });
    }

    /*
        The Check_OutSearch() method takes the book title typed by the user in the search box as a parameter
        This method returns a list of Strings that match the user's input
        If no match is found, it will display an error message. This occurs if no titles match or if all books are already checked out
     */
    public static String[] Check_OutSearch(String input) {
        String[] data;
        boolean found = false;
        boolean changeStatus = false;
        String currentLine;
        int x = 0;
        ArrayList<String> sameTitle = new ArrayList<>();
        String[] sameTitleCollection;

        try {
            FileReader fr = new FileReader("LibraryDatabase.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                if(data[1].equalsIgnoreCase(input)){
                    found = true;
                    if(data[4].equalsIgnoreCase("Checked In")) {
                        sameTitle.add(currentLine);
                        changeStatus = true;
                    }
                }
            }

            sameTitleCollection = new String[sameTitle.size()];
            if(changeStatus) {
                for(String s : sameTitle) {
                    sameTitleCollection[x] = s;
                    x++;
                }
            } else {
                if(!found){
                    JOptionPane.showMessageDialog(null, "Book Title Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if(found){
                    JOptionPane.showMessageDialog(null, "All Copies Are Already Checked Out!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            fr.close();
            br.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sameTitleCollection;
    }

    /*
        The Check_OutMatch() method takes the selected book from the list that the search results produced as parameter
        It calls to the UpdateBookStatus() method in the Main class to remove the old book status and update it to checked out
        A message showing the due date will pop up when finished
     */
    public static void Check_OutMatch(String book) {
        String title;
        String id;
        String[] data;

        data = book.split(",");
        id = data[0];
        title = data[1];

        Main.UpdateBookStatus(id, true);
        JOptionPane.showMessageDialog(null, title + " Was Successfully Checked Out! The due date is: " + Main.CalculateDate());
    }
}
