import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;

public class SpaceInvaders extends JFrame implements ActionListener {
	GamePanel game;

	Timer myTimer;

    public SpaceInvaders() {
		super("SpaceInvaders V.2001");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setLayout(new BorderLayout());


		game=new GamePanel();//creating the panel
		game.createEnemies();//creating enemies and barriers here
		game.createBarricades();
		add(game);

		setVisible(true);

		myTimer=new Timer(15,this);
		myTimer.start();
    }

    public void actionPerformed(ActionEvent evt){
		game.setBorders();//calling all my game functions
		game.enemyMove();
		game.enemyAttack();
		game.move();
		game.attack();
		game.check();
		game.repaint();
    }

    public static void main(String[] arguments) {
		SpaceInvaders frame = new SpaceInvaders();
    }
}

class GamePanel extends JPanel implements KeyListener{
	private int boxx,boxy,bulletx,bullety,lives=3, score=0,finalScore,shootCounter;
	private int ufoX=1000,ufoY=35;
	private int displayCounter=0;

	private boolean[] keys;
	private boolean attack=false, right=true, end=false, hit=false, finaly=false, dead=false;

	private ArrayList<aliens> enemies =  new ArrayList<aliens>();
	private ArrayList<Objects> enemyBullets = new ArrayList<Objects>();
	private ArrayList<Barricades> barricades = new ArrayList<Barricades>();
	private ArrayList<Objects> barricade1 = new ArrayList<Objects>();
	private ArrayList<Objects> barricade2 = new ArrayList<Objects>();
	private ArrayList<Objects> barricade3 = new ArrayList<Objects>();
	private ArrayList<Objects> barricade4 = new ArrayList<Objects>();
	private UFO saucer = new UFO(ufoX,ufoY,50,45);

	private Image background, ship, shot,scaleImage,minion1,minion2, shot2, redCrate, blueCrate, gameover, UFO, win;
	private Rectangle2D alienCheck, bulletCheck, barricadeCheck, ufoCheck;
	private Font myFont=new Font("Arial",Font.BOLD,30);
	private Font small=new Font("Arial",Font.BOLD,14);
	

	public GamePanel(){
		keys=new boolean[KeyEvent.KEY_LAST+1];

		background = new ImageIcon("background.png").getImage();//loading pics
		ship = new ImageIcon("ship1.png").getImage();
		shot = new ImageIcon("shot2.png").getImage();
		shot2 = new ImageIcon("minion1shot.png").getImage();
		minion1 = new ImageIcon("minion1.png").getImage();
		minion2 = new ImageIcon("minion2.png").getImage();
		redCrate = new ImageIcon("redcrate.png").getImage();
		blueCrate = new ImageIcon("bluecrate.png").getImage();
		gameover = new ImageIcon("gameover.png").getImage();
		UFO = new ImageIcon("ufo.png").getImage();
		win = new ImageIcon("YouWin.png").getImage();


		scaleImage = background.getScaledInstance(800,600, Image.SCALE_DEFAULT);//scaling them to size
		gameover = gameover.getScaledInstance(800,600, Image.SCALE_DEFAULT);
		win = win.getScaledInstance(800,600,Image.SCALE_DEFAULT);
		minion1 = minion1.getScaledInstance(40,40, Image.SCALE_DEFAULT);
		minion2 = minion2.getScaledInstance(40,40, Image.SCALE_DEFAULT);
		redCrate = redCrate.getScaledInstance(10,10, Image.SCALE_DEFAULT);
		blueCrate = blueCrate.getScaledInstance(10,10, Image.SCALE_DEFAULT);
		UFO = UFO.getScaledInstance(75,68,Image.SCALE_DEFAULT);
		

		boxx=368;
		boxy=500;

		setSize(800,600);
		addKeyListener(this);

	}

	//********************************
	public void keyTyped(KeyEvent e){
	}

	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()]=true;
	}
	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()]=false;
	}
	//********************************

	public void createEnemies(){
		for (int y=0;y<4;y++){//row or their y coordinate
			for (int i=20;i<=650;i+=65){//x coordinate
				enemies.add(new aliens(i,95+y*45,40,40));//adding them to an arraylist
			}
		}
	}

	public void createBarricades(){//i made 4 arraylists, each one represeting 1 barricade, and 1 barricade is made up of 5 rows of 5 of 10x10 small squares
		for (double i=120;i<=170;i+=10){
			for (double y=370;y<=420;y+=10){
				barricade1.add(new Objects(i,y,10,10));
			}
		}
		for (double i=290;i<=340;i+=10){
			for (double y=370;y<=420;y+=10){
				barricade2.add(new Objects(i,y,10,10));
			}
		}
		for (double i=460;i<=510;i+=10){
			for (double y=370;y<=420;y+=10){
				barricade3.add(new Objects(i,y,10,10));
			}
		}
		for (double i=630;i<=680;i+=10){
			for (double y=370;y<=420;y+=10){
				barricade4.add(new Objects(i,y,10,10));
			}
		}
	}

	public void enemyAttack(){
		if (enemies.size()>0){
			Rectangle2D ship = new Rectangle2D.Double(boxx,boxy,64,47);//creating a rectangle 2D to use their intersect function
			if (enemies.size()>=25){//if there are more than 24 enemies, there will be 3 bullets at any given time
				while (enemyBullets.size()<3){
					int index = (int )(Math.random() * enemies.size());//picking a random enemy to shoot
					enemyBullets.add(new Objects(enemies.get(index).getX()+16,enemies.get(index).getY(),17,8));//adding an object to bulletlist
				}
			}
			if (enemies.size()<25 && enemies.size()>=8){//if there are 8-24 enemies, there will be 2 bullets at any given time
				while (enemyBullets.size()<2){
					int index = (int )(Math.random() * enemies.size());
					enemyBullets.add(new Objects(enemies.get(index).getX()+16,enemies.get(index).getY(),17,8));
				}
			}
			else{
				while (enemyBullets.size()<1){//only 1 bullet
					int index = (int )(Math.random() * enemies.size());
					enemyBullets.add(new Objects(enemies.get(index).getX()+16,enemies.get(index).getY(),17,8));
				}
			}
			
			for (int i=0;i<enemyBullets.size();i++){//going through each enemyBullet
				if (enemyBullets.get(i).getY()>=600){//if its out of the screen
					enemyBullets.remove(i);//removing and calling attack to make a new bullet
					enemyAttack();
				}
				if (ship.intersects(enemyBullets.get(i).getX(),enemyBullets.get(i).getY(),8,17)){//if ship intersects with given rect area 
					lives-=1;//subtract 1 from lives
					enemyBullets.remove(i);//removing bullet and calling attack to make new bullet
					enemyAttack();
				}
				/*for the next 4 loops, this is the sequence of code:
				 - go through each mini rect in each barricade
				 -make a rectangle 2d with each mini rect to use intersect function
				 -checks if the mini rect collides with enemy bullet
				 -if it does, remove it, remove enemybullet and call attack
				 */
				for (int l=0;l<barricade1.size();l++){
					barricadeCheck = new Rectangle2D.Double(barricade1.get(l).getX(),barricade1.get(l).getY(),5,5);
					if (barricadeCheck.intersects(enemyBullets.get(i).getX(),enemyBullets.get(i).getY(),8,17)){
						barricade1.remove(l);
						enemyBullets.remove(i);
						enemyAttack();
					}
				}
				for (int l=0;l<barricade2.size();l++){
					barricadeCheck = new Rectangle2D.Double(barricade2.get(l).getX(),barricade2.get(l).getY(),5,5);
					if (barricadeCheck.intersects(enemyBullets.get(i).getX(),enemyBullets.get(i).getY(),8,17)){
						barricade2.remove(l);
						enemyBullets.remove(i);
						enemyAttack();
					}
				}
				for (int l=0;l<barricade3.size();l++){
					barricadeCheck = new Rectangle2D.Double(barricade3.get(l).getX(),barricade3.get(l).getY(),5,5);
					if (barricadeCheck.intersects(enemyBullets.get(i).getX(),enemyBullets.get(i).getY(),8,17)){
						barricade3.remove(l);
						enemyBullets.remove(i);
						enemyAttack();
					}
				}
				for (int l=0;l<barricade4.size();l++){
					barricadeCheck = new Rectangle2D.Double(barricade4.get(l).getX(),barricade4.get(l).getY(),5,5);
					if (barricadeCheck.intersects(enemyBullets.get(i).getX(),enemyBullets.get(i).getY(),8,17)){
						barricade4.remove(l);
						enemyBullets.remove(i);
						enemyAttack();
					}
				}
			}
			
			//just moving each bullet 3.5 down
			for (int i=0;i<enemyBullets.size();i++){
				enemyBullets.get(i).translate(0,3.5);
			}
		}
	}

	public void setBorders(){//in the original game, once the last row on each side is killed, the remaining enemies still move all the way to the side
	//so this function just helps me find when they hit the end
	int counter=0;
	if (enemies.size()>0){
		for (int i=0;i<enemies.size();i++){
			if (end==false){
				if (enemies.get(i).getX()==15){
					end=true;
				}
				else if (enemies.get(i).getX()==715){
					end=true;
				}
			}
			if (enemies.get(i).getY()>=310){//when they get down to a certain spot, they cannot go down anymore
				counter+=1;
				finaly=true;
			}
			
		}
		if (counter==0){//basically if the player kills everyone in the last row, this sets a boolean to be false, so the rest can move down again
			finaly=false;
		}	
	}
	
	}

	public void enemyMove(){
		if (enemies.size()>0){
			for (int i=0;i<enemies.size();i++){//going through each enemy
				if (end==true && finaly==false){//once they reached the end and not the final position
					enemies.get(i).translate(0.0,10.0);//move down
				}
				else{
					if (right==true){//which direction they need to move to
						enemies.get(i).translate(0.25,0.0);
					}
					else{
						enemies.get(i).translate(-0.25,0.0);
					}
				}
			}
			ufoX-=1;//moving the ufo
			if (ufoX<=-450){//once the ufo reaches a certain point, im resetting it so it can appear again
				ufoX=1000;
				saucer.reset();//if its hit, then im resetting it so its not hit
			}
			if (end){
				reverseDirection();//changing the direction they move
			}	
		}	
	}

	public void reverseDirection(){
		//just simple if statements and reversing boolean
		if (right==true){
			right=false;
			for (int i=0;i<enemies.size();i++){
				enemies.get(i).translate(-0.50,0.0);//just a fix for a bug, so they dont' stay in the same position and trigger end=true again
			}
		}
		else{
			right=true;
			for (int i=0;i<enemies.size();i++){
				enemies.get(i).translate(0.50,0.0);
			}
		}
		end=false;//setting end to be false so it doesn't constantly move down
	}

	public void check(){
		if (lives<0){//if theres no more lives
			dead=true;
		}
		if (enemies.size()>0){
			for (int i=0;i<enemies.size();i++){
				alienCheck = new Rectangle2D.Double(enemies.get(i).getX(),enemies.get(i).getY(),35,35);//making rectangle2d
				if (alienCheck.intersects(bulletx,bullety,10,10)){//if it intersects with players bullet
					score+=25;//adding score, all aliens are the same score
					enemies.remove(i);
					attack=false;//attack=false
					bullety-=1000;//moving the bullety so it doesnt hit anything else
				}
			}	
		}
		
		/*for (int i=0;i<enemyBullets.size();i++){
			bulletCheck = new Rectangle2D.Double(enemyBullets.get(i).getX(),enemyBullets.get(i).getY(),8,17);
			if (bulletCheck.intersects(bulletx,bullety,10,10)){
				enemyBullets.remove(i);
				attack=false;
				bullety-=1000;
			}
		}*/
		//////////////////////-Barricade Intersection-///////////////////////
		//same thing as the one in the enemy attack except for making attack false, and changing bullety
		for (int l=0;l<barricade1.size();l++){
				bulletCheck = new Rectangle2D.Double(bulletx,bullety,10,10);
				if (bulletCheck.intersects(barricade1.get(l).getX(),barricade1.get(l).getY(),10,10)){
					barricade1.remove(l);
					attack=false;
					bullety-=1000;
				}
		}
		for (int l=0;l<barricade2.size();l++){
				bulletCheck = new Rectangle2D.Double(bulletx,bullety,10,10);
				if (bulletCheck.intersects(barricade2.get(l).getX(),barricade2.get(l).getY(),10,10)){
					barricade2.remove(l);
					attack=false;
					bullety-=1000;
				}
		}
		for (int l=0;l<barricade3.size();l++){
				bulletCheck = new Rectangle2D.Double(bulletx,bullety,10,10);
				if (bulletCheck.intersects(barricade3.get(l).getX(),barricade3.get(l).getY(),10,10)){
					barricade3.remove(l);
					attack=false;
					bullety-=1000;
				}
		}
		for (int l=0;l<barricade4.size();l++){
				bulletCheck = new Rectangle2D.Double(bulletx,bullety,10,10);
				if (bulletCheck.intersects(barricade4.get(l).getX(),barricade4.get(l).getY(),10,10)){
					barricade4.remove(l);
					attack=false;
					bullety-=1000;
				}
		}
		/////////////////////////////////////////////////////////////////////
		ufoCheck = new Rectangle2D.Double(ufoX,ufoY,50,45);//if it hits the ufo
		if (ufoCheck.intersects(bulletx,bullety,8,17) && saucer.getHit()!=true){
			saucer.hit();
			score+=150;//add the score
			attack=false;
			bullety-=1000;
		}
	}

	public void move(){
		requestFocus();
		if (keys[KeyEvent.VK_RIGHT]){//if they click the right arrow
			boxx+=3;//move 3
			if (boxx>715){//if they hit the sides
				boxx=715;
			}
		}
		if (keys[KeyEvent.VK_LEFT]){//if they click the left arrow
			boxx-=3;
			if (boxx<5){//if they hit the sides
				boxx=5;
			}
		}
	}

	public void attack(){
		if (dead==false){//if they're still alive
			if (keys[KeyEvent.VK_SPACE]){//space to attack
				if (attack==false){//if there isnt already a bullet
					bulletx=boxx+27;//setting their coordinates to be the middle of the ship
					bullety=boxy+5;
	
				}
				attack=true;
				if (bullety<=0){//if its off screen
					attack=false;
				}
			}
			if (attack){
				bullety-=9;//moving bullet
			}
		}
	}

	//all drawing code goes here
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (lives>=0){//if player isnt dead
			g.drawImage(scaleImage,0,0,this);//drawing background first
			if (displayCounter<=300){
				displayCounter+=1;
				g.setFont(small);//showing basic score and lives
		        g.setColor(Color.WHITE);
		        g.drawString("ARROW KEYS to move",305,50);
		        g.drawString("SPACE to shoot",330,75);
			}
	         g.drawImage(ship,boxx,boxy,this);//drawing ship

			 if (attack==true){//if player attacked
			 	g.drawImage(shot,bulletx,bullety,this);//drawing bullet
			 }
			 for (int i=0;i<enemyBullets.size();i++){//drawing all enemybulelts
			 	g.drawImage(shot2,(int)(enemyBullets.get(i).getX()),(int)(enemyBullets.get(i).getY()),this);
			 }
	         for (int i=0;i<enemies.size();i++){//drawing all enemies
	         	if (enemies.get(i).getBig()==true){//just to swtich the enemies up, displaying different sprites
	         		g.drawImage(minion1,(int)(enemies.get(i).getX()),(int)(enemies.get(i).getY()),this);
	         	}
	         	else{
	         		g.drawImage(minion2,(int)(enemies.get(i).getX()),(int)(enemies.get(i).getY()),this);
	         	}

	         }
	         //displaying all barricades
	         for (int i=0;i<barricade1.size();i++){
				g.drawImage(redCrate,(int)(barricade1.get(i).getX()),(int)(barricade1.get(i).getY()),this);
	         }
	         for (int i=0;i<barricade2.size();i++){
	         	g.drawImage(blueCrate,(int)(barricade2.get(i).getX()),(int)(barricade2.get(i).getY()),this);
	         }
	         for (int i=0;i<barricade3.size();i++){
	         	g.drawImage(redCrate,(int)(barricade3.get(i).getX()),(int)(barricade3.get(i).getY()),this);
	         }
	         for (int i=0;i<barricade4.size();i++){
	         	g.drawImage(blueCrate,(int)(barricade4.get(i).getX()),(int)(barricade4.get(i).getY()),this);
	         }
	         
	         if (saucer.getHit()==false){//if we didn't hit the saucer, then draw it
	         	g.drawImage(UFO,ufoX,ufoY,this);
	         }
	         g.setFont(myFont);//showing basic score and lives
	         g.setColor(Color.WHITE);
	         g.drawString("LIVES: "+lives,600,50);
	         g.drawString("SCORE: "+score,55,50);
		}
		if (lives<0){//gameover screen
			g.setFont(myFont);
			g.drawImage(gameover,0,0,this);
			g.setColor(Color.WHITE);
	        g.drawString("FINAL SCORE: "+score,270,50);
		}
		if (enemies.size()==0 && lives>=0){//you win screen
			g.setFont(myFont);
			g.drawImage(win,0,0,this);
			g.setColor(Color.WHITE);
	        g.drawString("FINAL SCORE: "+score,270,50);
		}

    }
}

class Objects{
	private double x,y,h,w;//coordinates, plus width and height

    public double getX(){//setters and getters
		return x;
    }
    public double getY(){
		return y;
    }
    public double getWidth(){
		return w;
    }
    public double getHeight(){
		return h;
    }

    public Objects(double x, double y, double h, double w){//constructor
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
    }

    public void translate(double dx, double dy){//simple moving function
		x += dx;
		y += dy;
    }

}

class UFO extends Objects{
	private boolean hit;

	public void hit(){//setters getters
		hit=true;
	}
	public void reset(){
		hit=false;
	}
	public boolean getHit(){
		return hit;
	}


	public UFO(double x, double y, double w, double h){//calling on super
		super (x,y,w,h);
		hit=false;
	}
}

class aliens extends Objects{
    private boolean big;//to see which image to blit

    public boolean getBig(){
    	return big;
    }

    public aliens(double x, double y, double w, double h){
		super (x,y,w,h);
		int random = (int )(Math.random() * 2 + 1);//randomly selecting if its big or not
		if (random==1){
			big=true;
		}
		else{
			big=false;
		}
    }
}

