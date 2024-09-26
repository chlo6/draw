import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class Main extends JFrame {
    CardLayout cardLayout;
    JPanel lvl0, cards;
    Vector<Integer> xcoord = new Vector<>(1);
    Vector<Integer> ycoord = new Vector<>(1);
    Vector<Integer> brushsizevector = new Vector<>(1);
    Vector<Color> colorvector = new Vector<>(1);
    Vector<Color> colorlist = new Vector<>(1);

    boolean keypressed;
    int brushsize = 2;
    int brushsizehover = 0;
    int colorhover = 0;
    int clearhover = 0;
    Color curcolor = Color.black;
    Color lighterGray = new Color(230, 230, 230);

    public Main() {
        // make frame
        setSize(1000, 700);
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // make cardlayout
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // each card
        lvl0 = new JPanel(new GridLayout(1, 1));
        HomePan homepanel = new HomePan();
        lvl0.add(homepanel);
        cards.add(lvl0, "level0");

        // add cards to frame
        add(cards);
        cardLayout.show(cards, "level0");

        colorlist.addElement(new Color(255, 0, 0));
        colorlist.addElement(new Color(255, 127, 0));
        colorlist.addElement(new Color(255, 255, 0));
        colorlist.addElement(new Color(127, 255, 0));
        colorlist.addElement(new Color(0, 255, 0));
        colorlist.addElement(new Color(0, 255, 255));
        colorlist.addElement(new Color(0, 127, 255));
        colorlist.addElement(new Color(0, 0, 255));
        colorlist.addElement(new Color(127, 0, 255));
        colorlist.addElement(new Color(255, 0, 255));
        colorlist.addElement(new Color(0, 0, 0));
        colorlist.addElement(new Color(255, 255, 255));

        setVisible(true);

        // Request focus for the HomePan panel
        homepanel.setFocusable(true);
        homepanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        new Main();
    }

    class HomePan extends JPanel implements MouseMotionListener, KeyListener, MouseListener {
        public HomePan() {
            addMouseMotionListener(this);
            addMouseListener(this);
            addKeyListener(this);
            setBackground(Color.white);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw
            for (int i = 0; i < xcoord.size(); i++) {
                g.setColor(colorvector.get(i));
                g.fillRect(xcoord.get(i), ycoord.get(i), brushsizevector.get(i), brushsizevector.get(i));
            }


            // side panel
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, 150, 700);

            // choose brush size
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString("brush size:", 30, 30);
            int isum = 0;
            for (int i = 1; i < 7; i++) {
                isum += i;
                if (brushsizehover == i) {
                    g.setColor(Color.white);
                    g.fillRect(70 - 4*i, 25 + 9*isum, 10 + 2*4*i, 10 + 2*4*i);
                    g.setColor(Color.black);
                }
                g.fillRect(75-4*i, 30+ 9*isum, 2*4*i, 2*4*i);
            }

            // choose color
            g.drawString("color:", 55, 290);
            for (int i = 0; i < 12; i++) {
                g.setColor(colorlist.get(i));

                int gap = 0;
                if (i % 2 != 0) gap = 30;
                if (colorhover == i + 1) {
                    g.setColor(Color.white);
                    g.fillRect(45 + gap, 295 + 30*(int)(i/2), 30, 30);
                    g.setColor(colorlist.get(i));
                }
                g.fillRect(50 + gap, 300+30*(int)(i/2), 20, 20);
            }


            // clear
            if (clearhover != 0) {
                g.setColor(Color.white);
                g.fillRect(25, 500, 100, 50);
            }
            g.setColor(lighterGray);
            g.fillRect(30, 505, 90, 40);
            g.setColor(Color.black);
            g.drawString("clear?", 52, 530);

        }

        public void keyTyped(KeyEvent e) { }

        public void keyPressed(KeyEvent e) {
            keypressed = true;
            if (e.getKeyChar() == 158) keypressed = false;
            if (brushsizehover != 0) brushsize = 5*brushsizehover;
            else if (colorhover != 0) curcolor = colorlist.get(colorhover-1);

            repaint();
        }

        public void keyReleased(KeyEvent e) { keypressed = false; }

        public void mouseMoved(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            // add coordinates
            if (keypressed) {
                xcoord.add(x);
                ycoord.add(y);
                int newbrushsize = (int)(brushsize * 1.6);
                brushsizevector.add(newbrushsize);
                colorvector.add(curcolor);
            }

            // change brush size
            brushsizehover = 0;
            int isum = 0;
            for (int i = 1; i < 7; i++) {
                isum += i;
                if (x >= (70-4*i) && x <= (80+4*i) && y >= (25+9*isum) && y <= (35+9*isum+8*i)) {
                    brushsizehover = i;
                }
            }
            //                    g.fillRect(70 - 4*i, 25 + 9*isum, 10 + 2*4*i, 10 + 2*4*i);

            // change color
            colorhover = 0;
            for (int i = 0; i < 12; i++) {
                int gap = 0; if (i % 2 != 0) gap = 30;
                if (x >= (45+gap) && x <= (75+gap) && y >= (295 + 30*(int)(i/2)) && y <= (325 + 30*(int)(i/2))) {
                    colorhover = i + 1;
                }
            }

            // clear
            clearhover = 0;
            if (x >= 25 && x <= 125 && y >= 510 && y <= 560) clearhover = 1;
            repaint();
        }

        public void mouseDragged(MouseEvent e) { }

        public void mouseClicked(MouseEvent e) { }

        public void mousePressed(MouseEvent e) {
            if (brushsizehover != 0) brushsize = 5*brushsizehover;
            else if (colorhover != 0) curcolor = colorlist.get(colorhover-1);
            else if (clearhover != 0) {
                xcoord.clear();
                ycoord.clear();
                colorvector.clear();
                brushsizevector.clear();
            }
            repaint();
        }

        public void mouseReleased(MouseEvent e) { }

        public void mouseEntered(MouseEvent e) { }

        public void mouseExited(MouseEvent e) { }
    }
}
