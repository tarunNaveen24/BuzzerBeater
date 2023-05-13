package BuzzerBeater;

import java.io.File;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel {

    private Font font = new Font("DialogInput", Font.BOLD, 140);
    private Graphics g;
	private String[] players = {"S. Curry", "L. James", "M. Jordan", "S. O'Neal", "L. Doncic", "G. Antetekounmpo", "K. Bryant", "N. Jokic", "D. Nowitzki", "K. Durant"};
	private String[][] playersM = {{"S. Curry", "G. Antetekounmpo"}, {"L. James", "K. Bryant"}, {"M. Jordan", "N. Jokic"}, {"S. O'Neal", "D. Nowitzki"}, {"L. Doncic", "K. Durant"}};
	private Attributes attributes = new Attributes();
	private Color primaryColor = Color.BLACK;
	private Color secondaryColor = Color.RED;
	private Color tertiaryColor = Color.DARK_GRAY;
	private int targetScore = 7;
	private int currentPlayerSel = 1;
	private String player1 = "";
	private String player2 = "";
	private MainFrame frame;

    public MenuPanel(MainFrame frame) {
		this.frame = frame;
        this.addEventHandlers();
		this.attributes = (Attributes) attributes; //get rid of yellow error :)
    }

    @Override
	public void paintComponent(Graphics g){  
		this.g = g;
		g.setFont(font);
        super.paintComponent(g);
		this.setBackground(primaryColor);
		
		g.setColor(tertiaryColor);
		g.fillRect(0, 200, 900, 800);
		g.setColor(secondaryColor);

		//set a thicker stroke for grid lines
		Graphics2D g2 = (Graphics2D) g;
		Float size = ((Double) 5.0).floatValue();

		//grid for player names and stats
		g2.setStroke(new BasicStroke(size));
		g.drawLine(0, 200, 900, 200);
		g.drawLine(900, 200, 900, 1000);
		g.drawLine(0, 350, 900, 350);
		g.drawLine(0, 500, 900, 500);
		g.drawLine(0, 650, 900, 650);
		g.drawLine(0, 800, 900, 800);
		g.drawLine(0, 950, 900, 950);
		g.drawLine(450, 200, 450, 1000);
		g.drawLine(0, 200, 0, 1000);

		//draw names
		g.setColor(Color.WHITE);
		int count = 0;
		for (int rows = 0; rows < 2; rows++) {
			for (int cols = 0; cols < 5; cols++) {
				setFontSize(35);
				String stats = Attributes.getStringRep(players[count]).substring(0);
				g.drawString(players[count], rows * 450 + 15, cols * 150 + 250);
				setFontSize(15);
				g.drawString(stats.substring(0, stats.indexOf("|")), rows * 450 + 15, cols * 150 + 291);
				g.drawString(stats.substring(stats.indexOf("|") + 1), rows * 450 + 15, cols * 150 + 316);
				count++;
			}
		}

		//draw player circles
		setFontSize(20);
		int[] pointLoc = {1015, 625};
		drawImage(g, "sprites/ovalSprite.png", 250, pointLoc);
		g.drawString("PLAYER 1", 1170, 800);
		pointLoc[0] = 1425;
		drawImage(g, "sprites/ovalSprite.png", 250, pointLoc);
		g.drawString("PLAYER 2", 1570, 800);

		//draw other buttons
		setFontSize(40);
		g.setColor(Color.DARK_GRAY);
		g.drawString("START GAME ->", 1525, 890);
		g.setColor(Color.WHITE);
		g.drawString("TARGET SCORE: " + this.targetScore, 1000, 890);

		//draw other text
		setFontSize(40);
		if (this.currentPlayerSel == 1) {
			g.drawString("Choose your player:", 1170, 185);
		} else if (this.currentPlayerSel == 2) {
			pointLoc[0] = 1015;
			pointLoc[1] = 185;
			drawPlayer(g, "sprites/" + player1 + ".png/", 250, pointLoc);
			setFontSize(30);
			pointLoc[1] = 310;
			setFontSize(40);
			g.drawString("Choose your opponent:", 1140, 185);
		} else {
			pointLoc[0] = 1015;
			pointLoc[1] = 185;
			drawPlayer(g, "sprites/" + player1 + ".png/", 250, pointLoc);
			pointLoc[1] = 310;
			setFontSize(30);
			pointLoc[0] = 1425;
			setFontSize(40);
			pointLoc[1] = 185;
			drawPlayer(g, "sprites/" + player2 + ".png/", 250, pointLoc);
			
			g.setColor(Color.GREEN);
			setFontSize(40);
			GamePanel.setPlayers(player1, player2);
			g.drawRect(1500, 845, 360, 70);
			g.drawString("START GAME ->", 1525, 890);
			//setFontSize(50);
			g.setColor(Color.WHITE);
			//g.drawString("Ready to start!", 1220, 185);
		}

		//draw title
		setFontSize(130);
		g.drawString("BUZZER BEATER", 400, 120);
	}

	public void setFontSize(int size) {
		this.font = new Font("DialogInput", Font.BOLD, size);
		g.setFont(this.font);
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

	public void drawImage(Graphics g, String filename, int size, int[] loc){  
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			g.drawImage(resize(image, size + 120, size - 100), loc[0], loc[1], null);
		} catch (IOException ex) {
			System.out.println("ioexception called");
		}
    }

	public void drawPlayer(Graphics g, String filename, int size, int[] loc) {
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			g.drawImage(resize(image, size + 150, size + 300), loc[0], loc[1], null);
		} catch (IOException ex) {
			System.out.println("ioexception called");
		}
	}

    private void addEventHandlers() {
    	this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					onMouseClicked(e);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
    	});
    	this.addMouseMotionListener(null);
    }

	private void onMouseClicked(MouseEvent e) throws InterruptedException {
		int x = e.getX();
		int y = e.getY();
		
		//if player clicks on a player box
		int count = 0;
		int inCount = 0;
		for (int xCord = 0; xCord < 900; xCord += 450) {
			count++;
			inCount = 0;
			for (int yCord = 200; yCord < 900; yCord += 150) {
				inCount++;
				if (x > xCord && x < xCord + 450) {
					if (y > yCord && y < yCord + 150) {
						if (this.currentPlayerSel == 1) {
							this.player1 = playersM[inCount - 1][count - 1];
						} else if (this.currentPlayerSel == 2) {
							this.player2 = playersM[inCount - 1][count - 1];
						}
						this.currentPlayerSel++;
						repaint();
					}
				}
			}
		}
		
		//if player clicks on the start game button
		if (this.currentPlayerSel > 2) {
			if (x > 1500 & x < 1860 && y > 845 && y < 915) {
				frame.changePanel(1);
				// MainFrame.menuPanel.setVisible(false);
				// MainFrame.gamePanel.setVisible(true);
				
				System.out.println("Game Started");
				//setVisible(1);
			}
		}
    }

    //credit: https://stackoverflow.com/questions/9417356/bufferedimage-resize
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return dimg;
	}  

}