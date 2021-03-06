package perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class DataCalculate {
	private static ArrayList<Data> dataTable = new ArrayList<Data>();
	private static ArrayList<double[]> dataTmp = new ArrayList<double[]>();
	private static ArrayList<Data> dataTEST = new ArrayList<Data>();
	private static ArrayList<Data> dataEXP = new ArrayList<Data>();
	private static int demention = 3;
	private static int round = 0;
	private static boolean find = false;
	private static double threshold_value;
	private static double learn;
	private static double[] w = new double[demention];
	private static double[] x = new double[demention];
	private int[] result = new int[4];
	public String filename;

	public DataCalculate(double learn, int round, double[] weight, String file) throws Exception {
		this.learn = learn;
		this.round = round;
		filename = file;
		w = weight;
		threshold_value = w[0];
		readData();
		setTable();
		calculate();
		resultCal();
	}

	private void readData() throws Exception {
		String line = null;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
		while ((line = bufferedReader.readLine()) != null) {
			String[] item = null;
			item = line.trim().split("\\s+");
			double[] data = new double[demention];
			for (int counter = 0; counter < demention; counter++)
				data[counter] = (float) doubleValue(item[counter]);
			dataTmp.add(data);
		}
		bufferedReader.close();
	}

	// Set the table of the data.
	public void setTable() {
		for (int i = 0; i < dataTmp.size(); i++) {
			double[] point = new double[demention - 1];
			for (int j = 0; j < (demention - 1); j++) {
				point[j] = dataTmp.get(i)[j];
			}
			Data data = new Data();
			data.setData(dataTmp.get(i)[demention - 1], point);
			dataTable.add(data);
		}
		changeLabel(dataTable);
		seperateData();
	}

	// Separate the data into experiment and test data.
	public void seperateData() {
		Random rand = new Random();
		ArrayList<Data> tmp = new ArrayList<Data>();
		tmp = dataTable;
		float bound = Math.round(tmp.size() * 2 / 3);
		while (bound != tmp.size()) {
			int x = rand.nextInt(tmp.size());
			dataEXP.add(tmp.get(x));
			tmp.remove(x);
			if(tmp.size() == bound)
				break;
		}
		dataTable = dataEXP;
		dataTEST = tmp;
	}

	// Get each data in table and asign by the index.
	public static void initialize(int index, ArrayList<Data> list) {
		x[0] = threshold_value;
		x[1] = list.get(index).getPonit()[0];
		x[2] = list.get(index).getPonit()[1];
	}

	// Calculate the weight test data
	public static void calculate() {
		double sum = 0;
		int sucessCount = 1;
		for (int count = 0; count < round; count++) {
			for (int i = 0; i < dataTable.size(); i++) {
				if (sucessCount == dataTable.size())
					break;
				initialize(i, dataTable);
				sum = 0;
				for (int j = 0; j < demention; j++) {
					sum += w[j] * x[j];
				}
				if (Math.signum(sum) != dataTable.get(i).getD()) {
					if (i == dataTable.size() - 1)
						i = 0;
					sucessCount = 1;
					for (int k = 0; k < demention; k++) {
						if (Math.signum(sum) < 0)
							w[k] += learn * x[k];
						else
							w[k] -= learn * x[k];
					}
				} else
					sucessCount += 1;
			}
			if (sucessCount == dataTable.size()) {
				find = true;
				break;
			}
		}
		if (sucessCount < dataTable.size()) 
			find = false;
	}

	private void resultCal() {
		int sum = 0;
		int testCount = 0;
		int expCount = 0;
		for (int i = 0; i < dataTEST.size(); i++) {
			initialize(i, dataTEST);
			sum = 0;
			for (int j = 0; j < demention; j++) {
				sum += w[j] * x[j];
			}
			if (Math.signum(sum) == dataTEST.get(i).getD()) {
				testCount++;
			}
		}
		for (int i = 0; i < dataEXP.size(); i++) {
			initialize(i, dataEXP);
			sum = 0;
			for (int j = 0; j < demention; j++) {
				sum += w[j] * x[j];
			}
			if (Math.signum(sum) == dataEXP.get(i).getD()) {
				expCount++;
			}
		}
		result[0] = testCount;
		result[1] = dataTEST.size();
		result[2] = expCount;
		result[3] = dataEXP.size();
	}

	public static void changeLabel(ArrayList<Data> dataTable) {
		double label = dataTable.get(0).getD();
		for (int i = 0; i < dataTable.size(); i++) {
			if (dataTable.get(i).getD() != label)
				dataTable.get(i).setD(-1);
			else
				dataTable.get(i).setD(1);
		}
	}

	public ArrayList<Data> getDatas() {
		return dataTable;
	}

	public double[] getWeights() {
		return w;
	}

	public double doubleValue(String s) {
		return Double.parseDouble(s);
	}

	public boolean getFindBool() {
		return find;
	}

	public int[] getResult() {
		return result;
	}
}
