/*
    Mark Fowler
    CEN-3024C-17125
    10/5/2023

    This program is designed to manage a collection of books through a text file
    The user will be able to add, remove, or see the current list of books in the collection through a main menu
    The user will also be able to check in or check out a book from the collection
 */

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        StartProgram();
        System.out.println("Thank you for using the Library Management System! Goodbye!");
    }

    /*
    The StartProgram() method is responsible for housing the main menu. It will continuously loop until the user chooses the exit option
     */
    public static void StartProgram() {
        int selection;
        do {
            System.out.println("Welcome to the Library Management System!");
            System.out.println();
            System.out.println("Please select from the following options:");
            System.out.println("1. Check-in a book");
            System.out.println("2. Check-out a book");
            System.out.println("3. Add books to the library's collection");
            System.out.println("4. Remove books from the library's collection");
            System.out.println("5. Print current list of books in the library's collection");
            System.out.println("6. Exit the program");
            System.out.println();
            System.out.print("Enter the number for your selection: ");
            Scanner s = new Scanner(System.in);
            selection  = s.nextInt();

            switch (selection) {
                case 1 -> Check_In();
                case 2 -> Check_Out();
                case 3 -> Add();
                case 4 -> Remove();
                case 5 -> Print();
                case 6 -> System.out.println();
                default -> System.out.println("Invalid selection. Please try again");
            }
        } while (selection != 6);
    }

    /*
    The Check_In() method is responsible for updating the book status and due date for a book that is showing checked out
    If there are multiple copies of the book showing checked out, the user will need to confirm the barcode number of the correct option
    If the book title doesn't match any books, an error message will occur
     */
    public static void Check_In() {
        String[] data;
        boolean found = false;
        boolean changeStatus = false;
        String title;
        String id = null;
        String updatedStatus;
        String currentLine;
        int count = 0;
        ArrayList<String> sameTitle = new ArrayList<>();

        System.out.println();
        System.out.print("Please enter the title of the book to check-in: ");
        Scanner s = new Scanner(System.in);
        title = s.nextLine().toUpperCase();

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
                id = s.nextLine();
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
        System.out.println();
        Print();
        System.out.println();
    }

    /*
    The Check_Out() method is responsible for updating the book status and due date for a book that is showing checked in
    If there are multiple copies of the book showing checked in, the user will need to confirm the barcode number of the correct option
    If the book title doesn't match any in the collection, an error message will occur
     */
    public static void Check_Out() {
        String[] data;
        boolean found = false;
        boolean changeStatus = false;
        String title;
        String id = null;
        String currentLine;
        String updatedStatus;
        int count = 0;
        ArrayList<String> sameTitle = new ArrayList<>();

        System.out.println();
        System.out.print("Please enter the title of the book to check-out: ");
        Scanner s = new Scanner(System.in);
        title = s.nextLine().toUpperCase();

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
                id = s.nextLine();
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
        System.out.println();
        Print();
        System.out.println();
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
    It uses an if statement to determine what the update should be based on if it's checking in or checking out a book
    It takes a barcode as a parameter and returns the new status as a string
     */
    public static String UpdateBookStatus(String id, boolean checkOut) {
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

    /*
    The Add() method is responsible for reading in a text file through user input, and adding any new books to the collection
     */
    public static void Add() {
        String[] data;
        String textFile;
        String currentLine;
        String id;
        String title;
        String author;
        String genre;
        boolean valid;

        System.out.println();
        System.out.print("Please enter the name of the text file: ");
        Scanner s = new Scanner(System.in);
        textFile = (s.nextLine() + ".txt");

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
        System.out.println();
        Print();
        System.out.println();
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

    /*
    Remove() method is responsible for opening up the text file and removing a book from the text file by its barcode or title
    It stores the updated collection in a new file and deletes the old file containing the removed item
    If the title matches more than one book in the collection, the system will print out all books with that title. A barcode will then be asked to verify which book to remove.
    If the title or barcode number can't be found, an error message will occur
     */
    public static void Remove() {
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

        System.out.println();
        System.out.print("Please enter the Barcode Number or Title of the book you wish to remove: ");
        Scanner s = new Scanner(System.in);
        remove = s.nextLine().toUpperCase();

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
                ID = s.nextLine();

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
                Print();
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Print() method is responsible for reading in the text file to show the current collection
     */
    public static void Print() {
        String currentLine;

        try {
            FileReader fr = new FileReader("Collection.txt");
            BufferedReader br = new BufferedReader(fr);

            System.out.println();
            System.out.println("Printing in progress....");
            System.out.println();

            //prints out each line of the collection
            while((currentLine = br.readLine()) != null)
            {
                System.out.println(currentLine);
            }

            fr.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //allows user to view collection until they choose to exit
        int exit;
        do{
            System.out.println();
            System.out.println("Enter 1 to return to the main menu: ");
            Scanner s = new Scanner(System.in);
            exit  = s.nextInt();
            System.out.println();
        } while (exit != 1);
    }
}
