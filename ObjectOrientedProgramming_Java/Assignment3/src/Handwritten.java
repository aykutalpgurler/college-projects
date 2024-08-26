public class Handwritten extends Book{
    public Handwritten(int bookId) {
        super(bookId);
    }

    @Override
    public String toString() {
        return "Handwritten [id: " + getBookId() + "]";
    }
}
