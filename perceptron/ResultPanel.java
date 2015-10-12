package perceptron;

import java.awt.Graphics;

import javax.swing.JPanel;

public class ResultPanel extends JPanel{
	@Override
	protected void paintComponent(Graphics g) {	
		super.paintComponent(g);	
		g.drawString("TTTTTTTTTTEST", 100, 100);
	}
}
