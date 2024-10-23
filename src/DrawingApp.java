import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class DrawingApp extends JFrame {
    private Color currentColor = Color.BLACK;
    private float currentThickness = 2.0f;   

    private ArrayList<Line2D.Float> lines = new ArrayList<>(); 
    private ArrayList<Color> lineColors = new ArrayList<>(); 
    private ArrayList<Float> lineThicknesses = new ArrayList<>(); 

    public DrawingApp() {
        setTitle("Drawing App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        DrawingArea drawingArea = new DrawingArea();
        add(drawingArea, BorderLayout.CENTER);


        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem colorMenuItem = new JMenuItem("Choose Color");
        colorMenuItem.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(this, "Choose a color", currentColor);
            if (selectedColor != null) {
                currentColor = selectedColor;
            }
        });

        JMenuItem thicknessMenuItem = new JMenuItem("Choose Line Thickness");
        thicknessMenuItem.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter line thickness:", currentThickness);
            if (input != null) {
                try {
                    currentThickness = Float.parseFloat(input);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input! Please enter a number.");
                }
            }
        });

        menu.add(colorMenuItem);
        menu.add(thicknessMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        setVisible(true);
    }
    private class DrawingArea extends JPanel {
        private Point startPoint, endPoint;

        public DrawingArea() {
            setBackground(Color.WHITE);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint(); 
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    endPoint = e.getPoint();
                    Line2D.Float line = new Line2D.Float(startPoint, endPoint);
                    lines.add(line);
                    lineColors.add(currentColor);
                    lineThicknesses.add(currentThickness);
                    startPoint = endPoint;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            for (int i = 0; i < lines.size(); i++) {
                g2.setColor(lineColors.get(i));
                g2.setStroke(new BasicStroke(lineThicknesses.get(i)));
                g2.draw(lines.get(i));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawingApp::new);
    }
}
