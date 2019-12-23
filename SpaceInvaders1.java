import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;

public class SpaceInvaders1 extends JFrame implements ActionListener {

	GamePanel game;

	Timer myTimer;

    public SpaceInvaders1() {
		super("SpaceInvaders V.2001");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setLayout(new BorderLayout());


		game=new GamePanel();//creating the panel
		add(game);

		setVisible(true);

		myTimer=new Timer(15,this);
		myTimer.start();
    }

    public void actionPerformed(ActionEvent evt){
		game.move();
		game.repaint();
		
    }

    public static void main(String[] arguments) {
		SpaceInvaders1 frame = new SpaceInvaders1();
    }
}

class GamePanel extends JPanel implements KeyListener{
	private boolean[] keys;
	private Image block = new ImageIcon("block.png").getImage();
	private Rectangle player = new Rectangle(125,250,50,50);
	private boolean jump=false;
	private boolean grounded=true;
	private int jumpcounter=-24;
	private int velY, velX;

	public GamePanel(){
		keys=new boolean[KeyEvent.KEY_LAST+1];
		setSize(800,600);
		addKeyListener(this);

	}
	
	/*public void up(){
        velY = -10;
        velX = 0;
        
        if (jump==true){
        	jumpcounter=jumpcounter+2;
        }
        
        if (jumpcounter>=10){
        	jump=false;
        	jumpcounter=0;
        }
        player.translate(velX,velY+jumpcounter);
    }

    public void left(){
        velX = -5;
        velY = 0;
        player.translate(velX,velY);
    }

    public void right(){
        velX = 5;
        velY = 0;
        player.translate(velX,velY);
    }*/
    
    public void move(){
    	requestFocus();
    	velY=0;
    	velX=0;
    	
		if (keys[KeyEvent.VK_RIGHT]){//if they click the right arrow
			velX = 10;
        	velY = 0;
		}
		if (keys[KeyEvent.VK_LEFT]){//if they click the left arrow
			velX = -10;
        	velY = 0;
		}
		if (keys[KeyEvent.VK_UP]){//if they click the left arrow
			if (jump!=true){
				jump=true;
				velY = 0;
        		velX = 0;	
			}
		}
		
		if (jump==true){
			
			if (jumpcounter==26){
			
				jumpcounter=-24;
				jump=false;
				
			}
			velY+=jumpcounter;
			jumpcounter+=2;
		}
		
		player.translate(velX,velY);
		System.out.println(player.getLocation());

		
    }

	//********************************
	public void keyTyped(KeyEvent e){}

	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()]=true;
	}
	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()]=false;
	}
	//********************************
	
	//all drawing code goes here
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//g.fill(254,254,254);
		g.drawImage(block,(int)(player.getX()),(int)(player.getY()),this);
    }
}


