import java.time.LocalDate;

public class Printed extends Book{

    private boolean isBorrowed;
    private boolean isExtended;
    private LocalDate deadline;

    public Printed(int bookId) {
        super(bookId);
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public boolean isExtended() {
        return isExtended;
    }

    public void setExtended(boolean extended) {
        this.isExtended = extended;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }


    @Override
    public String returnAllFields() {
        return super.returnAllFields() + "Printed{" +
                "isBorrowed=" + isBorrowed +
                ", isExtended=" + isExtended +
                ", deadline=" + deadline +
                "} " + super.toString();
    }

    @Override
    public String toString() {
        return "Printed [id: " + getBookId() + "]";
    }
}
