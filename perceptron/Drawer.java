package perceptron;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Drawer extends JPanel {
	private ArrayList<Data> dataTable = new ArrayList<Data>();
	private ArrayList<Data> dataTest = new ArrayList<Data>();;
	private ArrayList<Data> dataExp = new ArrayList<Data>();;
	private double[] weight;
	private boolean find = false;
	private int xCenter;
	private int yCenter;
	private int ratio = 30;
	private int radius = (int)(ratio * 0.1);
	private int windowWeight;
	private int windowHeight;
	private JButton sizeIncrease = new JButton();
	private JButton sizeDecrease = new JButton();
	public Drawer(int windowWeight, int windowHeight, 
			double learn, double threshold, int round, double[] w, String filename) throws Exception {
		
		this.windowWeight = windowWeight;
		this.windowHeight = windowHeight;
		xCenter = this.windowWeight / 2;
		yCenter = this.windowHeight / 2;

		// The sizeIncrese and sizeDecrese are for controlling the ratio of presentation
		sizeIncrease.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ratio += 10;
				radius = (int)(ratio * 0.1);
				repaint();
			}
		});
		sizeDecrease.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ratio > 10)
					ratio -= 10;
				radius = (int)(ratio * 0.1);
				repaint();
				
			}
		});
		sizeIncrease.setText("Bigger");
		sizeDecrease.setText("Smaller");
		add(sizeIncrease);
		add(sizeDecrease);
		
		DataCalculate dataCal = new DataCalculate(learn, round, w, filename);
		weight = dataCal.getWeights();
		dataTable = dataCal.getDatas();
		find = dataCal.getFindBool();
		dataTest = dataCal.getTestData();
		dataExp = dataCal.getExpData();
		System.out.printf("\n%.2f  %.2f  %.2f", weight[0], weight[1], weight[2]);
	}

	@Override
	protected void paintComponent(Graphics g) {	
		super.paintComponent(g);	
		drawAxis(g);
		drawPoint(g);
		if(find)
			drawLine(g);
		showResultInfo(g);
	}

	private void drawAxis(Graphics g) {
		g.drawLine(windowWeight / 2, 0, windowWeight / 2, windowHeight);
		g.drawLine(0, windowHeight / 2, windowWeight, windowHeight / 2);
		for(int i = xCenter, j = xCenter; i <= windowWeight && j >= 0; i += ratio, j -= ratio) {
			g.drawLine(windowWeight / 2 - (int)(ratio * 0.2), i, windowWeight / 2 + (int)(ratio * 0.2), i);
			g.drawLine(windowWeight / 2 - (int)(ratio * 0.2), j, windowWeight / 2 + (int)(ratio * 0.2), j);
		}	
		for(int i = yCenter, j = yCenter; i <= windowHeight && j >= 0; i += ratio, j -= ratio) {
			g.drawLine(i, windowHeight / 2 - (int)(ratio * 0.2), i, windowHeight / 2 + (int)(ratio * 0.2));
			g.drawLine(j, windowHeight / 2 - (int)(ratio * 0.2), j, windowHeight / 2 + (int)(ratio * 0.2));
		}
	}
	
	private void drawPoint(Graphics g) {
		for(int i = 0; i < dataTable.size(); i++) {
			double[] datatmp = mapping(dataTable.get(i).getPonit());
			if(dataTable.get(i).getD() == -1) {
				g.setColor(Color.RED);
				g.fillOval((int)datatmp[0], (int)datatmp[1], radius, radius);
			}
			else {
				g.setColor(Color.BLUE);
				g.fillOval((int)datatmp[0], (int)datatmp[1], radius, radius);	
			}
		}
	}

	private void drawLine(Graphics g) {
		double xOff = (windowWeight / 2);
		double y1 = (weight[0] * ratio - (weight[1] * xOff))/ weight[2];
		double y2 = (weight[0] * ratio - (weight[1] * (-1) * xOff))/ weight[2];
		g.setColor(Color.BLUE);
		((Graphics2D)g).setStroke(new BasicStroke(4f));
		((Graphics2D)g).draw(new Line2D.Double(windowWeight, yCenter - y1, 0 , yCenter - y2));	
	}
	
	private void showResultInfo(Graphics g) {
		g.drawString("TEST", 100, 100);
	}
	
	private double[] mapping(double[] points) {
		double[] tmp = new double[2]; 
		tmp[0] = xCenter + points[0] * ratio;
		tmp[1] = yCenter - points[1] * ratio;
		return tmp;
	}
}