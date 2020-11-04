
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import AppPackage.AnimationClass;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Angel
 */
public class Laberinto extends javax.swing.JFrame {
    Label[][] labels2D = new Label[25][25];
    ArrayList<Label> labels1D = new ArrayList();
    GraphPanel graphPanel = new GraphPanel(650,650,0,0);
    Random random = new Random();
    boolean putRun = false;
    boolean putGoaler = false;
    AnimationClass animator = new AnimationClass();
    Timer timer;
    boolean finded  = false;
       
    /**
     * Creates new form Laberinto
     */
    public Laberinto() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("LABERINTO");
        makeMatrix();
        JOptionPane.showMessageDialog(null, "No puede realizar una carrera sin un corredor, debe escoger la posicion inicial del corredor usando el botón blanco del centro."
                + "\nPuede ver la matriz lógica del juego marcando la casilla para ver la misma"
                + "\nUsted puede agregar muralla utilzando el click izquierdo de su mouse y puede quitarla utilizando el click derecho"
                + "\nAdemás de agregar muralla de forma manual, el juego proporcinoa categorías de muralla aleatorea con la lista desplegable:"
                + "\n#Pocos: 50 bloques de muralla aleatorea"
                + "\n#Moderados: 100 bloques de muralla aleatorea"
                + "\n#Muchos: 150 bloques de muralla aleatorea"
                + "\n#Demasiados: 200 bloques de muralla aleatorea"
                + "\nCuando eliges alguna opción de muralla aleatorea, también puedes agregar y quitar muralla a tu gusto"
                + "\nEl botón correr disparará una carrera inmediata a la meta en la ruta más optimizada", "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void makeMatrix(){
        int sizex = 0;
        int sizey = 0;
        
        int names = 1;
        for(int row = 0; row<25; row++){
            for(int col = 0; col<25; col++){
                Label lbn = new Label(0,0,0,0,col,row);
                lbn.setBounds(sizex,sizey,25,25);
                lbn.name = names;
                names++;
                lbn.addMouseListener(new MouseListener(){
                    @Override
                    public void mouseClicked(MouseEvent me) {

                    }
                    @Override
                    public void mousePressed(MouseEvent me) {
                        if(me.getButton() == 1){
                            if(putRun && !lbn.block && !lbn.goal){
                                lbn.changeColor(Color.GREEN);
                                lbn.runner = true;
                                putRun = false;
                                putRunner.setBackground(Color.WHITE);
                            }else if(!lbn.runner && !lbn.goal && !putRun && !putGoaler){
                                lbn.changeColor(Color.RED);
                                lbn.block = true;
                                seeAround(lbn);
                            }else if(putGoaler && !lbn.block && !lbn.runner){
                                lbn.changeColor(Color.BLUE);
                                lbn.goal = true;
                                putGoaler = false;
                                putGoal.setBackground(Color.WHITE);
                            }
                        }else if(me.getButton() == 3){
                            if(putRun && !lbn.block && !lbn.goal){
                                lbn.changeColor(Color.GREEN);
                                lbn.runner = true;
                                putRun = false;
                                putRunner.setBackground(Color.WHITE);
                            }else if(!lbn.runner && !lbn.goal && !putRun && !putGoaler){
                                lbn.changeColor(Color.WHITE);
                                lbn.block = false;
                                lbn.drawBorder(0, 0, 0, 0);
                                seeAround(lbn);
                            }else if(putGoaler && !lbn.block && !lbn.runner){
                                lbn.changeColor(Color.BLUE);
                                lbn.goal = true;
                                putGoaler = false;
                                putGoal.setBackground(Color.WHITE);
                            }
                        }else if(me.getButton() == 4){
                            System.out.println("Muro: "+lbn.block+"\nCorredor: "+lbn.runner+"\nMeta: "+lbn.goal+"\n");
                            System.out.println("Posicion en X: "+lbn.posX+" Posicion en Y: "+lbn.posY);
                            System.out.println("Nombre: "+lbn.name);
                           
                        }else if(me.getButton() == 5){
                            
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                        if(putRun && !lbn.block && !lbn.goal){
                            lbn.changeColor(Color.GREEN);
                        }
                        if(putGoaler && !lbn.block && !lbn.runner){
                            lbn.changeColor(Color.BLUE);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                       if(putRun && !lbn.block && !lbn.goal){
                            lbn.changeColor(Color.WHITE);
                        }
                        if(putGoaler && !lbn.block && !lbn.runner){
                            lbn.changeColor(Color.WHITE);
                        }
                    }
                });
                labels2D[row][col] = lbn;
                labels1D.add(lbn);
                graphPanel.add(lbn);
                graphPanel.repaint();
                sizex+=25;
            }
            sizey+=25;
            sizex = 0;
        }
        matrixPanel.add(graphPanel);
        matrixPanel.repaint();
    }
    
    private void initialBlocks(int n){
        for(Label lb: labels1D){
            lb.changeColor(Color.WHITE);
            lb.block = false;
            lb.runner = false;
            lb.goal = false;
            lb.drawBorder(0, 0, 0, 0);
            lb.visited = false;
        }
        
        int[] randoms = new int[n];
        for(int i=0; i<randoms.length; i++){
            randoms[i] = 0;
        }
        for(int i=0; i<randoms.length; i++){
            randoms[i] = random.nextInt(624);
            randoms[i] = comprobateRandoms(randoms[i],randoms);
        }
       
        for(int r: randoms){
            labels1D.get(r).changeColor(Color.RED);
            labels1D.get(r).block = true;
        }
        for(int r: randoms){
            seeAround(labels1D.get(r));
        }
    }
    
    private int comprobateRandoms(int r, int[] rans){
        int val = r;
        for(int ra: rans){
            if(ra == r){
                val = random.nextInt(624);
                val = comprobateRandoms(val,rans);
            }
        }
        return val;
    }
    
    private void seeAround(Label prueba){
        int index = labels1D.indexOf(prueba);
        try{
            ArrayList<Label> arrLabels = new ArrayList();
            
            try{
                if(prueba.block==true){
                   Label up = labels1D.get(index-25);
                   if(up.block==false){
                       prueba.drawBorder(3, prueba.left, prueba.bottom, prueba.right);
                   }else if(up.block == true){
                       up.drawBorder(up.up, up.left, 0, up.right);
                   }else if(up.visited == false && up.block == true){
                       arrLabels.add(up);
                   }   
                }else{
                    try{
                        Label up = labels1D.get(index-25);
                        if(up.block==true){
                            up.drawBorder(up.up, up.left, 3, up.right);
                        }
                    }catch(Exception ex){
                        
                    }
                }
            }catch(Exception ex){
                prueba.drawBorder(3, prueba.left, prueba.bottom, prueba.right);
            }
            try{
                if(prueba.block == true){
                   Label left = labels1D.get(index-1);
                   if(left.block==false){
                       prueba.drawBorder(prueba.up, 3, prueba.bottom, prueba.right);
                   }else if(left.block == true && isOnBugRegions(index-1)==false && isOnBugRegions(index)==false){
                       left.drawBorder(left.up, left.left, left.bottom, 0);
                   }else if(left.block == true && isOnBugRegions(index-1)==true && isOnBugRegions(index)==false){
                        left.drawBorder(left.up, left.left, left.bottom, 0);
                   }else if(left.block == true && isOnBugRegions(index-1)==false && isOnBugRegions(index)==true){
                        left.drawBorder(left.up, left.left, left.bottom, 0);
                   }else if(left.block == true && isOnBugRegions(index-1)==true && isOnBugRegions(index)==true){
                        left.drawBorder(left.up, left.left, left.bottom, 3);
                        prueba.drawBorder(prueba.up, 3, prueba.bottom, prueba.right);
                   }else if(left.visited == false && left.block == true){
                       arrLabels.add(left);
                   }   
                }else{
                    try{
                        Label left = labels1D.get(index-1);
                        if(left.block==true){
                            left.drawBorder(left.up, left.left, left.bottom, 3);
                        }
                    }catch(Exception ex){
                        
                    }
                }
            }catch(Exception ex){
                prueba.drawBorder(prueba.up, 3, prueba.bottom, prueba.right);
            }
            try{
                if(prueba.block == true){
                    Label bottom = labels1D.get(index+25);
                    if(bottom.block==false){
                        prueba.drawBorder(prueba.up, prueba.left, 3, prueba.right);
                    }else if(bottom.block == true){
                        bottom.drawBorder(0, bottom.left, bottom.bottom, bottom.right);
                    }else if(bottom.visited == false && bottom.block == false){
                        arrLabels.add(bottom);
                    }
                }else{
                    try{
                        Label bottom = labels1D.get(index+25);
                        if(bottom.block==true){
                            bottom.drawBorder(3, bottom.left, bottom.bottom, bottom.right);
                        }
                    }catch(Exception ex){
                        
                    }
                }
            }catch(Exception ex){
                prueba.drawBorder(prueba.up, prueba.left, 3, prueba.right);
            }
            try{
                if(prueba.block == true){
                    Label right = labels1D.get(index+1);
                    if(right.block==false){
                        prueba.drawBorder(prueba.up, prueba.left, prueba.bottom, 3);
                    }else if(right.block == true && isOnBugRegions(index+1)==false && isOnBugRegions(index)==false){
                        right.drawBorder(right.up, 0, right.bottom, right.right);
                    }else if(right.block == true && isOnBugRegions(index+1)==true && isOnBugRegions(index)==false){
                        right.drawBorder(right.up, 0, right.bottom, right.right);
                    }else if(right.block == true && isOnBugRegions(index+1)==false && isOnBugRegions(index)==true){
                        right.drawBorder(right.up, 0, right.bottom, right.right);
                    }else if(right.block == true && isOnBugRegions(index+1)==true && isOnBugRegions(index)==true){
                        right.drawBorder(right.up, 3, right.bottom, right.right);
                        prueba.drawBorder(prueba.up, prueba.left, prueba.bottom, 3);
                    }else if(right.visited == false && right.block == true){
                        arrLabels.add(right);
                    }                    
                }else{
                    try{
                        Label right = labels1D.get(index+1);
                        if(right.block==true){
                            right.drawBorder(right.up, 3, right.bottom, right.right);
                        }
                    }catch(Exception ex){
                        
                    }
                }
            }catch(Exception ex){
                prueba.drawBorder(prueba.up, prueba.left, prueba.bottom, 3);
            }
            if(prueba.block == true){
                prueba.visited = true;   
            }
            
            for(Label lb: arrLabels){
                seeAround(lb);
            }
        }catch(Exception ex){
            
        }
    }
    private boolean isOnBugRegions(int pos){
        boolean res = false;
        int[] regionRight = new int[25];
        int[] regionLeft = new int[25];
        int r = 0;
        int l = 24;
        for(int i = 0; i<25; i++){
            regionRight[i] = r;
            regionLeft[i] = l;
            r+=25;
            l+=25;
        }
        for(int n: regionRight){
            if(pos==n){
                res = true;
            }
        }
        for(int n: regionLeft){
            if(pos==n){
                res = true;
            }
        }
        return res;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel();
        matrixPanel = new javax.swing.JPanel();
        resetButtom = new javax.swing.JButton();
        seeMatrixButtom = new javax.swing.JCheckBox();
        runButtom = new javax.swing.JButton();
        dificult = new javax.swing.JComboBox<>();
        putRunner = new javax.swing.JButton();
        putGoal = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        container.setBackground(new java.awt.Color(255, 255, 255));
        container.setPreferredSize(new java.awt.Dimension(625, 700));

        matrixPanel.setBackground(new java.awt.Color(255, 255, 255));
        matrixPanel.setPreferredSize(new java.awt.Dimension(625, 625));
        matrixPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                matrixPanelMouseMoved(evt);
            }
        });
        matrixPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                matrixPanelMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                matrixPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout matrixPanelLayout = new javax.swing.GroupLayout(matrixPanel);
        matrixPanel.setLayout(matrixPanelLayout);
        matrixPanelLayout.setHorizontalGroup(
            matrixPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        matrixPanelLayout.setVerticalGroup(
            matrixPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 625, Short.MAX_VALUE)
        );

        resetButtom.setBackground(new java.awt.Color(231, 67, 56));
        resetButtom.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        resetButtom.setForeground(new java.awt.Color(255, 255, 255));
        resetButtom.setText("Reiniciar");
        resetButtom.setPreferredSize(new java.awt.Dimension(50, 30));
        resetButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtomActionPerformed(evt);
            }
        });

        seeMatrixButtom.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        seeMatrixButtom.setText("Ver Matriz");
        seeMatrixButtom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                seeMatrixButtomItemStateChanged(evt);
            }
        });
        seeMatrixButtom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                seeMatrixButtomStateChanged(evt);
            }
        });
        seeMatrixButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seeMatrixButtomActionPerformed(evt);
            }
        });

        runButtom.setBackground(new java.awt.Color(60, 188, 216));
        runButtom.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        runButtom.setText("Correr");
        runButtom.setPreferredSize(new java.awt.Dimension(50, 30));
        runButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtomActionPerformed(evt);
            }
        });

        dificult.setBackground(new java.awt.Color(255, 255, 102));
        dificult.setForeground(new java.awt.Color(0, 0, 0));
        dificult.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MUROS", "Pocos", "Moderados", "Muchos", "Demasiados" }));
        dificult.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dificultItemStateChanged(evt);
            }
        });
        dificult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dificultActionPerformed(evt);
            }
        });

        putRunner.setBackground(new java.awt.Color(255, 255, 255));
        putRunner.setForeground(new java.awt.Color(255, 255, 255));
        putRunner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        putRunner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                putRunnerActionPerformed(evt);
            }
        });

        putGoal.setBackground(new java.awt.Color(255, 255, 255));
        putGoal.setForeground(new java.awt.Color(255, 255, 255));
        putGoal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        putGoal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                putGoalActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Colocar una meta");

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Colocar un corredor");

        javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(matrixPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(runButtom, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetButtom, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(putGoal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(putRunner, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(seeMatrixButtom, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dificult, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addComponent(matrixPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(seeMatrixButtom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dificult, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(runButtom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(resetButtom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(containerLayout.createSequentialGroup()
                        .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(putRunner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(putGoal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtomActionPerformed
       for(Label lb: labels1D){
           lb.setBackground(Color.WHITE);
           lb.block = false;
           lb.runner = false;
           lb.goal = false;
           lb.drawBorder(0, 0, 0, 0);
           lb.visited = false;
       }
       deleteWay();
       dificult.setSelectedIndex(0);
       putRun = false;
       putRunner.setBackground(Color.WHITE);
       putGoaler = false;
       putGoal.setBackground(Color.WHITE);
       seeMatrixButtom.setSelected(false);
    }//GEN-LAST:event_resetButtomActionPerformed

    private void seeMatrixButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seeMatrixButtomActionPerformed
       
    }//GEN-LAST:event_seeMatrixButtomActionPerformed

    private void seeMatrixButtomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_seeMatrixButtomStateChanged
        
    }//GEN-LAST:event_seeMatrixButtomStateChanged

    private void seeMatrixButtomItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_seeMatrixButtomItemStateChanged
        if(evt.getStateChange() == 1){
            for(Label lb: labels1D){
                lb.drawBorder(1, 1, 1, 1);
            }
            matrizWay();
        }else{
            for(Label lb: labels1D){
                lb.drawBorder(0, 0, 0, 0);
            }
            for(Label lb: labels1D){
                if(lb.block == true){
                    seeAround(lb);
                }
            }
        }
    }//GEN-LAST:event_seeMatrixButtomItemStateChanged
    private void matrizWay(){
        String[][] way = new String[25][25];
        String[][] mA = new String[25][25];
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        for(int row = 0; row<25; row++){
            for(int col = 0; col<25; col++){
                if(labels2D[row][col].block){
                    way[row][col] = " ";
                    mA[row][col] = "0";
                }else{
                    way[row][col] = "•";
                    mA[row][col] = "1";
                }
            }
        }
        for(int row = 0; row<25; row++){
            for(int col = 0; col<25; col++){
                System.out.print(way[row][col]+" ");
            }
            
            System.out.print("   |   ");
            for(int col = 0; col<25; col++){
                System.out.print(mA[row][col]+" ");
            }
            System.out.println("");
        }
    }
    private void matrixPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_matrixPanelMouseReleased
        
    }//GEN-LAST:event_matrixPanelMouseReleased

    private void matrixPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_matrixPanelMouseClicked
        
    }//GEN-LAST:event_matrixPanelMouseClicked

    private void matrixPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_matrixPanelMouseMoved
        
    }//GEN-LAST:event_matrixPanelMouseMoved

    private void runButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtomActionPerformed
        deleteWay();
        int blocks = 0;
        String posRun = "Fila: -, Columna: -";
        String posGoal = "Fila: -, Columna: -";
        int initialCol = 25, initialRow = 25, finalCol = 25, finalRow = 25;
        for(int row = 0; row<25; row++){
            for(int col = 0; col<25; col++){
                if(labels2D[row][col].runner){
                    posRun = "Fila: "+row+", Columna: "+col;;
                    initialRow = row;
                    initialCol = col;
                }
                if(labels2D[row][col].goal){
                    posGoal = "Fila: "+row+", Columna: "+col;;
                    finalRow = row;
                    finalCol = col;
                }
                if(labels2D[row][col].block){
                    blocks++;
                }
            }
        }
        if(initialCol!=25 && initialRow!=25 && finalCol!=25 && finalRow!=25){
            JOptionPane.showMessageDialog(null,"\nMuros: "+blocks
                +"\nPosisión del corredor: "+posRun
                +"\nPosisión de la meta: "+posGoal,"Información",JOptionPane.INFORMATION_MESSAGE);
            
            //Implements Dijkstra Algorithm, first remember make a matrix adyacence
            DijkastraAlgoritmo d = new DijkastraAlgoritmo(625);
            makeAdyacenceMatrix(d);
            d.dijkstra.dijkstra(labels2D[initialRow][initialCol].name);
            d.dijkstra.setDestino(labels2D[finalRow][finalCol].name);
            
            for(int i: d.dijkstra.shortPath){
                Label lb = getLabelByName(i);
                lb.setText("•");
                lb.changeColor(Color.GRAY);
            }
            
            if(d.dijkstra.shortPath.size() == 1){
                JOptionPane.showMessageDialog(null, "Es imposible llegar a la meta con esta distribución de muros","Alerta",JOptionPane.WARNING_MESSAGE);
            }
            
            labels2D[initialRow][initialCol].changeColor(Color.GREEN);
            labels2D[finalRow][finalCol].changeColor(Color.BLUE);
            labels2D[initialRow][initialCol].setText("");
            labels2D[finalRow][finalCol].setText("");
            //proveAnotherWay();
            
        }else{
            JOptionPane.showMessageDialog(null,"Error, no se ha localizado al corredor y / o meta","Error",JOptionPane.ERROR_MESSAGE);
        }
        
        
    }//GEN-LAST:event_runButtomActionPerformed
    
    private void proveAnotherWay(){
        int count = 1;
        int posInitial = 0;
        int posFinal = 0;
        for(Label lb: labels1D){
            if(lb.block == false){
                lb.name = count;
            }
            if(lb.runner == true){
                posInitial = count;
            }
            if(lb.goal == true){
                posFinal = count;
            }
            
            count++;
        }
        DijkastraAlgoritmo d = new DijkastraAlgoritmo(count);
        makeAdyacenceMatrix(d);
        d.dijkstra.dijkstra(posInitial);
        d.dijkstra.setDestino(posFinal);
        
        for(int i: d.dijkstra.shortPath){
            Label lb = getLabelByName(i);
            lb.setBackground(Color.PINK);
        }
        
    }
    
   private void makeAdyacenceMatrix(DijkastraAlgoritmo d){
       
       for(int row = 0; row<25; row++){
           for(int col = 0; col<25; col++){
                if(labels2D[row][col].block == false){
                    try{
                         if(seeUp(row,col)){
                             d.dijkstra.addEdge(labels2D[row][col].name, labels2D[row-1][col].name, 1, true);                   
                         }   
                    }catch(Exception ex){}

                    try{
                        if(seeDown(row,col)){
                            d.dijkstra.addEdge(labels2D[row][col].name, labels2D[row+1][col].name, 1, true);                  
                        } 
                    }catch(Exception ex){}

                    try{
                        if(seeRight(row,col)){
                            d.dijkstra.addEdge(labels2D[row][col].name, labels2D[row][col+1].name, 1, true);         
                        }
                    }catch(Exception ex){}

                    try{
                     if(seeLeft(row,col)){
                         d.dijkstra.addEdge(labels2D[row][col].name, labels2D[row][col-1].name, 1, true);
                     }
                    }catch(Exception ex){}
                }
            }
       }
   }
   
   private boolean  seeUp(int row, int col){
      boolean val = false;
      try{
       if(!labels2D[row-1][col].block){
           val = true;
        }   
      }catch(Exception ex){
          
      }
      return val;
   }
   
   private boolean seeDown(int row, int col){
     boolean val = false;
     try{
       if(!labels2D[row+1][col].block){
          val = true;
        }   
      }catch(Exception ex){
          
      }
     return val;
   }
   
   private boolean seeRight(int row, int col){
       boolean val = false;
       try{
       if(!labels2D[row][col+1].block){
          val = true;
        }   
      }catch(Exception ex){
          
      }
       return val;
   }
   
   private boolean seeLeft(int row, int col){
      boolean val = false;
      try{
       if(!labels2D[row][col-1].block){
          val = true;
        }   
      }catch(Exception ex){
          
      }
      return val;
   }
   
   private Label getLabelByName(int name){
       Label lab = null;
       for(Label lb: labels1D){
           if(lb.name == name){
               lab = lb;
           }
       }
       return lab;
   }
   
   
    
    private void deleteWay(){
        for(Label lb: labels1D){
            if(lb.getText().length()>0){
                lb.setText("");
                if(!lb.block && !lb.runner && !lb.goal){
                    lb.changeColor(Color.WHITE);   
                }
            }
        }
    }
    private void dificultItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dificultItemStateChanged
       
    }//GEN-LAST:event_dificultItemStateChanged

    private void dificultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dificultActionPerformed
        int dif = dificult.getSelectedIndex();
        seeMatrixButtom.setSelected(false);
        deleteWay();
        switch(dif){
            case 0:
                break;
            case 1:
                initialBlocks(50);
                break;
            case 2:
                initialBlocks(100);
                break;
            case 3:
                initialBlocks(150);
                break;
            case 4:
                initialBlocks(200);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_dificultActionPerformed

    private void putRunnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_putRunnerActionPerformed
        deleteWay();
        for(Label lb: labels1D){
            if(lb.runner == true){
                lb.runner = false;
                lb.changeColor(Color.WHITE);
            }
        }
        if(!putRun){
            putRunner.setBackground(Color.GREEN);
            putRun = true;
        }else{
            putRunner.setBackground(Color.WHITE);
            putRun = false;
        }
        putGoaler = false;
        putGoal.setBackground(Color.WHITE);
    }//GEN-LAST:event_putRunnerActionPerformed

    private void putGoalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_putGoalActionPerformed
        deleteWay();
        for(Label lb: labels1D){
            if(lb.goal == true){
                lb.goal = false;
                lb.changeColor(Color.WHITE);
            }
        }
        if(!putGoaler){
            putGoal.setBackground(Color.BLUE);
            putGoaler = true;
        }else{
            putGoal.setBackground(Color.WHITE);
            putGoaler = false;
        }
        putRun = false;
        putRunner.setBackground(Color.WHITE);
    }//GEN-LAST:event_putGoalActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("DarkMetal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Laberinto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Laberinto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Laberinto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Laberinto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Laberinto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel container;
    private javax.swing.JComboBox<String> dificult;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel matrixPanel;
    private javax.swing.JButton putGoal;
    private javax.swing.JButton putRunner;
    private javax.swing.JButton resetButtom;
    private javax.swing.JButton runButtom;
    private javax.swing.JCheckBox seeMatrixButtom;
    // End of variables declaration//GEN-END:variables
}
