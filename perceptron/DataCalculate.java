package perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class DataCalculate {
	public static ArrayList<Data> dataTable = new ArrayList<Data>();
	public static ArrayList<double[]> dataTmp = new ArrayList<double[]>();
	public static ArrayList<Data> dataTEST = new ArrayList<Data>();
	public static ArrayList<Data> dataEXP = new ArrayList<Data>();
	public static int demention = 3;
	public static int round = 0;
	public static boolean find = false;
	public static double threshold_value;
	public static double learn;
	public static double[] w = new double[demention];
	public static double[] x = new double[demention];
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
	}

	private void readData()throws Exception
	{    
		String line = null;
	    BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
	    while((line = bufferedReader.readLine()) != null) {
	    	
	    	String[] item = null;
			item = line.trim().split("\\s+"); 
			double[] data = new double[demention];
			for(int counter = 0; counter < demention; counter++)
				data[counter] = (float)doubleValue(item[counter]);	
			dataTmp.add(data);
		}
	    bufferedReader.close();
	}

	public static void setTable() {
		for(int i = 0; i < dataTmp.size(); i++) {
			double[] point = new double[demention - 1];
			for(int j = 0; j < (demention - 1); j++) {
				point[j] = dataTmp.get(i)[j];
			}
			Data data = new Data();
			data.setData(dataTmp.get(i)[demention - 1], point);
			dataTable.add(data);
		}
		changeLabel(dataTable);
		seperateData();
	}
	
	public static void seperateData() {
		Random rand = new Random();
		ArrayList<Data> tmp = new ArrayList<Data>();
		tmp = dataTable;
		for(int index = 0; index < (tmp.size() * 2 / 3); index ++) {
			int x = rand.nextInt(tmp.size());
			dataEXP.add(tmp.get(index));
			tmp.remove(index);
		}
		dataTable = dataEXP;
		dataTEST = tmp;
	}
	
	// Get each data in table and asign by the index. 
    public static void initialize(int index) {
		x[0] = threshold_value;
		x[1] = dataTable.get(index).getPonit()[0];
		x[2] = dataTable.get(index).getPonit()[1];
	}

	public static void calculate() {
		double sum = 0;
		int sucessCount = 1;
		for(int count = 0; count < round; count++) {
			for(int i = 0; i < dataTable.size(); i++) {
				if(sucessCount == dataTable.size())
					break;
				initialize(i);
				sum = 0;
				for(int j = 0; j < demention; j++) {
					sum += w[j] * x[j];	
				}
				if(Math.signum(sum) != dataTable.get(i).getD()) {
					if(i == dataTable.size() - 1)
						i = 0;
					sucessCount = 1;
					for(int k = 0; k < demention; k++) {
						if(Math.signum(sum) < 0) 
							w[k] += learn * x[k];
						else 
							w[k] -= learn * x[k];
					}
				}
				else
					sucessCount += 1;
				//System.out.printf("\n%d  [ %.2f  %.2f  %.2f] : %.2f * [%.2f  %.2f  %.2f]-->[%f : %.2f]",sucessCount, w[0], w[1], w[2], learn, x[0], x[1], x[2], Math.signum(sum),dataTable.get(i).getD());
			}//end of for loop for running data in data table.
			if(sucessCount == dataTable.size()) {
				find = true;
				break;
			}	
		}// End of for loop for round time.
		if(sucessCount < dataTable.size()) {
			find = false;
			System.out.printf("Can not find the solution in round times.");
		}			
	}
	// Compare the values for separating the points.
	public static boolean compareSign(double a, double b) {
		if(a * b < 0)
			return false;
		else 
			return true;
	}
	
	public static void changeLabel(ArrayList<Data> dataTable) {
		double label = dataTable.get(0).getD();
		for(int i = 0; i < dataTable.size(); i++) {
			if(dataTable.get(i).getD() != label)
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
	
	public boolean getFindBool(){
		return find;
	}
	
	public ArrayList<Data> getTestData() {
		return dataTEST;
	}
	
	public ArrayList<Data> getExpData() {
		return dataEXP;
	}
}
