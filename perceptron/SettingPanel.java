package perceptron;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class SettingPanel extends JPanel {
	private double threshold_value;
	private double learning;
	private int round;
	private double w[];
	private String filename;

	public SettingPanel() {
		JTextField learn = new JTextField();
		JTextField threshold = new JTextField();
		JTextField round = new JTextField();
		JTextField weight = new JTextField();
		String[] fileItem = { "2Circle2.txt", "2Circle1.txt", "2class.txt", "2CloseS.txt", "2CloseS2.txt", "2CloseS3.txt", "2cring.txt",
				"2CS.txt", "2Hcircle1.txt", "2Ccircle1.txt", "2ring.txt", "5CloseS1.txt", "8OX.TXT", "感知機1.txt", "感知機2.txt", "感知機3.txt",
				"感知機4.txt" };

		JScrollPane scroll = new JScrollPane();
		JList list = new JList(fileItem);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		scroll.setViewportView(list);

		// Create an object for the message window included all setting.
		Object[] message = { "學習率", learn, "閥值", threshold, "循環次數", round, "鍵結值(w1 w2 w3...)", weight, "選擇檔案", scroll };

		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		pane.createDialog(null, "Do some setting first!").setVisible(true);

		// Initialize the values which are used in the program
		this.learning = Double.parseDouble(learn.getText());
		this.threshold_value = Double.parseDouble(threshold.getText());
		this.round = Integer.parseInt(round.getText());
		int[] selectItemIndex = list.getSelectedIndices();
		for (int index : selectItemIndex) {
			Object selected = list.getModel().getElementAt(index);
			filename = selected.toString();
		}

		// Initialize the value of weight
		String[] s = weight.getText().trim().split("\\s+");
		w = new double[s.length + 1];
		w[0] = threshold_value;
		w[1] = Double.parseDouble(s[0]);
		w[2] = Double.parseDouble(s[1]);
	}

	public double getLearn() {
		return learning;
	}

	public double getThreshold() {
		return threshold_value;
	}

	public int getRound() {
		return round;
	}

	public double[] getWeight() {
		return w;
	}

	public String getFileName() {
		return filename;
	}
}

