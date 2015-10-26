package perceptron;

import java.awt.Color;
import javax.swing.JFrame;

public class Perceptron extends JFrame {

	// Initialize the window size
	public static int windowWeight = 800;
	public static int windowHeight = 800;

	public static void main(String[] args) throws Exception {
		Color back = new Color(38, 38, 38);
		SettingPanel setting = new SettingPanel();
		Drawer drawer = new Drawer(windowWeight, windowHeight, setting.getLearn(), setting.getThreshold(),
				setting.getRound(), setting.getWeight(), setting.getFileName());
		drawer.setBackground(back);
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(windowWeight, windowHeight);
		frame.add(drawer);
	}
}




