
import java.awt.Color;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Angel
 */
public class GraphPanel extends JPanel{
    int sizex,sizey,posi,posf;
    public GraphPanel(int x, int y, int xi, int yf){
        this.setLayout(null);
        this.setBounds(0,0,x,y);
        this.setBackground(Color.WHITE);
        this.posi = xi;
        this.posf = yf;
    }
}
