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
	Color blue = new Color(89, 150, 150);
	Color bluePoint = new Color(86, 128, 203);
	Color redPoint = new Color(209, 95, 99);
	Color purple = new Color(155, 108, 194);
	Color yellow = new Color(222, 187, 62);
	private double[] weight;
	private boolean find = false;
	private int xCenter;
	private int yCenter;
	private int ratio = 30;
	private int radius = (int) (ratio * 0.1);
	private int windowWeight;
	private int windowHeight;
	private int[] result = new int[4];
	private String settingValue = null;
	private JButton sizeIncrease = new JButton();
	private JButton sizeDecrease = new JButton();

	public Drawer(int windowWeight, int windowHeight, double learn, double threshold, int round, double[] w,
			String filename) throws Exception {

		this.windowWeight = windowWeight;
		this.windowHeight = windowHeight;
		xCenter = this.windowWeight / 2;
		yCenter = this.windowHeight / 2;
		
		// The sizeIncrese and sizeDecrese are for controlling the ratio of presentation.
		sizeIncrease.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ratio += 10;
				radius = (int) (ratio * 0.1);
				repaint();
			}
		});
		sizeDecrease.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ratio > 10)
					ratio -= 10;
				radius = (int) (ratio * 0.1);
				repaint();
			}
		});
		sizeIncrease.setText("Bigger");
		sizeDecrease.setText("Smaller");
		add(sizeIncrease);
		add(sizeDecrease);
		settingValue = Double.toString(w[0]) + " " + Double.toString(w[1]) + " " + Double.toString(w[2]) + " "
				+ Integer.toString(round) + " " + Double.toString(learn) + " " + Double.toString(threshold);
		DataCalculate dataCal = new DataCalculate(learn, round, w, filename);
		weight = dataCal.getWeights();
		dataTable = dataCal.getDatas();
		find = dataCal.getFindBool();
		result = dataCal.getResult();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawAxis(g);
		drawPoint(g);
		drawLine(g);
		showResultInfo(g);
	}

	private void drawAxis(Graphics g) {
		g.setColor(Color.gray);
		g.drawLine(windowWeight / 2, 0, windowWeight / 2, windowHeight);
		g.drawLine(0, windowHeight / 2, windowWeight, windowHeight / 2);
		for (int i = xCenter, j = xCenter; i <= windowWeight && j >= 0; i += ratio, j -= ratio) {
			g.drawLine(windowWeight / 2 - (int) (ratio * 0.2), i, windowWeight / 2 + (int) (ratio * 0.2), i);
			g.drawLine(windowWeight / 2 - (int) (ratio * 0.2), j, windowWeight / 2 + (int) (ratio * 0.2), j);
		}
		for (int i = yCenter, j = yCenter; i <= windowHeight && j >= 0; i += ratio, j -= ratio) {
			g.drawLine(i, windowHeight / 2 - (int) (ratio * 0.2), i, windowHeight / 2 + (int) (ratio * 0.2));
			g.drawLine(j, windowHeight / 2 - (int) (ratio * 0.2), j, windowHeight / 2 + (int) (ratio * 0.2));
		}
	}

	private void drawPoint(Graphics g) {
		for (int i = 0; i < dataTable.size(); i++) {
			double[] datatmp = mapping(dataTable.get(i).getPonit());
			if (dataTable.get(i).getD() == -1) {
				g.setColor(redPoint);
				g.fillOval((int) datatmp[0], (int) datatmp[1], radius, radius);
			} else {
				g.setColor(bluePoint);
				g.fillOval((int) datatmp[0], (int) datatmp[1], radius, radius);
			}
		}
	}

	private void drawLine(Graphics g) {
		g.setColor(yellow);
		double xOff = (windowWeight / 2);
		double y1 = (weight[0] * ratio - (weight[1] * xOff)) / weight[2];
		double y2 = (weight[0] * ratio - (weight[1] * (-1) * xOff)) / weight[2];
		((Graphics2D) g).setStroke(new BasicStroke(4f));
		((Graphics2D) g).draw(new Line2D.Double(windowWeight, yCenter - y1, 0, yCenter - y2));
	}

	private void showResultInfo(Graphics g) {
		g.setColor(blue);
		String[] tmp = settingValue.split(" ");
		((Graphics2D) g).setStroke(new BasicStroke(4f));
		g.drawRect( 30, 10, 185, 245);
		g.drawString("鏈結值:  [" + tmp[1] + ", " + tmp[2] + "] ", 50, 28);
		g.drawString("學習率:  " + tmp[4], 50, 53);
		g.drawString("訓練次數:  " + tmp[3], 50, 78);
		g.drawString("閥值:  " + tmp[0], 50, 103);
		g.drawString(" ", 50, 123);
		g.drawString("資料數:  " + Integer.toString(result[1] + result[3]), 50, 140);
		g.drawString("訓練數:  " + Integer.toString(result[1]), 50, 165);
		g.drawString("訓練正確率: " + Float.toString(((float) result[0] / (float) result[1]) * 100) + " %", 50, 190);
		g.drawString("測試數:  " + Integer.toString(result[3]), 50, 215);
		g.drawString("測試正確率: " + Float.toString(((float) result[2] / (float) result[3]) * 100) + " %", 50, 240);
		if(!find) {
			g.setColor(purple);
			g.drawRect( windowWeight / 2 + 40, 50, 330, 40);
			g.drawString("Can not Find the suitable sulotion in round time.", windowWeight / 2 + 50, 75);
		}
	}

	private double[] mapping(double[] points) {
		double[] tmp = new double[2];
		tmp[0] = xCenter + points[0] * ratio;
		tmp[1] = yCenter - points[1] * ratio;
		return tmp;
	}
}
