package project.algorithm.HS;

import project.data.*;
import project.start.*;

import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class Harmony {

	private double[] harmony = null;
	private double sensitivity;
	private double specificy;
	private double fitness;
	
	// tp: true positive;
	private int tp;
	// fn: false negative;
	private int fn;
	// tn: true negative;
	private int tn;
	// fp: false positive;
	private int fp;
	
	private int total;
	
	private int[] chromosome = null;
	
	public Harmony(int length) {
		this.harmony = new double[length];
		this.chromosome = new int[length];
		initialise();
	}
	
	/*
	 * In version 1.0, the harmony value is real and randomly selected from the antigen range.
	 */
	/*public void build(int[] chromosome, ArrayList<Antigen> antigen_info) {
		RandomGenerator rnd = new RandomGenerator();
		this.chromosome = new int[chromosome.length];
		for (int i = 0; i < this.harmony.length; i++) {
			this.chromosome[i] = chromosome[i];
			if (chromosome[i] == 1) {
				double max = antigen_info.get(i).getMaxValue();
				double min = antigen_info.get(i).getMinValue();
				this.harmony[i] = rnd.getRandomValue(min, max);
			}
		}
	}*/
	
	/*
	 * In version 1.1, the harmony value is real and randomly selected from the cutoff windows.
	 */
	public void build(int[] chromosome, ArrayList<Antigen> antigen_info, int random_seed) {
		RandomGenerator rnd = new RandomGenerator(random_seed);
		this.chromosome = new int[chromosome.length];
		for (int i = 0; i < this.harmony.length; i++) {
			this.chromosome[i] = chromosome[i];
			if (chromosome[i] == 1) {
				int index = rnd.getRandomIndex(antigen_info.get(i).getCutOffSize());
				this.harmony[i] = antigen_info.get(i).getCutOffValue(index);
			}
		}
	}
	
	public void calculateVar(ArrayList<Patient> patient_info, int threshold) {
		initialise();
		for (int i = 0; i < patient_info.size(); i++) {
			Patient pa = new Patient();
			pa = patient_info.get(i);
			boolean count = false;
			int check = 0;
			for (int j = 0; j < this.harmony.length; j++) {
				if (chromosome[j] == 1) {
					if (pa.getAntigenValue(j) > this.harmony[j]) {
						check++;
						if (check == threshold) {
							count = true;
							if (pa.isCancer()) {
								this.tp++;
							} else {
								this.fp++;
							}
							break;
						}
					}
				}
			}
			
			if (!count) {
				if (!pa.isCancer()) {
					this.tn++;
				} else {
					this.fn++;
				}
			}
		}
		
		if (this.tp + this.fn == 0) {
			this.sensitivity = 0.0;
		} else {
			this.sensitivity = this.tp*1.0/(this.tp+this.fn);	
		}
		
		if (this.tn + this.fp == 0) {
			this.specificy = 0.0;
		} else {
			this.specificy = this.tn*1.0/(this.tn + this.fp);
		}
	}
	
	public void calculateFitness(ArrayList<Harmony> harmony_list) {
		
	}
	
	public void calculateFitness(double sensi_goal, double speci_goal, int iteration, int total_iterations) {
		
		int scale = 50;
		int value = iteration/scale;
		double spec_weight = value*scale*1.0/total_iterations;
		double sens_weight = 1 - spec_weight;
		
		// double spec_weight = 1;
		// double sens_weight = 1;
		
		/*
		 * Version 1.0: Fitness = (sensi_goal - sensi) + (speci_goal - speci) + Math.abs(sensi - speci)
		 * 
		 * double goal_percen = 1;
		double diff_percen = 0;
		double sensi_goal_difference = 0;
		double speci_goal_difference = 0;
		
		if (this.sensitivity < sensi_goal) {
			sensi_goal_difference = sensi_goal - this.sensitivity;
		}
		
		if (this.specificy < speci_goal) {
			speci_goal_difference = speci_goal - this.specificy;
		}
		
		this.obj = goal_percen*(sensi_goal_difference + speci_goal_difference) + diff_percen*(Math.abs(this.sensitivity - this.specificy));
		 * 
		 * 
		 * 
		 * 
		 */
		
		/*
		 * version 1.1: simplied from version 1.0;
		 * record the minimum one
		 */
		// this.fitness = Math.min(this.sensitivity, this.specificy);
		
		/*
		 * version 1.2: record the maximum one.
		 */
		
		// this.fitness = Math.max(this.sensitivity, this.specificy);
		
		/*
		 * version 1.3: calculate the total value of obj?
		 */
		
		/*
		 * version 1.4: Generate the pareto front of each data set based on the chebyshev method.
		 */
		// this.fitness = Math.min(this.sensitivity-sensi_goal, this.specificy-speci_goal);
		this.fitness = Math.max((sensi_goal - this.sensitivity)*sens_weight, (speci_goal - this.specificy)*spec_weight);
		this.total = this.fn+this.fp+this.tn+this.tp;
	}
	
	public void initialise() {
		this.tp = 0;
		this.fn = 0;
		this.tn = 0;
		this.fp = 0;
		this.total = 0;
	}
	
	public void PrintOnScreen() {
		System.out.println("The harmony value is " + Arrays.toString(this.harmony));
		System.out.println("The sensitivity is " + this.sensitivity + " and the specificity is " + this.specificy + " and the fitness value is " + this.fitness);
		System.out.println("The true positivie is: "+this.tp + "The true negative is: "+this.tn + "The false positive is: "+this.fp + "The false negative is: "+this.fn);
		System.out.println("The total number of patient is " + this.total);
	}
	
	public void setHarmony(Harmony har, int[] chromosome) {
		setHarmonyValue(har.getHarmonyValue());
		setSensitivity(har.getSensitivity());
		setSpecificy(har.getSpecificy());
		setFitness(har.getFitness());
		setTruePositive(har.getTruePositive());
		setFalseNegative(har.getFalseNegative());
		setTrueNegative(har.getTrueNegative());
		setFalsePositive(har.getFalsePositive());
		setChromosome(chromosome);
	}
	
	public void setHarmonyIndexValue(int index, double value) {
		this.harmony[index] = value;
	}
	
	public double getHarmonyIndexValue(int index) {
		return this.harmony[index];
	}
	
	public void setChromosomeIndexValue(int index, int value) {
		this.chromosome[index] = value;
	}
	
	public void setChromosome(int[] chromosome) {
		for (int i = 0; i < chromosome.length; i++) {
			this.chromosome[i] = chromosome[i];
		}
	}
	
	public double[] getHarmonyValue() {
		double[] cloned = new double[this.harmony.length];
		for (int i = 0; i < cloned.length; i++) {
			cloned[i] = this.harmony[i];
		}
		return cloned;
	}
	
	public void setHarmonyValue(double[] value) {
		for (int i = 0; i < value.length; i++) {
			this.harmony[i] = value[i];
		}
	}
	
	public double getSensitivity() {
		return this.sensitivity;
	}

	public void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}

	public double getSpecificy() {
		return this.specificy;
	}

	public void setSpecificy(double specificy) {
		this.specificy = specificy;
	}

	public double getFitness() {
		return this.fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public void setTruePositive(int tp) {
		this.tp = tp;
	}
	
	public int getTruePositive() {
		return this.tp;
	}
	
	public void setTrueNegative(int tn) {
		this.tn = tn;
	}
	
	public int getTrueNegative() {
		return this.tn;
	}
	
	public void setFalsePositive(int fp) {
		this.fp = fp;
	}
	
	public int getFalsePositive() {
		return this.fp;
	}
	
	public void setFalseNegative(int fn) {
		this.fn = fn;
	}
	
	public int getFalseNegative() {
		return this.fn;
	}
	
	public void clear() {
		this.harmony = new double[this.harmony.length];
	}
	
	public static Comparator<Harmony> sortByFITNESS = new Comparator<Harmony>() {

		public int compare(Harmony h1, Harmony h2) {

		  double fit1 = h1.getFitness();
		  double fit2 = h2.getFitness();

		  /*For ascending order*/
		  
		  /*if (obj1-obj2 > 0) {
			  return 1;
		  } else if (obj1-obj2 < 0){
			  return -1;
		  } else {
			  double sum1 = h1.getSensitivity()+h1.getSpecificy();
			  double sum2 = h2.getSensitivity()+h2.getSpecificy();
			  
			  if (sum1-sum2 > 0) {
				  return -1;
			  } else if (sum1-sum2 > 0) {
				  return 1;
			  }
			  
			  return 0; 
		  }*/
		  
		  /*For ascending order*/ 
		  if (fit1-fit2 > 0) {
			  return 1;
		  } 
		  
		  if (fit1-fit2 < 0){
			  return -1;
		  }
		  
		  return 0; 

		  
		   /*For descending order*/
		  /*if (fit1-fit2 > 0) {
			  return -1;
		  } 
		  
		  if (fit1-fit2 < 0){
			  return 1;
		  } 
		  
		  return 0;*/
	   }};
	
	@Override
	public Harmony clone() {
		Harmony cloned = null;
		try {
			cloned = (Harmony) super.clone();
		} catch (CloneNotSupportedException e) {
			
		}
		
		return cloned;
	}
}
