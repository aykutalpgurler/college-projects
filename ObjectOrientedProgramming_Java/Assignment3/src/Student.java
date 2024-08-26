public class Student extends Member{
    public Student(int memberId) {
        super(memberId, 2, 1);
    }

    @Override
    public String toString() {
        return "Student [id: " + getMemberId() + "]";
    }
}
