public class Academic extends Member{
    public Academic(int memberId) {
        super(memberId, 4, 2);
    }

    @Override
    public String toString() {
        return "Academic [id: " + getMemberId() + "]";
    }
}
