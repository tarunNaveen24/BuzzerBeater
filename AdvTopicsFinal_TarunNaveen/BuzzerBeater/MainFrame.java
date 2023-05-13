package BuzzerBeater;


import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MainFrame extends JFrame {
    public static final int WIDTH = 1925;
	public static final int HEIGHT = 1015;
 	
	public static final int MENU = 0;
	public static final int GAME = 1;

    public static GamePanel gamePanel;
    public static MenuPanel menuPanel;

	public static boolean shot = false;
	private StopWatch shotMeter = new StopWatch();
	public static double shotTimer;

	private double[] movement = {0, 0};

	private ArrayList<KeyEvent> keysPressed = new ArrayList<>();
	//up, down, left, right
	private boolean[] keys = {false, false, false, false};

    public static void startGUI() throws InterruptedException {
		MainFrame theGUI = new MainFrame();
		
		// Starts the UI Thread and creates the the UI in that thread.
		// Uses a Lambda Expression to call the createFrame method.
		// Use theGUI as the semaphore object
		SwingUtilities.invokeLater( () -> theGUI.createFrame(theGUI) );

		synchronized (theGUI ) {
			theGUI.wait();
		}
	}

    /**
	 * Create the main JFrame and all animation JPanels.
	 * 
	 * @param semaphore The object to notify when complete
	 */
	public void createFrame(Object semaphore) {
		// Sets the title found in the Title Bar of the JFrame
		this.setTitle("Buzzer Beater");
		// Sets the size of the main Window
		this.setSize(WIDTH, HEIGHT);
		// Allows the application to properly close when the
		// user clicks on the Red-X.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(null);

        addMenuBar();
	
		// create the game panel, but don't add it to the JFrame, yet.
        this.setVisible(true);

        menuPanel = new MenuPanel(this);
		menuPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(menuPanel);
        menuPanel.setVisible(true);
		
		// tell the main thread that we are done creating our stuff
		synchronized (semaphore) {
			semaphore.notify();
		}

		this.addKeyListener(new KeyAdapter() {
			@Override
            public void keyPressed(KeyEvent ke) {

				keysPressed.add(ke);
				GamePanel.shot = shot;
				GamePanel.currentShotProgress = shotMeter.getCurrentTime();

				double p1Speed = 5 * (Attributes.speed.get(GamePanel.player1.getName()) / 100.0);

					if (!shot) {
						if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
							if (GamePanel.isClose(GamePanel.getp1Loc(), GamePanel.getp2Loc(), 50)
								&& GamePanel.getp2Loc()[1] > GamePanel.getp1Loc()[1]) {
								if ((Attributes.strength.get(GamePanel.player1.getName()) > 
									Attributes.strength.get(GamePanel.player2.getName()))) {
									keys[0] = true;
								} else {
									keys[0] = false;
								}
							} else {
								keys[0] = true;
							}
						} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
							if (GamePanel.isClose(GamePanel.getp1Loc(), GamePanel.getp2Loc(), 50)
								&& GamePanel.getp2Loc()[1] < GamePanel.getp1Loc()[1]) {
								if ((Attributes.strength.get(GamePanel.player1.getName()) > 
									Attributes.strength.get(GamePanel.player2.getName()))) {
									keys[1] = true;
								} else {
									keys[1] = false;
								}
							} else {
								keys[1] = true;
							}							
						} else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
							if (GamePanel.isClose(GamePanel.getp1Loc(), GamePanel.getp2Loc(), 50)
								&& GamePanel.getp2Loc()[0] < GamePanel.getp1Loc()[0]) {
								if ((Attributes.strength.get(GamePanel.player1.getName()) > 
									Attributes.strength.get(GamePanel.player2.getName()))) {
									keys[2] = true;
								} else {
									keys[2] = false;
								}
							} else {
								keys[2] = true;
							}
						} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
							if (GamePanel.isClose(GamePanel.getp1Loc(), GamePanel.getp2Loc(), 50)
								&& GamePanel.getp2Loc()[0] > GamePanel.getp1Loc()[0]) {
								if ((Attributes.strength.get(GamePanel.player1.getName()) > 
									Attributes.strength.get(GamePanel.player2.getName()))) {
									keys[3] = true;
								} else {
									keys[3] = false;
								}
							} else {
								keys[3] = true;
							}
						} else if (ke.getKeyCode() == KeyEvent.VK_S){
							System.out.println("shot taken");
							shotMeter.start();
							shot = true;
						} else {
							return;
						}
					} else {
						//System.out.println(shotMeter);
					}

					for (int index = 0; index < 4; index++) {
						if (keys[index]) {
							switch(index) {
								case 0: 
									movement[1] = p1Speed;
									break;
								case 1:
									movement[1] = -p1Speed;
									break;
								case 2:
									movement[0] = -p1Speed;
									break;
								case 3:
									movement[0] = p1Speed;
									break;
							}
							//keys[index] = false;
						}
					}
					if (!GamePanel.freezePlayers) {
						if (isClose(GamePanel.p1Refresh, GamePanel.p2Refresh, 5)) {
							GamePanel.movep1(movement);
						}
						GamePanel.moveBall();
					}
					//GamePanel.changeAIPosition(movement);
					movement[0] = 0;
					movement[1] = 0;
					repaint();
					//keysPressed.remove(ke);
				//}

                //boundaries
                if (GamePanel.getp1Loc()[0] > 1760) {
                    GamePanel.getp1Loc()[0] -= p1Speed;
                    repaint();
                } else if (GamePanel.getp1Loc()[0] < 225) {
                    GamePanel.getp1Loc()[0] += p1Speed;
                    repaint();
                }
                if (GamePanel.getp1Loc()[1] > 815) {
                    GamePanel.getp1Loc()[1] -= p1Speed;
                    repaint();
                }
                if (GamePanel.getp1Loc()[1] < 60) {
                    GamePanel.getp1Loc()[1] += p1Speed;
                    repaint();
                }
                
            }

			@Override
			public void keyReleased(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
					keys[0] = false;
					movement[1] = 0;
				} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
					keys[1] = false;
					movement[1] = 0;
				} 
				if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
					keys[2] = false;
					movement[0] = 0;
				} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
					keys[3] = false;
					movement[0] = 0;
				}
				if (ke.getKeyCode() == KeyEvent.VK_S) {
					shotTimer = shotMeter.stop();
					GamePanel.shotDone(shotTimer);
					GamePanel.currentShotProgress = 0;
					System.out.println("shot probability: " + GamePanel.currentShot.calculateProbability());
					shot = false;
				}
			}
		});
	}

	public static boolean isClose(int num1, int num2, int diff) {
		return (Math.abs(num1 - num2) < diff);
	}

    void changePanel(int panelIndex) {
		// The drawing of the scroll bar and management of the layout
		// gets messed up if we simply show/hide the panel.
		// Instead. Remove & Add the panels while also showing/hiding.
		// if (panelIndex == MENU) {
		// 	this.remove(gamePanel);
		// } else {
		// 	this.add(gamePanel);
		// 
		//remove menu
		menuPanel.setVisible(false);
		this.remove(menuPanel);
		//add game
		gamePanel = new GamePanel();
		gamePanel.setSize(WIDTH, HEIGHT);
		gamePanel.setBounds(0,0,WIDTH,HEIGHT);
        this.add(gamePanel);
		
		gamePanel.setVisible(true);
		this.revalidate();
		this.repaint();
	}

    /**
	 * Add some menu options to control the experience.
	 */
	private void addMenuBar() {
		
		JMenuBar bar = new JMenuBar();
		// Add the menu bar to the JFrame
		this.setJMenuBar(bar);
		
		// Add more top-level menu options for the specific animation panel
		JMenu menu = new JMenu("Game Options");
		menu.setMnemonic('s');
        JMenuItem item = new JMenuItem("Start Game", 'g');
        item.addActionListener(e -> changePanel(0));
        menu.add(item);
        item = new JMenuItem("Restart Game", 'r');
        menu.add(item);
        bar.add(menu);
        
	}

    public static void doneCallback() {
        //setVisible(0);
    }

}
