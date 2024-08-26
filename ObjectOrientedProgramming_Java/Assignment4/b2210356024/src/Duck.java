public class Duck {
    private double x;
    private double y;
    private double speedX;
    private double speedY;
    private boolean shot;

    public Duck(double x, double y, double speedX, double speedY) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.shot = false;
    }

    public void moveTheDuck() {
        if (!shot) {
            x += speedX;
            y += speedY;
            if (x <= 0 || x >= 256 * DuckHunt.SCALE - 27 * DuckHunt.SCALE) {
                speedX *= -1;
            }
            if (y <= 0 || y >= 240 * DuckHunt.SCALE - 31 * DuckHunt.SCALE) {
                speedY *= -1;
            }
        }
    }

    public boolean isShot(double mouseX, double mouseY) {
        this.shot = mouseX >= x && mouseX <= x + 27 * DuckHunt.SCALE && mouseY >= y && mouseY <= y + 31 * DuckHunt.SCALE;
        return shot;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }
}