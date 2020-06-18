
/*
 * The MIT License
 *
 * Copyright 2020 Prakhar Gandhi.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package snake;

/**
 *
 * @author Prakhar Gandhi
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
/**
*
* @author Tony Stark
*/
public class Snake extends JPanel implements Runnable{ 
   static Toolkit tool=Toolkit.getDefaultToolkit();
   Random r=new Random();
   List<Point> points=new ArrayList<Point>();
   Thread th;
   int dx=0,dy=0;
   static Snake pl;
   int sleeptime=70;
   Point food,bigfood;
   boolean collided=false,running=false;
   int score=0;
   String dir="right";
   boolean entered=false,visible=false;
   int anm=0;
   Graphics2D g;
   static JLabel lbl=new JLabel();
   static JLabel lb=new JLabel();
   GradientPaint gp;
   //////////
   public Snake()
   {
       setOpaque(false);
       for(int i=200;i<240;i+=10)
       {
           points.add(new Point(i,50));
       }
       lb.setFont(new Font("arial",Font.BOLD,20));
       lbl.setFont(new Font("arial",Font.BOLD,20));
       lb.setForeground(Color.BLUE);
       lbl.setForeground(Color.BLUE);
       try
       {
           for(LookAndFeelInfo info:UIManager.getInstalledLookAndFeels())
           {
               if(info.getName().equals("Nimbus"))
               {
                   UIManager.setLookAndFeel(info.getClassName());
                   break;
               }
           }
       }
       catch(Exception e)
       {
           System.out.println(e);
       }
       food=new Point(30,50);
       th=new Thread(this);
   }
//////////
   public void paint(Graphics g2)
   {
       g=(Graphics2D)g2;
       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       g.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
       super.paint(g);
       g.setFont(new Font("arial",Font.BOLD,20));
       g.setColor(Color.lightGray);
       for(int i=0;i<getWidth();i+=10)
       {
           g.drawLine(i,0,i,getHeight());
       }
       for(int j=0;j<getHeight();j+=10)
       {
           g.drawLine(0,j,getWidth(),j);
       }
       tool.sync();
       //Code to draw food on screen
       g.setColor(Color.GREEN);
       g.fill3DRect(food.x,food.y,10,10,true);
       g.setColor(Color.RED);
       if(visible==true)
       {
           g.setColor(Color.RED);
           g.fill3DRect(bigfood.x,bigfood.y,10,10,true);
           
           g.setColor(Color.BLUE);
           g.drawString("Time Left",150,30);
           g.setColor(Color.GREEN);
           g.drawRect(250,10,100,20);
           g.fill3DRect(250,10,anm-=1,20,false);
           if(anm==0)
           {
               visible=false;
               anm=100;
           }
           
       }
       if(entered==false)
       {
           g.drawString("Press Enter to enjoy",50,200);
       }
       if(collided==true)
       {
           th.suspend();
           th.stop();
           int a=JOptionPane.showConfirmDialog(this,"Do u want to continue?","u lost, please try again with more fun",JOptionPane.YES_NO_OPTION);
           if(a==JOptionPane.YES_OPTION)
           {
               collided=false;
               reset();
           }
           else
           {
               System.exit(0);
           }
       }
       else
       {
           g.setColor(Color.RED);
           //code males the snake move
           Point px=points.get(0);
           points.get(0).setLocation(px.x+=dx,px.y+=dy);
           g.setColor(Color.BLUE);
           for(int i=1;i<points.size();++i)
           {
               if(i>1)
               {
                   g.setColor(Color.RED);
               }
               Point p=points.get(i).getLocation();
               points.get(i).setLocation(px);
               g.fill3DRect(px.x,px.y,10,10,true);
               px=p;
           }
           //condition to check if the food is eaten.If eaten
           //add new food
           if(points.get(0).equals(food))
           {
               addFood();
               score+=10;
               lbl.setText("Score="+score);
               points.add(new Point(0,0));
               if(points.size()%5==0)
               {
                   sleeptime-=5;
               }
               if(points.size()%10==0)
               {
                   addBigFood();
                   visible=true;
                   anm=100;
               }
           }
           if(points.get(0).equals(bigfood))
           {
               visible=false;
               score+=(anm)*2;
               sleeptime+=5;
               lbl.setText("Score="+score);
           }
       }
       
   }
   ////////
   public void reset()
   {
       points.clear();
       for(int i=200;i<240;i+=10)
       {
           points.add(new Point(i,50));
       }
       addFood();
       dx-=10;
       dy=0;
       
       score=0;
       anm=100;
       
       visible=false;
       sleeptime=70;
       dir="left";
       
       lbl.setText(null);
       th=new Thread(this);
       th.start();
   }
   ////////
   public void addFood()
   {
       while(true)
       {
           boolean isOccupied=false;
           Point p =new Point(r.nextInt(getWidth()-60)+40,r.nextInt(getHeight()-60)+40);
           if(p.x%10==0 && p.y%10==0)
           {
               for(int i=0;i<points.size();++i)
               {
                   if(p.equals(points.get(i)))
                   {
                       isOccupied=true;
                       break;
                   }
               }
               if(isOccupied==false)
               {
                   food=p;
                   break;
               }
           }
       } 
   }
//////////
   public void addBigFood()
   {
       while(true)
       {
           boolean isOccupied=false;
           Point p =new Point(r.nextInt(getWidth()-60)+40,r.nextInt(getHeight()-60)+40);
           if(p.x%10==0 && p.y%10==0)
           {
               for(int i=0;i<points.size();++i)
               {
                   if(p.equals(points.get(i)))
                   {
                       isOccupied=true;
                       break;
                   }
               }
               if(isOccupied==false)
               {
                   bigfood=p;
                   break;
               }
           }
       } 
   }
//////////
   public void checkCollision()
   {
       for(int i=3;i<points.size();++i)
       {
           if(points.get(i).equals(points.get(0)))
           {
               collided=true;
           }
       }
       if(points.get(0).getX()+10>=getWidth())
       {
           collided =true;
       }
       if(points.get(0).getX()<=0)
       {
           collided=true;
       }
       if(points.get(0).getY()+10>=getHeight()-10)
       {
           collided =true;
       }
       if(points.get(0).getY()<=0)
       {
           collided=true;
       }
   }
 //////////
   public void run()
   {
       Thread me=Thread.currentThread();
       while(me==th)
       {
           try
           {
               repaint();
               th.sleep(sleeptime);
               checkCollision();           
           }
           catch(Exception e)
           {}
       }
       th=null;
   }
   ///////////
public static void main(String args[])
{
    JFrame jfm =new JFrame();
    jfm.setLayout(new BorderLayout());
    jfm.setSize(400,550);
    jfm.getContentPane().setBackground(Color.WHITE);
    pl=new Snake();
    
    lb.setBorder(BorderFactory.createTitledBorder(null,"Snake Game by Tony Stark",TitledBorder.CENTER,TitledBorder.BELOW_TOP,new Font("Tahoma",1,24),new Color(204,0,0)));
    
    pl.setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
    
    jfm.add(BorderLayout.NORTH,lb);
    lbl.setBorder(BorderFactory.createTitledBorder(null,"Total Score",TitledBorder.CENTER,TitledBorder.BELOW_TOP,new Font("Tahoma",1,24),new Color(204,0,0)));
    lbl.setFont(new Font("arial",Font.BOLD,20));
    
    jfm.add(BorderLayout.CENTER,pl);
    jfm.add(BorderLayout.PAGE_END,lbl);
    
    jfm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jfm.setTitle("A Basic game by Zorro");
    jfm.setVisible(true);
    jfm.setLocation((int)(tool.getScreenSize().getWidth()-jfm.getWidth())/2,(int)(tool.getScreenSize().getHeight()/2-jfm.getHeight()/2));
    
    KeyListener kl=new KeyAdapter()
    {
       public void keyPressed(KeyEvent ke)
       {
           if(ke.getKeyCode()==KeyEvent.VK_RIGHT)
           {
               if(!pl.dir.equals("left" ))
               {
                   pl.dir="right";
                   pl.dx=10;
                   pl.dy=0;
               }
               
           }
           if(ke.getKeyCode()==KeyEvent.VK_LEFT)
           {
               if(!pl.dir.equals("right" ))
               {
                   pl.dir="left";
                   pl.dx=-10;
                   pl.dy=0;
               }
               
           }
           if(ke.getKeyCode()==KeyEvent.VK_UP)
           {
               if(!pl.dir.equals("down" ))
               {
                   pl.dir="up";
                   pl.dx=0;
                   pl.dy=-10;
               }
               
           }
           if(ke.getKeyCode()==KeyEvent.VK_DOWN)
           {
               if(!pl.dir.equals("up" ))
               {
                   pl.dir="down";
                   pl.dx=0;
                   pl.dy=10;
               }
               
           }
           if(ke.getKeyCode()==KeyEvent.VK_P)
           {
               lbl.setText("Game Paused.Press 'S' to continue playing");
               pl.th.suspend();
           }
           if(ke.getKeyCode()==KeyEvent.VK_S)
           {
               pl.th.resume();
           }
           if(ke.getKeyCode()==KeyEvent.VK_X)
           {
               pl.th.stop();
               System.exit(0);
           }
           if(ke.getKeyCode()==KeyEvent.VK_ENTER)
           {
               if(pl.running==false)
               {
                   pl.entered=true;
                   pl.running=true;
                   pl.th.start();
               }
           }
           else
           {
               ke.consume();
           }
               
       }
    };
    jfm.addKeyListener(kl);
    
}   
}