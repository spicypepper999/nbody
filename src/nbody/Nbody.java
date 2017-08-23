package nbody;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Nbody extends JPanel implements ActionListener {

    // Index of the body that camera is centered on
    int cameraIndex = 0;
    double scale = 1.0;
    int frame = 0;
    BSim sim;
    Timer timer = new Timer(16, this);
    BPoint camera = new BPoint(0, 0);
    //x, y, mass, name, dir, speed, trail length   
    static Body[] inputBodies = new Body[]{
        new Body(200.0, 500.0, 100.0, "planet1", 0.0, 0.2, 500),
        new Body(200.0, 200.0, 10.0, "moon1", 0.0, 2.3, 500),
        new Body(200.0, 150.0, 1.0, "meteor1", 0.0, 0.8, 500)
    };
    static Dimension size = new Dimension(1000, 1000);

    //creates BSim when called
    public Nbody(Body[] inputBodies, double inputGravConstant) {
        sim = new BSim(inputBodies, inputGravConstant);
        timer.start();
    }

    //create jpanel window and initialize NBody
    public static void createSim(int inputX, int inputY, Body[] inputBodies, double inputGravConstant) {
        JFrame window = new JFrame("Nbody");
        Nbody game = new Nbody(inputBodies, inputGravConstant);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setSize(inputX, inputY);
        window.getContentPane().add(game);
    }

    //this is called every frame
    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == timer) {
            sim.nextTick();
            repaint();
            frame++;
            System.out.println(frame);
        }
    }

    //this draws the frame after im done calculating the positions and vectors, it draws an oval for each body as well as the name 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int a = 0; a < sim.getBodies().length; a++) {
            camera = sim.getBodies()[cameraIndex].getBPoint();
            Body body = sim.getBodies()[a];
            String name = body.getName();
            int mass = (int) body.getMass();
            int diameter = (int) Math.sqrt(body.getMass());
            int camX = (int) camera.getX() - (int)(size.getWidth() / 2);
            int camY = (int) camera.getY() - (int)(size.getHeight() / 2);
            int x = (int) body.getX() - camX;
            int y = (int) body.getY() - camY;
            int trailLength = (int) body.getTrail().length;
            g.drawOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
            g.drawString(name, x, y - diameter - 5);
            for (int b = 0; b < trailLength - 1; b++) {
                if (body.getTrail()[b] != null) {
                    int trailX = (int) body.getTrail()[b].getX() - camX;
                    int trailY = (int) body.getTrail()[b].getY() - camY;
                    int trailNextX = (int) body.getTrail()[b + 1].getX() - camX;
                    int trailNextY = (int) body.getTrail()[b + 1].getY() - camY;
                    g.drawLine(trailX, trailY, trailNextX, trailNextY);
                }
            }
        }
    }

    //this is called first when i run this program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createSim((int)size.getWidth(), (int)size.getHeight(), inputBodies, -10.0);
            }
        });
    }
}
