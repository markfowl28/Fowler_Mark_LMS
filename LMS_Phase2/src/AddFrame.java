/*
    Mark Fowler
    CEN-3024C-17125
    10/24/2023

    The AddFrame Class is responsible for setting up the GUI for the add books option
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class AddFrame extends JFrame {
    private JTextField input;
    private JButton searchButton;
    private JList addedList;
    private JPanel addPanel;
    private JLabel fileLabel;
    private JLabel updateLabel;

    public AddFrame() {
        setContentPane(addPanel);
        setTitle("Add New Books");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String file = input.getText();
                addedList.setListData(Add(file));
                updateLabel.setText("The following books have been added:");
            }
        });
    }

    /*
        The Add() method takes the file name entered by the user as a parameter
        It uses the Book class to assign values to any new books
        It returns a list of all the new books added for the user to see
        If the file name entered doesn't match any file, an error message will occur
     */
    public static String[] Add(String input) {
        String[] data;
        String textFile;
        String currentLine;
        String id;
        String title;
        String author;
        String genre;
        boolean valid;
        ArrayList<String> newBooks = new ArrayList<>();
        String book;
        String[] listNewBooks;
        int x = 0;

        textFile = (input + ".txt");

        try {
            FileReader fr = new FileReader(textFile);
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter("LibraryDatabase.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                id = data[0];
                title = data[1];
                author = data[2];
                genre = data[3];

                Book addBook = new Book(id, title, author, genre);

                valid = VerifyAdd(id);

                if(valid) {
                    book = (id + "," + title + "," + author + "," + genre + "," + addBook.bookStatus + ",Due Date:" + addBook.returnDate);
                    newBooks.add(book);
                    pw.println(book);
                }
            }

            fr.close();
            br.close();
            pw.flush();
            pw.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        listNewBooks = new String[newBooks.size()];
        for (String s : newBooks) {
            listNewBooks[x] = s;
            x++;
        }
        return listNewBooks;
    }

    /*
        The VerifyAdd() method is used by the Add() method to verify that no duplicate books with the same barcode numbers are added that already exist in the collection
        It takes the barcode number as a parameter and returns true or false depending on if a book with that barcode number doesn't exist already
     */
    public static boolean VerifyAdd(String id) {
        boolean valid = true;
        String[] data;
        String currentLine;

        try {
            FileReader fr = new FileReader("LibraryDatabase.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                if((data[0].equalsIgnoreCase(id)))
                {
                    valid = false;
                }
            }

            fr.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return valid;
    }
}
