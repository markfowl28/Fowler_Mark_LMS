/*
    Mark Fowler
    CEN-3024C-17125
    10/5/2023

    The Book class is designed to create a Book object for every new book added to the collection
    The Book object assigns the included values from the text file, and the additional values of a checked in status and a null due date
 */

import java.util.Date;

public class Book {
    String bookID;
    String bookTitle;
    String bookAuthor;
    String bookGenre;
    String bookStatus;
    Date returnDate;

    public Book(String id, String title, String author, String genre) {
        bookID = id;
        bookTitle = title;
        bookAuthor = author;
        bookGenre = genre;
        bookStatus = "Checked In";
        returnDate = null;
    }
}
