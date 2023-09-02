/*
    Mark Fowler
    CEN-3024C-17125
    9/1/2023

    This program is designed to manage a collection of books through a text file.
    The user will be able to add, remove, or see the current list of books in the collection through a main menu.
 */

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        //Program Main Menu
        int selection;
        do {
            System.out.println("Welcome to the Library Management System!");
            System.out.println();
            System.out.println("Please select from the following options:");
            System.out.println("1. Add a book to the library's collection");
            System.out.println("2. Remove a book from the library's collection");
            System.out.println("3. See the current list of books in the collection");
            System.out.println("4. Exit the program");
            System.out.println();
            System.out.print("Enter the number for your selection: ");
            Scanner s = new Scanner(System.in);
            selection  = s.nextInt();

            switch (selection) {
                case 1 -> Add();
                case 2 -> Remove();
                case 3 -> List();
                default -> System.out.println("Invalid selection. Please try again");
            }
        } while (selection != 4);
        System.out.println();
        System.out.println("Thank you for using the Library Management System! Goodbye!");
    }

    /*
    Add() method is responsible for opening up the text file and adding a user entered book.
    The user will give the book title and author and the program will generate a unique ID number using
    a series of loops.
     */
    public static void Add() {
        int position = 0;
        int count = 0;
        String currentLine;
        String[] data;
        String title;
        String author;
        String ID;
        boolean valid;

        System.out.println();
        System.out.print("Please enter the title of the book you wish to add: ");
        Scanner s = new Scanner(System.in);
        title = s.nextLine();
        System.out.println();
        System.out.print("Please enter the author of the book you wish to add: ");
        author = s.nextLine();

        //creates unique ID by counting the total books and adding 1 until no existing match is found
        try {
            FileWriter fw = new FileWriter("Collection.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader("Collection.txt");
            BufferedReader br = new BufferedReader(fr);

            while(br.readLine() != null)
            {
                count++;
            }

            do {
                ID = Integer.toString(count);
                valid = true;

                while((currentLine = br.readLine()) != null)
                {
                    data  = currentLine.split(",");
                    if((data[position].equalsIgnoreCase(ID)))
                    {
                        valid = false;
                        count++;
                    }
                }
            } while (!valid);

            pw.println(ID + "," + title + "," + author);
            System.out.println();
            System.out.println(ID + "," + title + "," + author + " has been added");

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
    }

    /*
    Remove() method is responsible for opening up the text file and removing a book from the text file by
    iterating through each line until the matching ID is found that the user inputs.
    It stores the updated collection in a new file and deletes the old file containing the removed item.
     */
    public static void Remove() {
        int position = 0;
        String currentLine;
        String[] data;
        String remove;
        String printRemoved = null;

        File newFile = new File("Tempt.txt");
        File oldFile = new File("Collection.txt");

        System.out.println();
        System.out.print("Please enter the ID number of the book you wish to remove: ");
        Scanner s = new Scanner(System.in);
        remove = s.nextLine();

        //Loops through and copies every line to a new file that doesn't contain the ID entered by user
        try {
            FileWriter fw = new FileWriter("Tempt.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader("Collection.txt");
            BufferedReader br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null)
            {
                data  = currentLine.split(",");
                if(!(data[position].equalsIgnoreCase(remove)))
                {
                    pw.println(currentLine);
                } else {
                    printRemoved = currentLine;
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

            System.out.println();
            System.out.println(printRemoved + " has been removed");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    /*
    List() method is responsible for reading in the text file to show the current collection.
     */
    public static void List() {
        String currentLine;

        try {
            FileReader fr = new FileReader("Collection.txt");
            BufferedReader br = new BufferedReader(fr);

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