
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class NBClassifier {
	ArrayList<String> trainDocs;
	ArrayList<Integer> trainLabels;
	ArrayList<Integer> testLabel = new ArrayList<Integer>();
	ArrayList<Integer> tempLabel = new ArrayList<Integer>();

	int numClasses=2;
	int[] classCounts; // number of docs per class
	String[] classStrings; // concatenated string for a given class
	int[] classTokenCounts; // total number of tokens per class
	HashMap<String, Double>[] conProbability;
	HashSet<String> vocab;
	static String[] myDocs;
	StringBuffer sb = new StringBuffer();
	String[] files;
	/**
	 * Build a Naive Bayes classifier using a training document set
	 * 
	 * @param trainData
	 *            the training document folder
	 */
	public NBClassifier(String trainData) {

		trainDocs = new ArrayList<String>();
		trainLabels = new ArrayList<Integer>();
		classCounts = new int[numClasses];
		classStrings = new String[numClasses];
		classTokenCounts = new int[numClasses]; 
		conProbability = new HashMap[numClasses];
		vocab = new HashSet<String>();
		File path = new File(trainData);

		files = fileProc(trainData);
		
		int i = 0;
		while (i < files.length) 
		{	
			String[] fileNames = new File(path + "/" + files[i]).list();
			int j = 0;
			while (j < fileNames.length) {
				try {
					Scanner scan = new Scanner(new File(new File(path + "/" + files[i]) + "/"
							+ fileNames[j]));
					while (scan.hasNextLine()) {
						String line = scan.nextLine();
						sb = sb.append(line);
					}
					scan.close();
					addDocs(sb, i);
					
					sb.setLength(0);
				}

				catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
				j++;
			}
			i++;
		}
		System.out.println("Total Training Documents: "+trainDocs.size());
		System.out.println("Total Labels: "+trainLabels.size());
		classDocs();
		
		int l = 0;
		while(l<numClasses){
			String[] tokens = classStrings[l].split("[\" ()_,?:;%&-]+");
			classTokenCounts[l] = tokens.length;
			for(String token:tokens)
			{
				addToVocab(token, l);
				
			}
			l++;
		}
		int k=0;
		while(k<numClasses)
		{
			Iterator<Map.Entry<String, Double>> iterator = conProbability[k].entrySet().iterator();
			int vSize = vocab.size();
			while(iterator.hasNext())
			{
				Map.Entry<String, Double> entry = iterator.next();
				String token = entry.getKey();
				Double count = entry.getValue();
				count = (count+1)/(classTokenCounts[k]+vSize);
				conProbability[k].put(token, count);
			}
			k++;
		}
	}


	private void addToVocab(String token, int l) {
		vocab.add(token);
		if(conProbability[l].containsKey(token))
		{
			double count = conProbability[l].get(token);
			conProbability[l].put(token, count+1);
		}
		else
			conProbability[l].put(token, 1.0);
	}


	private void classDocs() {
		for(int m=0;m<numClasses;m++)
		{
			classStrings[m] = "";
			conProbability[m] = new HashMap<String,Double>();
		}
		for(int n=0;n<trainLabels.size();n++)
		{
			classCounts[trainLabels.get(n)]++;

			classStrings[trainLabels.get(n)] += (trainDocs.get(n) + " ");
		}

	}


	private void addDocs(StringBuffer sb2, int i) {
		trainDocs.add(sb.toString());
		trainLabels.add(i);
	}


	private String[] fileProc(String trainData) {
		File path = new File(trainData);
		files = path.list();
		return files;
	}

//
	public int classify(String doc)throws IOException
	{
		double[] score = trackScores();
		
		String[] tokens = doc.split("[\" ()_,?:;%&-]+");
		int vSize = vocab.size();

		int i=0;
		while(i<numClasses)
		{
			for(String token: tokens)
			{
				if(!conProbability[i].containsKey(token))
					score[i] += Math.log(1.0/(classTokenCounts[i]+vSize));

				else
					score[i] += Math.log(conProbability[i].get(token));

			}
			i++;
		}
		
		int label = 0;

		for(i=0;i<score.length;i++){
			if(score[i]>score[0])
				label = i;
		}

		return label;
	}
	
	private double[] trackScores() {
		double[] score = new double[numClasses];
		for(int i=0;i<score.length;i++){
			score[i] =
					Math.log(classCounts[i]*1.0/trainDocs.size());
		}
		
		return score;
	}


	public void classifyTraining(String testDocs)throws IOException
	{

		File path = new File(testDocs);
		String[] testfiles = path.list();

		for (int i = 0; i < testfiles.length; i++) 
		{
			File read = new File(path + "/" + files[i]);
			String[] fileNames = read.list();
			
			compare(i, fileNames, path);
		}
		System.out.println("Positive Reviews: "+classCounts[0]);
		System.out.println("Negative Reviews: "+classCounts[1]);
		double total = testLabel.size()+tempLabel.size();
		double right = testLabel.size();
		double accuracy = right/total;
		System.out.println("Training Accuarcy: "+accuracy*100+"%");

	}
	private void compare(int i, String[] fileNames, File path) {
		int j = 0;
		while (j < fileNames.length) {

			Scanner scan;
			try {
				scan = new Scanner(new File(new File(path + "/" + files[i])+ "/"
						+ fileNames[j]));
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					sb = sb.append(line);
				}
				if(classify(sb.toString())==i)
				{
					testLabel.add(classify(sb.toString()));
				}
				else
				{
					tempLabel.add(classify(sb.toString()));
				}
				sb.setLength(0);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			j++;
		}
	}


	public ArrayList<Integer> classifyTest(String testDocs)throws IOException
	{

		ArrayList<Integer> testLabel = new ArrayList<Integer>();
		ArrayList<Integer> tempLabel = new ArrayList<Integer>();
		String[] testfiles = new File(testDocs).list();

		int i = 0;
		while (i < testfiles.length) 
		{
			Scanner scan = new Scanner(new File(testDocs+ "/"
					+ testfiles[i]));
			while (scan.hasNextLine()) {
				sb = sb.append(scan.nextLine());
			}
			int label = classify(sb.toString());
			System.out.println(testfiles[i]+" --> "+files[label]);
			if(label==i)
			{
				testLabel.add(i);
			}
			else
			{
				tempLabel.add(i);
			}
			sb.setLength(0);
			
			i++;
		}

		return testLabel;
	}
	public static void main(String[] args) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String trainData = "data/train";
		NBClassifier nb = new NBClassifier(trainData);
		nb.classifyTraining(trainData);
		String input="data/test";
		nb.classifyTest(input);
	}
}
