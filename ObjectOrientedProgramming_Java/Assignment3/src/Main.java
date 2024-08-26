import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        String[][] input = FileInput.create2DInputArray(Objects.requireNonNull(FileInput.readFile(args[0], true, true)), "\t"); // Read file and produce 2D array from lines and commands.
        FileOutput.writeToFile(args[1],"",false,false); // Initialize empty output.txt
        int numberOfMembers = 0;
        int numberOfBooks = 0;
        ArrayList<Member> members = new ArrayList<>();
        ArrayList<Book> books = new ArrayList<>();
        for(String[] line : input) {
            switch (line[0]) {
                case "addBook":
                    numberOfBooks++;
                    if (line[1].equals("P")) {
                        books.add(new Printed(numberOfBooks));
                        FileOutput.writeToFile(args[1], String.format("Created new book: Printed [id: %d]", numberOfBooks), true, true);
                    } else if (line[1].equals("H")) {
                        books.add(new Handwritten(numberOfBooks));
                        FileOutput.writeToFile(args[1], String.format("Created new book: Handwritten [id: %d]", numberOfBooks), true, true);
                    }
                    break;
                case "addMember":
                    numberOfMembers++;
                    if (line[1].equals("S")) {
                        members.add(new Student(numberOfMembers));
                        FileOutput.writeToFile(args[1], String.format("Created new member: Student [id: %d]", numberOfMembers), true, true);
                    } else if (line[1].equals("A")) {
                        members.add(new Academic(numberOfMembers));
                        FileOutput.writeToFile(args[1], String.format("Created new member: Academic [id: %d]", numberOfMembers), true, true);
                    }
                    break;
                case "borrowBook":
                    if (Book.getBook(Integer.parseInt(line[1]), books) instanceof Handwritten) {
                        FileOutput.writeToFile(args[1], "You cannot borrow this book!", true, true);
                    } else {
                        if (((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).isBorrowed()) {
                            FileOutput.writeToFile(args[1], "You have exceeded the borrowing limit!", true, true);
                        } else {
                            // If member borrowed fewer books than his/her limit, enter the block. Else write an error message.
                            if (Objects.requireNonNull(Member.getMember(Integer.parseInt(line[2]), members)).getNumberOfBorrowedBooks() < Objects.requireNonNull(Member.getMember(Integer.parseInt(line[2]), members)).getBorrowingBookLimit()) {
                                Objects.requireNonNull(Member.getMember(Integer.parseInt(line[2]), members)).setNumberOfBorrowedBooks(Objects.requireNonNull(Member.getMember(Integer.parseInt(line[2]), members)).getNumberOfBorrowedBooks() + 1);
                                ((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).setBorrowed(true);
                                ((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).setDeadline(stringToDate(line[3]).plusWeeks(Objects.requireNonNull(Member.getMember(Integer.parseInt(line[2]), members)).getTimeLimit()));
                                Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setProcessDate(line[3]);
                                Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setUsedBy(line[2]);
                                FileOutput.writeToFile(args[1],
                                        String.format("The book [%s] was borrowed by member [%s] at %s", line[1], line[2], line[3]),
                                        true, true);
                            // If book isn't borrowed but Member tries to exceed limit, enter the block and write an error message.
                            } else {
                                FileOutput.writeToFile(args[1], "You have exceeded the borrowing limit!", true, true);
                            }
                        }
                    }
                    break;
                case "returnBook":
                    long fee;
                    // If book type is Printed, and it is borrowed, enter the block.
                    if (Book.getBook(Integer.parseInt(line[1]), books) instanceof Printed && ((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).isBorrowed()) {
                        Objects.requireNonNull(Member.getMember(Integer.parseInt(line[2]), members)).setNumberOfBorrowedBooks(Objects.requireNonNull(Member.getMember(Integer.parseInt(line[2]), members)).getNumberOfBorrowedBooks() - 1);
                        ((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).setBorrowed(false);
                        fee = ChronoUnit.DAYS.between(((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).getDeadline(), stringToDate(line[3]));
                        FileOutput.writeToFile(args[1], String.format("The book [%s] was returned by member [%s] at %s Fee: %d", line[1], line[2], line[3], (fee > 0 ? fee : 0)),
                                true, true);
                    // If book was reading in library, enter the block.
                    } else if (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).isReadingIn()) {
                        Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setReadingIn(false);
                        FileOutput.writeToFile(args[1], String.format("The book [%s] was returned by member [%s] at %s Fee: 0", line[1], line[2], line[3]),
                                true, true);
                    // Else, write an error message.
                    } else {
                        FileOutput.writeToFile(args[1], "WARNING FOR returnBook", true, true);
                    }
                    break;
                case "extendBook":
                    // If book type is Printed, and it is borrowed, enter the block.
                    if (Book.getBook(Integer.parseInt(line[1]), books) instanceof Printed && ((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).isBorrowed()) {
                        // If book's deadline was already extended, enter the block and write an error message.
                        if (((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).isExtended()) {
                            FileOutput.writeToFile(args[1], "You cannot extend the deadline!", true, true);
                        // Else extend the deadline.
                        } else {
                            ((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).setDeadline(((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).getDeadline().plusWeeks(Objects.requireNonNull(Member.getMember(Integer.parseInt(line[2]), members)).getTimeLimit()));
                            ((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).setExtended(true);
                            FileOutput.writeToFile(args[1], String.format("The deadline of book [%s] was extended by member [%s] at %s", line[1], line[2], line[3]), true, true);
                            FileOutput.writeToFile(args[1], String.format("New deadline of book [%s] is %s", line[1], ((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), true, true);
                        }
                    // If book type is not Printed or not borrowed enter the block and write an error message.
                    } else {
                        FileOutput.writeToFile(args[1], "You cannot extend the deadline!", true, true);
                    }
                    break;
                case "readInLibrary":
                    // If book type is printed and book is in library (not borrowed), enter the block.
                    if (Book.getBook(Integer.parseInt(line[1]), books) instanceof Printed && !((Printed) (Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)))).isBorrowed()) {
                        Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setReadingIn(true);
                        Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setProcessDate(line[3]);
                        Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setUsedBy(line[2]);
                        FileOutput.writeToFile(args[1], String.format("The book [%s] was read in library by member [%s] at %s", line[1], line[2], line[3]), true, true);
                    // Else, if book type is Handwritten enter the block
                    } else if (Book.getBook(Integer.parseInt(line[1]), books) instanceof Handwritten) {
                        // Students cannot access Handwritten books, If a student tries, write an error message.
                        if (Member.getMember(Integer.parseInt(line[2]), members) instanceof Student) {
                            FileOutput.writeToFile(args[1], "Students can not read handwritten books!", true, true);
                        // Academics can access.
                        } else if (Member.getMember(Integer.parseInt(line[2]), members) instanceof Academic) {
                            Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setReadingIn(true);
                            Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setProcessDate(line[3]);
                            Objects.requireNonNull(Book.getBook(Integer.parseInt(line[1]), books)).setUsedBy(line[2]);
                            FileOutput.writeToFile(args[1], String.format("The book [%s] was read in library by member [%s] at %s", line[1], line[2], line[3]), true, true);
                        }
                    // If book type is not printed or already borrowed, enter the block and write an error message.
                    } else {
                        FileOutput.writeToFile(args[1], "You can not read this book!", true, true);
                    }
                    break;
                case "getTheHistory":
                    FileOutput.writeToFile(args[1], "History of library:", true, true);
                    FileOutput.writeToFile(args[1], "", true, true);
                    int studentCount = 0;
                    for (Member member : members) {
                        if (member instanceof Student) {
                            studentCount++;
                        }
                    }
                    FileOutput.writeToFile(args[1], String.format("Number of students: %d", studentCount), true, true);
                    if (studentCount > 0) {
                        for (Member member : members) {
                            if (member instanceof Student) {
                                FileOutput.writeToFile(args[1], member.toString(), true, true);
                            }
                        }
                    }
                    FileOutput.writeToFile(args[1], "", true, true);
                    int academicCount = 0;
                    for (Member member : members) {
                        if (member instanceof Academic) {
                            academicCount++;
                        }
                    }
                    FileOutput.writeToFile(args[1], String.format("Number of academics: %d", academicCount), true, true);
                    if (academicCount > 0) {
                        for (Member member : members) {
                            if (member instanceof Academic) {
                                FileOutput.writeToFile(args[1], member.toString(), true, true);
                            }
                        }
                    }
                    FileOutput.writeToFile(args[1], "", true, true);
                    int printedCount = 0;
                    for (Book book : books) {
                        if (book instanceof Printed) {
                            printedCount++;
                        }
                    }
                    FileOutput.writeToFile(args[1], String.format("Number of printed books: %d", printedCount), true, true);
                    if (printedCount > 0) {
                        for (Book book : books) {
                            if (book instanceof Printed) {
                                FileOutput.writeToFile(args[1], book.toString(), true, true);
                            }
                        }
                    }
                    FileOutput.writeToFile(args[1], "", true, true);
                    int handwrittenCount = 0;
                    for (Book book : books) {
                        if (book instanceof Handwritten) {
                            handwrittenCount++;
                        }
                    }
                    FileOutput.writeToFile(args[1], String.format("Number of handwritten books: %d", handwrittenCount), true, true);
                    if (handwrittenCount > 0) {
                        for (Book book : books) {
                            if (book instanceof Handwritten) {
                                FileOutput.writeToFile(args[1], book.toString(), true, true);
                            }
                        }
                    }
                    FileOutput.writeToFile(args[1], "", true, true);
                    int borrowedBookCount = 0;
                    for (Book book : books) {
                        if (book instanceof Printed && ((Printed) book).isBorrowed()) {
                            borrowedBookCount++;
                        }
                    }
                    FileOutput.writeToFile(args[1], String.format("Number of borrowed books: %d", borrowedBookCount), true, true);
                    if (borrowedBookCount > 0) {
                        for (Book book : books) {
                            if (book instanceof Printed && ((Printed) book).isBorrowed()) {
                                FileOutput.writeToFile(args[1], String.format("The book [%s] was borrowed by member [%s] at %s", book.getBookId(), book.getUsedBy(), book.getProcessDate()), true, true);
                            }
                        }
                    }
                    FileOutput.writeToFile(args[1], "", true, true);
                    int readInBookCount = 0;
                    for (Book book : books) {
                        if (book.isReadingIn()) {
                            readInBookCount++;
                        }
                    }
                    FileOutput.writeToFile(args[1], String.format("Number of books read in library: %d", readInBookCount), true, true);
                    if (readInBookCount > 0) {
                        for (Book book : books) {
                            if (book.isReadingIn()) {
                                FileOutput.writeToFile(args[1], String.format("The book [%s] was read in library by member [%s] at %s", book.getBookId(), book.getUsedBy(), book.getProcessDate()), true, true);
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("BOOM");
                    break;
            }
        }
    }

    /**
     * Parses a string representation of a date into a LocalDate object using the format "yyyy-MM-dd".
     *
     * @param dateString the string representation of the date to be parsed
     * @return a LocalDate object representing the parsed date
     * @throws DateTimeParseException if the given date string cannot be parsed
     */
    public static LocalDate stringToDate(String dateString) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

}
