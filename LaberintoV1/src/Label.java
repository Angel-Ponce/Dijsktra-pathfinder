
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Angel
 */
public class Label extends JLabel {
    public boolean block = false;
    public boolean runner = false;
    public boolean goal = false;
    public boolean way = false;
    public int up = 0;
    public int left = 0;
    public int bottom = 0;
    public int right = 0;
    public boolean visited = false;
    public boolean stateDijstra = false;
    public int name;
    public int posX;
    public int posY;
    
    
    public Label(int up, int left, int bottom, int right, int posX, int posY){
        this.up = up;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.setFont(new Font("Impact",Font.BOLD,15));
        this.setHorizontalAlignment(CENTER);
        this.posX = posX;
        this.posY = posY;
    }
    
    public void changeColor(Color color){
        this.setBackground(color);
        this.setOpaque(true);
    }
    
    public void drawBorder(int up, int left, int bottom, int right){
        this.setBorder(BorderFactory.createMatteBorder(up, left, bottom, right, Color.BLACK));
        this.up = up;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
}
