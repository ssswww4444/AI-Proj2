/* Class TDLeaf, used as machine learning class. 
 * Methods are called by Peiyun_Wenqiang at the end of each game 
 * to update weights for all features used in the evaluation functions
 * in Peiyun_Wenqiang Agent. 
 * 
 * COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/** Class TDLeaf, used as machine learning class. 
 * Methods are called by Peiyun_Wenqiang at the end of each game 
 * to update weights for all features used in the evaluation functions
 * in Peiyun_Wenqiang Agent. */

public class TDLeaf {
	private static boolean learningSwitch = true;   // turn on or off the TD leaf learning
	private static ArrayList<Double> weights;  // weights of the features
	private static ArrayList<ArrayList<Double>> features;  // score of the features
	private static ArrayList<Double> sl;  // eval(sl, w), evaluation of best leaf
	public static final double LAMBDA = 0.7;
	public static final double ALPHA = 1.0;  // learning rate
	
	// files storing weights
	public static final String D5V_FILE_PATH = "d5v.txt"; // dimension 5, player V
	public static final String D6V_FILE_PATH = "d6v.txt"; // dimension 6, player V
	public static final String D7V_FILE_PATH = "d7v.txt"; // dimension 7, player V
	
	public static final String D5H_FILE_PATH = "d5h.txt"; // dimension 5, player H
	public static final String D6H_FILE_PATH = "d6h.txt"; // dimension 6, player H
	public static final String D7H_FILE_PATH = "d7h.txt"; // dimension 7, player H
	
	private static String filePath;
	
	/** Get the weights read from the file */
	public static ArrayList<Double> getWeights(int dimension, boolean playerH) {
		
		// Corresponding filePath
		switch(dimension) {
		case 5:
			filePath = playerH? D5H_FILE_PATH:D5V_FILE_PATH;
			break;
		case 6:
			filePath = playerH? D6H_FILE_PATH:D6V_FILE_PATH;
			break;
		case 7:
			filePath = playerH? D7H_FILE_PATH:D7V_FILE_PATH;
			break;
		}
		
		Scanner scan = null;
		try {
			scan = new Scanner(new File(filePath));  // scan from the file
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(scan.hasNextDouble()) {
			weights.add(scan.nextDouble());  // scan each weight
		}
		scan.close();
		return weights;
	}
	
	/** Update the weights and write to file at the end of the game */
	public static void updateWeights() {
		
		if(!learningSwitch) {  // not updating weight if not learning switch off
			return;
		}
		
		FileWriter writer;
		
		// avoiding outliers
		for(int i = 0; i < weights.size(); i++) {
			double diff = computeDiff(i);
			if(Math.abs(diff) > 0.2) {
				return;
			}
		}
		
		for(int i = 0; i < weights.size(); i++) {
			weights.set(i, weights.get(i) + computeDiff(i));  // update the weight
		}
		
		try {
			writer = new FileWriter(filePath);
			for(int i=0; i<weights.size(); i++) {  // write to the file
				writer.write(weights.get(i) + "\n");
			}
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/** Called at start of a new game, used to compute change of weight */
	public static void newGame() {
		// reset all arraylists
		weights = new ArrayList<Double>();
		sl = new ArrayList<Double>();
		features = new ArrayList<ArrayList<Double>>();
		filePath = D5H_FILE_PATH; // init as dimension 5, player H
	}
	
	/** Return change of weight j */
	public static double computeDiff(int j) {
		double diff = 0;  // change of weight
		for(int i = 0; i < sl.size() - 1; i++) {
			double sum = 0;  // sum of the inner series
			for(int m = 0; m < sl.size() - 1; m++) {
				double d = reward(m+1) - reward(m);  // temporal difference dm = r(m+1) - r(m)
				sum += Math.pow(LAMBDA, m-i) * d;  // lambda^(m-i) * dm
			}
			// calculate partial derivative with chain rule
			diff += Math.pow((1/Math.cosh(sl.get(i))), 2) * features.get(i).get(j) * sum;
		}
		return diff*ALPHA;
	}
	
	/** Return reward calculated */
	private static double reward(int i) {
		return Math.tanh(sl.get(i));  // r(sl,w) = tanh(eval(sl,w))
	}
	
	/** Record a state of the game */
	public static void recordState(double score, ArrayList<Double> featureSet) {
		sl.add(score);  // store best leaf value
		features.add(featureSet);  // store features of best leaf
	}
}
