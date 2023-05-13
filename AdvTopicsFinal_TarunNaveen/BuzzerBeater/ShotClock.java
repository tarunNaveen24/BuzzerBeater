package BuzzerBeater;

public class ShotClock extends StopWatch {
    public boolean isShooting;
    public int currentTime;

    public int getTimeLeft() {
        if (isShooting) {
            return (int) (15 - this.getCurrentTime());
        } else {
            return (int) (15 - this.getTimeSeconds());
        }
    }
}