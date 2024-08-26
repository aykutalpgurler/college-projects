import java.util.ArrayList;

public abstract class Book {

    private final int bookId;
    private boolean isReadingIn;
    private String usedBy;
    private String processDate;


    public Book(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Returns the book object with the specified ID from the given list of books.
     *
     * @param id the ID of the book to be retrieved
     * @param books the list of books to search through
     * @return the book object with the specified ID, or null if the book was not found
     */
    public static Book getBook(int id, ArrayList<Book> books) {
        for (Book book : books) {
            if (book.getBookId() == id) {
                return book;
            }
        }
        return null;
    }


    public int getBookId() {
        return bookId;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public boolean isReadingIn() {
        return isReadingIn;
    }

    public void setReadingIn(boolean readingIn) {
        isReadingIn = readingIn;
    }

    @Override
    public String toString() {
        return null;
    }

    /**
     * Returns a string representation of all the fields of the Book object.
     *
     * @return a string representation of the Book object's fields
     */
    public String returnAllFields() {
        return "Book{" +
                "bookId=" + bookId +
                ", isReadingIn=" + isReadingIn +
                ", usedBy='" + usedBy + '\'' +
                ", processDate='" + processDate + '\'' +
                '}';
    }
}
