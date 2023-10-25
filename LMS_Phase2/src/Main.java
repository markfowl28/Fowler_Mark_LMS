/*
    Mark Fowler
    CEN-3024C-17125
    10/24/2023

    This program is designed to manage a collection of books through a text file
    The user will be able to add, remove, or see the current list of books in the collection through a main menu
    The user will also be able to check in or check out a book from the collection
 */

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        MainFrame myFrame = new MainFrame();
    }

    /*
    The ClearData() method clears the matches after confirming selection for removing, checking out, and checking in books
     */
    public static String[] ClearData() {
        return new String[]{""};
    }

    /*
    The CalculateDate() method is responsible for assigning the due date value to a book being checked out
    When a book is checked out, the due date is set 4 weeks(28 days) from the current date and time
     */
    public static Date CalculateDate() {
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 28);

        return c.getTime();
    }

    /*
    The UpdateBookStatus() method is responsible for removing the old book status when checking in or checking out a book
    It takes the book's barcode and a boolean statement as a parameter
    The boolean statement determines whether the book is being checked out or checked in
     */
    public static void UpdateBookStatus(String id, boolean checkOut) {
        String currentLine;
        String[] data;
        String remove;

        File newFile = new File("Tempt.txt");
        File oldFile = new File("LibraryDatabase.txt");

        remove = id;

        try {
            FileWriter fw = new FileWriter("Tempt.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader("LibraryDatabase.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");

                if(checkOut) {
                    if (!(data[0].equalsIgnoreCase(remove))) {
                        pw.println(currentLine);
                    } else {
                        pw.println(id + "," + data[1] + "," + data[2] + "," + data[3] + "," + "Checked Out" + ",Due Date:" + CalculateDate());
                    }
                } else {
                    if (!(data[0].equalsIgnoreCase(remove))) {
                        pw.println(currentLine);
                    } else {
                        pw.println(id + "," + data[1] + "," + data[2] + "," + data[3] + "," + "Checked In" + ",Due Date:" + null);
                    }
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
