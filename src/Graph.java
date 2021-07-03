//This software is  made by: Denes Kutlan George
//The software is open src, that means - you can modify / edit the code freely as long as you don't publish it publicly on the internet!
//Copyright © 2020 - 2021 Denes K. G., All rights served 
//Contacts: Gmail: h43to.code@gmail.com; Discord: H34TO#0668

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Graph extends JPanel {

	static Scanner user = new Scanner(System.in);
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(50, 100, 200, 180);
    private Color pointColor = new Color(0, 0, 0, 200);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 5;
    private int numberYDivisions = 10;
    private List<Double> scores;
    static String path;
    static String pathCache;
    static String ecScore;

    public Graph(List<Double> scores) {
        this.scores = scores;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.white);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < scores.size(); i++) {
            if (scores.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((scores.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (Double score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Double score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setScores(List<Double> scores) {
        this.scores = scores;
        invalidate();
        this.repaint();
    }

    public List<Double> getScores() {
        return scores;
    }

    public static void startup() {
    	
    	//Startup
    	System.out.println("Path to data file:");
    	path = user.nextLine();
    	pathCache = path;
        List<Double> scores = new ArrayList<>();
        
        //Startup scan
        for (int i = 0; i < 1; i++) {
           	  try {
           		Scanner scanner = new Scanner(new File(path));
         			while (scanner.hasNextLine()) {
           				ecScore = scanner.nextLine();
               			if(ecScore.indexOf(",") != -1 || ecScore.indexOf(" ") != -1)
              			 {
              				ecScore = ecScore.replaceAll(",",".");
              				ecScore = ecScore.replaceAll(" ","");
              			 }
               			if(ecScore.isEmpty() || ecScore.matches("[a-zA-Z]+")) {
               				ecScore = "0";
               			}
           				scores.add((double) Double.parseDouble(ecScore));
         			}
         			scanner.close();
         		} catch (FileNotFoundException fnf) {
         			System.out.println("The" + path + "file can't be found! Please check the file path!");
         			fnf.printStackTrace();
            		System.exit(1);
         		}
         	}
        
        //Define mainPanel
        Graph mainPanel = new Graph(scores);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setLayout(null); 
        
        //Define & Add textField
        JTextField pathField = new JTextField(20);
        pathField.setBounds(800,200,150,25);
        pathField.setText(path);
        frame.add(pathField);

        //Define & Add Register button + Check if clicked
        final JButton b = new JButton("Update");  
        b.setBounds(800,250,95,30);
        b.setBackground(null);
        frame.add(b);   
        b.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
                path = pathField.getText();
                try {
             		Scanner scanner = new Scanner(new File(path));
             		scanner.close();
           			} catch (FileNotFoundException fnf) { 
               			path = pathCache;
               			System.out.println("The '" + path + "' file can't be found! Please check the file path!");
           			}	
          }
        });
         
        //Update graph / scan file again
        int delay = 100; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
 	
            	mainPanel.updateUI();
                scores.clear();
                for (int i = 0; i < 1; i++) {
                 	  try {
                 		Scanner scanner = new Scanner(new File(path));
               			while (scanner.hasNextLine()) {
               				ecScore = scanner.nextLine();
               			if(ecScore.indexOf(",") != -1 || ecScore.indexOf(" ") != -1)
               			 {
               				ecScore = ecScore.replaceAll(",",".");
               				ecScore = ecScore.replaceAll(" ","");
               			 }
               			if(ecScore.isEmpty() || ecScore.matches("[a-zA-Z]+")) {
               				ecScore = "0";
               			}
               				scores.add((double) Double.parseDouble(ecScore));
               				pathCache = path;
               			}
               			scanner.close();
               		} catch (FileNotFoundException fnf) {
               			System.out.println("The '" + path + "' file can't be found! Please check the file path!");
               			fnf.printStackTrace(); 
                		System.exit(1);
               		}
               	}
            }
        };
      new Timer(delay, taskPerformer).start();
}
    
    //Main class
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            startup();
         }
      });
   }
}
