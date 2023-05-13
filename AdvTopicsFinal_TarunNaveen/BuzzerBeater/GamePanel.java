package BuzzerBeater;

import java.io.File;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    public static Player player1;
    public static Player player2;

    public static int p1Refresh = 0;
    public static int p2Refresh = 0;

    private Graphics g;

    private BufferedImage ballImage;

    static StopWatch shotClock;
    private static boolean shotClockStarted = false;
    public static boolean shot = false;
    public static double currentShotProgress = 0;

    private boolean jumpExecuted = false;

    private static boolean looseBall = false;

    public static boolean freezePlayers = false;
    public static boolean freezep2 = false;

    public double currentProbability = 0;
     
    private Color primaryColor = Color.BLACK;
	private Color secondaryColor = Color.RED;
    private Color white = Color.WHITE;
    public static double[] p1Loc = {1150, 440};
    private static double[] p2Loc = {950, 440};
    private static final double[][] startingLocs = {{1150, 440}, {950, 440}, {275, 475}};
    private static double[] hoopLocation = {227.75, 414.5};
    public static double[] ballLocation = {1150, 440};
    private boolean posession = true;
    private boolean player1Point = true;
    public static double[] aiDesiredPosition = {265 + 0.975*(p1Loc[0]-265), 475 + 0.975*(p1Loc[1]-475)};
    public static Shot currentShot;
    public static boolean shotCompleted = false;

    private Font font = new Font("DialogInput", Font.BOLD, 140);

    public GamePanel() {
        BufferedImage image;
        try {
            image = ImageIO.read(new File("sprites/ball.png"));
            ballImage = MenuPanel.resize(image, 50, 50); // right size
        } catch (IOException e) {}
    }

    public static void setPlayers(String player11, String player22) {
        player1 = new Player(player11);
        player2 = new Player(player22);
    }

    public void resetPoint() {

        // try {
        //     Thread.sleep(500);
        // } catch (InterruptedException e) {}

        p1Loc[0] = startingLocs[0][0];
        p1Loc[1] = startingLocs[0][1];
        p2Loc[0] = startingLocs[1][0];
        p2Loc[1] = startingLocs[1][1];
        ballLocation[0] = startingLocs[0][0];
        ballLocation[1] = startingLocs[0][1];

        if (player1Point) {
            player1.addPoint();
        } else {
            player2.addPoint();
        }

        this.repaint();
        
        shot = false;
        freezePlayers = false;
        shotClockStarted = false;
        jumpExecuted = false;
        currentShot = null;
        shotCompleted = false;

    }

    public static boolean isClose(double[] loc1, double[] loc2, int difference) {
        return (Math.abs(loc1[0]-loc2[0]) < difference) && (Math.abs(loc1[1] - loc2[1]) < difference);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;
        this.setBackground(primaryColor);
        g.setColor(white);
    
        //draw title
		setFontSize(130);
		//g.drawString("BUZZER BEATER", 400, 120);

        
        //draw court outline
        //set a thicker stroke for grid lines
        g.setColor(white);
		Graphics2D g2 = (Graphics2D) g;
		Float size = ((Double) 10.0).floatValue();
		g2.setStroke(new BasicStroke(size));
        double p2Speed = 5 * (Attributes.speed.get(player2.getName()) / 100.0);

        System.out.println("p1: " + p1Refresh + "     " + "p2: " + p2Refresh);
        // int[] xPoints = {150, 150, 1400, 1400};
        // int[] yPoints = {900, 200, 200, 900};
        // g.setColor(tertiaryColor);
        // g.fillPolygon(xPoints, yPoints, 4);
        // g.setColor(white);
        // g.drawPolygon(xPoints, yPoints, 4);
        // //half court line
        // g.drawLine(1200, 200, 1200, 900);
        // g.drawOval(1075, 425, 250, 250);
        // //three point arc
        // g.drawArc(-50, 200, 900, 700, -90, 180);
        // //the paint
        // int[] xPoints1 = {150, 150, 625, 625};
        // int[] yPoints1 = {675, 425, 425, 675};
        // g.setColor(primaryColor);
        // g.fillPolygon(xPoints1, yPoints1, 4);
        // g.setColor(white);
        // g.drawPolygon(xPoints1, yPoints1, 4);
        // g.drawOval(500, 423, 255, 255);
        
        // g.setColor(primaryColor);
        int[] pointLoc = {120, 50};
        drawCourt(g, "sprites/court (2).png", pointLoc);
        drawScoreboard();
        // int[] playerSize = {200, 200};
        // pointLoc[0] = 500;
        // pointLoc[1] = 500;
        // drawPlayer(g, "sprites/L. Doncic_dribbling.png", playerSize, pointLoc);\
        g.setColor(Color.RED);
        g.fillOval((int) p1Loc[0], (int) p1Loc[1], 75, 75);
        g.setColor(Color.BLUE);
        g.fillOval((int) p2Loc[0], (int) p2Loc[1], 75, 75);
        // int[] playerSize = {125, 175};
        // int[] p1LocInt = {(int) p1Loc[0], (int) p1Loc[1]};
        // int[] p2LocInt = {(int) p2Loc[0], (int) p2Loc[1]};

        // drawPlayer(g, "sprites/" + player1.getName() + ".png/", playerSize, p1LocInt);
        // drawPlayer(g, "sprites/" + player2.getName() + ".png/", playerSize, p2LocInt);

        //draw ball
        if (posession) {
            g.drawImage(ballImage, (int) p1Loc[0], (int) p1Loc[1], null);
        } else {
            drawBall(ballLocation);
        }

        //draw the shot meter if the player is currently shooting
        if (shot) {
            //make ai jump towards the player
            posession = false;
            if (!jumpExecuted) {
                double[] aimovement0 = {0, 0};
                if (GamePanel.getp2Loc()[0] < p1Loc[0]) {
                    aimovement0[0] = p2Speed * 6;
                } else {
                    aimovement0[0] = -1 * p2Speed * 6;  
                }
                if (GamePanel.getp2Loc()[1] < p1Loc[1]) {
                    aimovement0[1] = p2Speed * 6;
                } else {
                    aimovement0[1] = -1 * p2Speed * 6;
                }
                movep2(aimovement0, p2Speed);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("this shouldn't happen :)");
                }
                player1.setPose("shooting");
                player2.setPose("jumping");
                jumpExecuted = true;
            }

            //draw the shot meter
            java.awt.Graphics2D g3 = (java.awt.Graphics2D) g.create();
            g3.setStroke(new java.awt.BasicStroke(2)); // thickness of 3.0f
            g.setColor(white);
            freezePlayers = true;
            g.drawRect((int) p1Loc[0] - 10, (int) p1Loc[1] - 40, 95, 20);
            if (currentShotProgress < 0.95) {
                g.setColor(Color.WHITE);
                g.fillRect((int) p1Loc[0] - 10, (int) p1Loc[1] - 40, (int) (95 * currentShotProgress), 20);
            } else if (currentShotProgress > 0.95 && currentShotProgress < 1.05) {
                g.setColor(Color.GREEN);
                g.fillRect((int) p1Loc[0] - 10, (int) p1Loc[1] - 40, 95, 20);
            } else if (currentShotProgress > 1.05 && currentShotProgress < 2) {
                g.setColor(Color.WHITE);
                g.fillRect((int) p1Loc[0] - 10, (int) p1Loc[1] - 40, (int) (95 * (2 - currentShotProgress)), 20);
            } else {
                g.setColor(Color.RED);
                g.fillRect((int) p1Loc[0] - 10, (int) p1Loc[1] - 40, 95, 20);
            }

            if (shotCompleted) {
                double num = (int) (Math.random() * 100) + 1;
                System.out.println(num + " " + currentShot.calculateProbability() * 100);
                if (num < currentShot.calculateProbability() * 100) {
                    ballLocation[0] = hoopLocation[0];
                    ballLocation[1] = hoopLocation[1];
                    drawBall(ballLocation);
                    repaint();
                }
                shotCompleted = false;
            }

        } else {
            //System.out.println("shot probability: " + currentShot.calculateProbability()); // this doesnt work (null pointer)
            freezePlayers = false;
            player1.setPose("dribbling");
            player2.setPose("running");

        }

        //ai defense
        //make a line between the hoop and the player
        //if the ai isn't on that line and as close to the player as possible, get it to that position
        //make a smaller similar triangle with the two legs on top of the two current legs of the bigger triangle. 
        //put the ai on the tip of that triangle
        //hoop location: (265, 475)
        aiDesiredPosition = calculateAIPos();
        if (isClose(aiDesiredPosition, p2Loc,25)) {
            player2.setPose("defense");
            freezep2 = true;
        } else {
            player2.setPose("running");
            freezep2 = false;
        }
        if (!GamePanel.getp1Loc().equals(GamePanel.startingLocs[0]) || (!(isClose(p1Loc[0], p2Loc[0]) && !isClose(p1Loc[1], p2Loc[1])))) {
            double[] aimovement = {0, 0};
            if (GamePanel.getp2Loc()[0] < aiDesiredPosition[0]) {
                aimovement[0] = p2Speed;
            } else {
                aimovement[0] = -1 * p2Speed;  
            }
            if (GamePanel.getp2Loc()[1] < aiDesiredPosition[1]) {
                aimovement[1] = p2Speed;
            } else {
                aimovement[1] = -1 * p2Speed;
            }

            //if (freezep2) {
                // System.out.print(Arrays.toString(aiDesiredPosition) + "   ");
                // System.out.println(Arrays.toString(p2Loc));
            //}
            
            if (!freezep2) {
                movep2(aimovement, p2Speed);
            } 
            repaint();
        } else {
            //don't move
            double[] aiMovement = {0, 0};
            movep2(aiMovement, p2Speed);
        }

        drawHoop();
        
        if (isClose(ballLocation, hoopLocation, 17)) {
            
            resetPoint();

            
        }

        //endgame
        if (player1.getScore() == 7) {
            freezePlayers = true;
            
        } else if (player2.getScore() == 7) {
            freezePlayers = true;
        }

    }

    public static void moveBall() {
        ballLocation[0] = p1Loc[0];
        ballLocation[1] = p1Loc[1];
    }

    public static double[] calculateAIPos() {
        double leg1 = p1Loc[0]-hoopLocation[0]; //horizontal leg (adjacent)
        double leg2 = Math.abs(p1Loc[1]-hoopLocation[1]); //vertical leg (opposite)
        double hyp = Math.sqrt(Math.pow(leg1, 2) + Math.pow(leg2, 2));
        //tan(theta) = opposite / adjacent
        double theta = arctan(leg2/leg1);

        double newhyp = hyp * 0.15;    
        //cos(theta) = adjacent / hypotenuse    
        double newleg2 = newhyp * sin(theta);
        //sin(theta) = opposite / hypotenuse
        double newleg1 = newhyp * cos(theta);

        double[] pos = new double[2];
        pos[1] = p1Loc[1] - newleg1;
        if (p1Loc[1] - hoopLocation[1] > 0) {
            pos[1] = p1Loc[1] - newleg2;
        } else {
            pos[1] = p1Loc[1] + newleg2;
        }
        pos[0] = p1Loc[0] - newleg1;

        if (looseBall) {
            return ballLocation;
        }
        return pos;
    }

    public static void changeAIPosition(double[] aiMovement) {
        aiDesiredPosition[0] += aiMovement[0];
        aiDesiredPosition[1] += aiMovement[1];
    }

    public static boolean isClose(double num1, double num2) {
        //hoop location: (210, 475)
        return (Math.abs((num1-75)-num2) < 50);
    }

    public static boolean aiClose(double num1, double num2) {
        //hoop location: (210, 475)
        return (Math.abs(num1-num2) < 25);
    }

    public static boolean shotMade(double num1, double num2) {
        return (Math.abs((num1 - num2)) < 17);
    }

    public static double[] getp1Loc() {   
        return p1Loc;
    }

    public static double[] getp2Loc() {
        return p2Loc;
    }

    public void movep2(double[] movement, double p2Speed) {

        //boundaries
        p2Loc[0] += movement[0];
        p2Loc[1] += movement[1];
        if (GamePanel.getp2Loc()[0] > 1760) {
            GamePanel.getp2Loc()[0] -= p2Speed;
            repaint();
        } else if (GamePanel.getp2Loc()[0] < 225) {
            GamePanel.getp2Loc()[0] += p2Speed;
            repaint();
        }
        if (GamePanel.getp2Loc()[1] > 835) {
            GamePanel.getp2Loc()[1] -= p2Speed;
            repaint();
        }
        if (GamePanel.getp2Loc()[1] < 40) {
            GamePanel.getp2Loc()[1] += p2Speed;
            repaint();
        }

        try {
            throw new RuntimeException("player 2 stack trace");
        } catch(Exception ex) {
            //ex.printStackTrace();
        }

        p2Refresh++;

    }

    public static void movep1(double[] movement) {
        p1Loc[0] += movement[0];
        p1Loc[1] += movement[1];
        try {
            throw new RuntimeException("player 1 stack trace");
        } catch(Exception ex) {
            //ex.printStackTrace();
        }
        p1Refresh++;
        

        
    }

    //to make finding coordinates easier :)
    public void drawGrid() {
        for (int counter = 0; counter < 2000; counter += 50) {
            this.g.drawLine(counter, 0, counter, 2000);
        }
        for (int counter2 = 0; counter2 < 2000; counter2 += 50) {
            this.g.drawLine(0, counter2, 2000, counter2);
        }
    }

    public void setFontSize(int size) {
		this.font = new Font("DialogInput", Font.BOLD, size);
		g.setFont(this.font);
	}

    public void drawPlayer(Graphics g, String filename, int[] size, int[] loc) {
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			g.drawImage(MenuPanel.resize(image, size[0], size[1]), loc[0], loc[1], null);
		} catch (IOException ex) {
			System.out.println("ioexception called");
		}
	}

    public void drawCourt(Graphics g, String filename, int[] loc) {
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			//g.drawImage(image, loc[0], loc[1], null);
            g.drawImage(MenuPanel.resize(image, 1700, 850), loc[0], loc[1], null);
		} catch (IOException ex) {
			System.out.println("ioexception called");
		}
	}

    public void drawHoop() {
		try {
			BufferedImage image = ImageIO.read(new File("sprites/hoop2.png"));
			g.drawImage(MenuPanel.resize(image, 200, 200), 120, 340, null);
		} catch (IOException ex) {
			System.out.println("ioexception called");
		}
	}

    public void drawBall(double[] loc) {
        try {
			BufferedImage image = ImageIO.read(new File("sprites/ball.png"));
			g.drawImage(MenuPanel.resize(image, 50, 50), (int) loc[0], (int) (loc[1]), null);
		} catch (IOException ex) {
			System.out.println("ioexception called");
		}
    }

    public void drawScoreboard() {

        //scoreboard
        g.setColor(primaryColor);
        g.fillRect(1300, 725, 570, 200);
        g.setColor(secondaryColor);
        g.drawRect(1300, 725, 570, 200);
        g.drawLine(1500, 725, 1500, 925);
        g.drawLine(1300, 785, 1700, 785);
        g.drawLine(1700, 725, 1700, 925);
        //scoreboard contents
        setFontSize(25);
        g.setColor(white);
        g.drawString(String.format("%.11s", player1.getName()), 1315, 762);
        g.drawString(String.format("%.11s", player2.getName()), 1515, 762);
        setFontSize(120);
        g.drawString(player1.getScore() + "", 1335, 890);
        g.drawString(player2.getScore() + "", 1535, 890);
        //every time there is a new point, refresh the shot clock
        if (!shotClockStarted) {
            shotClock = new ShotClock();
            shotClock.start();
            shotClockStarted = true;
        }
        //if the player is taking a shot, stop the shot clock
        int timeLeft = 0;
        if (shot) {
            shotClock.stop();
            timeLeft = (int) (15 - shotClock.getTimeSeconds());
        } else {
            timeLeft = (int) (15 - shotClock.getCurrentTime());
        }
        //draw the amount of time left, make sure not to draw a negative number
        
        if (timeLeft > 0) {
            g.drawString(timeLeft + "", 1712, 865);
        } else {
            freezePlayers = true;
            g.drawString("0", 1712, 865);
        }
    }

    public static void shotDone(double time) {
        shotCompleted = true;
        currentShot = new Shot(time, player1, player2, p1Loc, p2Loc);
    }

    public static void passWatch(StopWatch shotClock1) {
        shotClock = shotClock1;
    }

    public static double sin(double num) { return Math.sin(num); }
    public static double cos(double num) { return Math.cos(num); }
    public static double arcsin(double num) { return Math.asin(num); }
    public static double arccos(double num) { return Math.acos(num); }
    public static double arctan(double num) { return Math.atan(num); }

}