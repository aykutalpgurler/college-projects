import java.util.ArrayList;

public abstract class Member {

    private final int memberId;
    private final int borrowingBookLimit;
    private final int timeLimit;
    private int numberOfBorrowedBooks;

    public Member(int memberId, int borrowingBookLimit, int timeLimit) {
        this.memberId = memberId;
        this.borrowingBookLimit = borrowingBookLimit;
        this.timeLimit = timeLimit;
    }

    /**
     * Returns the member object with the specified ID from the given list of members.
     *
     * @param id the ID of the member to be retrieved
     * @param members the list of members to search through
     * @return the member object with the specified ID, or null if the member was not found
     */
    public static Member getMember(int id, ArrayList<Member> members) {
        for (Member member : members) {
            if (member.getMemberId() == id) {
                return member;
            }
        }
        return null;
    }


    public int getMemberId() {
        return memberId;
    }

    public int getBorrowingBookLimit() {
        return borrowingBookLimit;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getNumberOfBorrowedBooks() {
        return numberOfBorrowedBooks;
    }

    public void setNumberOfBorrowedBooks(int numberOfBorrowedBooks) {
        this.numberOfBorrowedBooks = numberOfBorrowedBooks;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                '}';
    }
}
