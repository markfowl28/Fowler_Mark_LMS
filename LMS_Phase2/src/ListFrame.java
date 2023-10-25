/*
    Mark Fowler
    CEN-3024C-17125
    10/24/2023

    The ListFrame Class is responsible for setting up the GUI and performing the see current books option
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    /*
        Print() method is responsible for reading in the database text file to show the current collection
     */
    public static String[] Print() {
        String currentLine;
        ArrayList<String> collection = new ArrayList<>();
        int x = 0;

        try {
            FileReader fr = new FileReader("LibraryDatabase.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                collection.add(currentLine);
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] printCollection = new String[collection.size()];
        for(String s : collection) {
            printCollection[x] = s;
            x++;
        }

        return printCollection;
    }
}
