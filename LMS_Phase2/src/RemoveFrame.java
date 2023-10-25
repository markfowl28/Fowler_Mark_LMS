/*
    Mark Fowler
    CEN-3024C-17125
    10/24/2023

    The RemoveFrame Class is responsible for setting up the GUI and performing the remove books option
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

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
                if (Objects.equals(title, "")) {
                    matches.setListData(RemoveSearch(id, false));
                } else {
                    matches.setListData(RemoveSearch(title, true));
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
        The RemoveSearch() method takes the input typed by the user in the search box and a boolean statement as a parameter
        The boolean statement determines whether it was searched by title or ID
        This method returns a list of Strings that match the user's input
        If no match is found, it will display an error message
     */
    public static String[] RemoveSearch(String input, boolean searchTitle) {
        String currentLine;
        String[] data;
        boolean error = true;
        int x = 0;
        ArrayList<String> matches = new ArrayList<>();
        String [] results;

        try {
            FileReader fr = new FileReader("LibraryDatabase.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                if(searchTitle){
                    if (data[1].equalsIgnoreCase(input)) {
                        matches.add(currentLine);
                        error = false;
                    }
                } else {
                    if (data[0].equalsIgnoreCase(input)) {
                        matches.add(currentLine);
                        error = false;
                    }
                }
            }

            fr.close();
            br.close();

            results = new String[matches.size()];

            if(error) {
                JOptionPane.showMessageDialog(null, "Error: " + input + " could not be located", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                for (String s : matches) {
                    results[x] = s;
                    x++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    /*
        The Remove() method takes the selected book from the list that the search results produced as parameter
        It creates a new text file and reads from the old database file into the new file. Omitting the selected book
        It then deletes the old text file and renames the new file to match the original name
     */
    public static void Remove(String input) {
        String currentLine;
        String[] data;

        File newFile = new File("Tempt.txt");
        File oldFile = new File("LibraryDatabase.txt");

        try {
            FileWriter fw = new FileWriter("Tempt.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader("LibraryDatabase.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                if((currentLine.equalsIgnoreCase(input))) {
                    JOptionPane.showMessageDialog(null, data[1]+ " has been removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    pw.println(currentLine);
                }
            }

            fr.close();
            br.close();
            pw.flush();
            pw.close();
            bw.close();
            fw.close();

            if (oldFile.delete()) {
                System.out.println("File deleted successfully");
            }
            else {
                System.out.println("Failed to delete the file");
            }
            File rename = new File("LibraryDatabase.txt");
            if (newFile.renameTo(rename)) {
                System.out.println("File renamed successfully");
            }
            else {
                System.out.println("Failed to rename the file");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
