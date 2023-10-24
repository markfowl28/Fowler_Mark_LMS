/*
    Mark Fowler
    CEN-3024C-17125
    10/13/2023

    These are unit tests using JUnit to test the following:
    Books can be added to the database
    A book can be removed from the database by barcode or title
    A book can be checked out.  If so, the due date is no longer "null".
    A book can be checked in. If so, the due date is "null".
 */
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    //Tests the add method by opening up a file called CollectionOC.txt and adds it to the Collection.txt file
    @Test
    void addTest() {
        String[] data;
        String textFile;
        String currentLine;
        String id;
        String title;
        String author;
        String genre;
        boolean valid;

        textFile = "CollectionOC.txt";

        try {
            FileReader fr = new FileReader(textFile);
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter("Collection.txt", true);
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
                    pw.println(id + "," + title + "," + author + "," + genre + "," + addBook.bookStatus + ",Due Date:" + addBook.returnDate);
                    System.out.println();
                    System.out.println(currentLine + " has been added");
                }
            }

            fr.close();
            br.close();
            pw.flush();
            pw.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Tests the Check_In method by requesting to check back in the book The Da Vinci Code to the collection
    @Test
    void check_InTest() {
        String[] data;
        boolean found = false;
        boolean changeStatus = false;
        String title;
        String id = null;
        String updatedStatus;
        String currentLine;
        int count = 0;
        ArrayList<String> sameTitle = new ArrayList<>();

        title = "The Da Vinci Code";

        try {
            FileReader fr = new FileReader("Collection.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                if(data[1].equalsIgnoreCase(title)){
                    found = true;
                    if(data[4].equalsIgnoreCase("Checked Out")) {
                        sameTitle.add(currentLine);
                        id = data[0];
                        count++;
                        changeStatus = true;
                    }
                }
            }

            if(count > 1) {
                System.out.println();
                System.out.println("The following books match the title entered....");
                System.out.println();

                for(String x : sameTitle) {
                    System.out.println(x);
                }

                System.out.println();
                System.out.println("Please enter the barcode number matching the book you wish to check-out: ");
                id = "1001";
            }

            if(!found){
                System.out.println("Error: " + title + " could not be found");
            }
            if(found && !changeStatus){
                System.out.println("Error: All copies of " + title + " are already checked in");
            }

            fr.close();
            br.close();

            if(changeStatus){
                updatedStatus = UpdateBookStatus(id, false);

                FileWriter fw = new FileWriter("Collection.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                pw.println(updatedStatus);
                System.out.println();
                System.out.println(title + " has been returned");

                pw.flush();
                pw.close();
                bw.close();
                fw.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Tests the Check_Out method by requesting to check out the book The Hobbit from the collection
    @Test
    void check_OutTest() {
        String[] data;
        boolean found = false;
        boolean changeStatus = false;
        String title;
        String id = null;
        String currentLine;
        String updatedStatus;
        int count = 0;
        ArrayList<String> sameTitle = new ArrayList<>();

        title = "The Hobbit";

        try {
            FileReader fr = new FileReader("Collection.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                if(data[1].equalsIgnoreCase(title)){
                    found = true;
                    if(data[4].equalsIgnoreCase("Checked In")){
                        sameTitle.add(currentLine);
                        id = data[0];
                        count++;
                        changeStatus = true;
                    }
                }
            }

            if(count > 1) {
                System.out.println();
                System.out.println("The following books match the title entered....");
                System.out.println();

                for(String x : sameTitle) {
                    System.out.println(x);
                }

                System.out.println();
                System.out.println("Please enter the barcode number matching the book you wish to check-out: ");
                id = "1003";
            }

            if(!found){
                System.out.println();
                System.out.println("Error: " + title + " could not be found");
            }
            if(found && !changeStatus) {
                System.out.println();
                System.out.println("Error: All copies of " + title + " are already checked out");
            }

            fr.close();
            br.close();

            if(changeStatus){
                updatedStatus = UpdateBookStatus(id, true);

                FileWriter fw = new FileWriter("Collection.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                pw.println(updatedStatus);
                System.out.println();
                System.out.println(title + " is due by: " + CalculateDate());

                pw.flush();
                pw.close();
                bw.close();
                fw.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Tests the Remove method by requesting to remove the book The Girl on the Train from the collection
    @Test
    void removeTest() {
        String currentLine;
        String[] data;
        String remove;
        boolean error = true;
        String printRemoved = null;
        int count = 0;
        String ID;
        ArrayList<String> sameTitle = new ArrayList<>();
        ArrayList<String> compareID = new ArrayList<>();

        File newFile = new File("Tempt.txt");
        File oldFile = new File("Collection.txt");

        remove = "The Girl on the Train";

        try {
            FileWriter fw = new FileWriter("Tempt.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader("Collection.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                if((data[0].equalsIgnoreCase(remove))) {
                    printRemoved = currentLine;
                    error = false;
                }
                else if(data[1].equalsIgnoreCase(remove))
                {
                    sameTitle.add(currentLine);
                    compareID.add(data[0]);
                    count++;
                } else {
                    pw.println(currentLine);
                }
            }

            if(count > 1) {
                System.out.println();
                System.out.println("The following books match the title entered....");
                System.out.println();

                for(String x : sameTitle) {
                    System.out.println(x);
                }

                System.out.println();
                System.out.println("Please enter the barcode number matching the book you wish to remove: ");
                ID = "1007";

                count = 0;

                for(String y : sameTitle) {
                    if (!Objects.equals(compareID.get(count), ID)) {
                        pw.println(y);
                        error = false;
                    } else {
                        printRemoved = y;
                    }
                    count++;
                }
            }

            fr.close();
            br.close();
            pw.flush();
            pw.close();
            bw.close();
            fw.close();

            oldFile.delete();
            File rename = new File("Collection.txt");
            newFile.renameTo(rename);

            if(error) {
                System.out.println();
                System.out.println("Error: Title or Barcode Number " + remove + " could not be located");
                System.out.println();
            } else {
                System.out.println();
                System.out.println(printRemoved + " has been removed");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    *******************************************************************
    The following methods are called within the various testing methods
    *******************************************************************
     */
    private String UpdateBookStatus(String id, boolean checkOut) {
        String currentLine;
        String[] data;
        String remove;
        String update = null;

        File newFile = new File("Tempt.txt");
        File oldFile = new File("Collection.txt");

        remove = id;

        try {
            FileWriter fw = new FileWriter("Tempt.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader("Collection.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");

                if(checkOut) {
                    if (!(data[0].equalsIgnoreCase(remove)) || ((data[0].equalsIgnoreCase(remove) && (data[4]).equalsIgnoreCase("Checked Out")))) {
                        pw.println(currentLine);
                    } else {
                        update = (id + "," + data[1] + "," + data[2] + "," + data[3] + "," + "Checked Out" + ",Due Date:" + CalculateDate());
                    }
                } else {
                    if (!(data[0].equalsIgnoreCase(remove)) || ((data[0].equalsIgnoreCase(remove) && (data[4]).equalsIgnoreCase("Checked In")))) {
                        pw.println(currentLine);
                    } else {
                        update = (id + "," + data[1] + "," + data[2] + "," + data[3] + "," + "Checked In" + ",Due Date:" + null);
                    }
                }
            }

            fr.close();
            br.close();
            pw.flush();
            pw.close();
            bw.close();
            fw.close();

            oldFile.delete();
            File rename = new File("Collection.txt");
            newFile.renameTo(rename);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return update;
    }

    private static Date CalculateDate() {
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 28);

        return c.getTime();
    }

    private static boolean VerifyAdd(String id) {
        boolean valid = true;
        String[] data;
        String currentLine;

        try {
            FileReader fr = new FileReader("Collection.txt");
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