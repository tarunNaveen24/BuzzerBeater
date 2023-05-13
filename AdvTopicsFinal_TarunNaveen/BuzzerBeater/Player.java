package BuzzerBeater;

public class Player {

    private String name;
    private String currentPose = "dribbling";
    private int score;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPose(String pose) {
        this.currentPose = pose;
    }

    public String getPose() {
        return this.currentPose;
    }

    public int getScore() {
        return this.score;
    }

    public void addPoint() {
        this.score++;
    }

}
