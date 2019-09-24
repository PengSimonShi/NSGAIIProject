package project.algorithm.NSGAII;

import project.algorithm.HS.Harmony;
import project.data.Patient;
import project.start.*;

import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class Chromosome {

	private int[] chromosome = null;
	private int totalgen = 0;
	private ArrayList<Integer> gen = null;
	private ArrayList<Integer> nogen = null;
	private double fitness_value;
	private double sum_sen_spe;
	
	private int rank = 0;
	private int number_dominates = 0;
	private ArrayList<Integer> dominators = null;
	private double crowd_distance = 0.0;
	// private boolean check;
	
	private Harmony har = null;
	
	public Chromosome() {
		
	}
	
	public Chromosome(int length) {
		this.chromosome = new int[length];
		this.har = new Harmony(length);
		clear();
	}
	
	public void buildChromosome(int size, int random_seed) {
		int[] chromo = new int[this.chromosome.length];
		
		ArrayList<Integer> nogen_copy = (ArrayList<Integer>)this.nogen.clone();
		RandomGenerator rnd = new RandomGenerator(random_seed);
		for (int i = 0; i < size; i++) {
			int index = (int) rnd.getRandomIndex(nogen_copy.size());
			chromo[nogen_copy.get(index)] = 1;
			nogen_copy.remove(nogen_copy.get(index));
		}
		buildChromosome(chromo);
	}
	
	public void buildChromosome(int[] chromosome) {
		this.chromosome = new int[chromosome.length];
		clear();
		for (int i = 0; i < chromosome.length; i++) {
			this.chromosome[i] = chromosome[i];
			if (chromosome[i] == 1) {
				this.totalgen++;
				this.gen.add(i);
				this.nogen.remove((Integer) i);
			}
		}
	}
	
	/*
	 * flip the gen value from 1 to 0.
	 */
	public void flipGen(int size, int random_seed) {
		RandomGenerator rnd = new RandomGenerator(random_seed);
		while(this.totalgen > size) {
			int index = (int)rnd.getRandomIndex(this.totalgen);
			int val = this.gen.get(index);
			this.chromosome[val] = 0;
			this.gen.remove(index);
			this.nogen.add(val);
			this.totalgen--;
		}
	}
	
	/*
	 * flip the gen value from 0 to 1.
	 */
	public void antiflipGen(int size, int random_seed) {
		RandomGenerator rnd = new RandomGenerator(random_seed);
		while (this.totalgen < size) {
			int index = (int)rnd.getRandomIndex(this.nogen.size());
			int val = this.nogen.get(index);
			this.chromosome[val] = 1;
			this.nogen.remove(index);
			this.gen.add(val);
			this.totalgen++;
		}
	}
	
	public double getFitnessValue() {
		return this.fitness_value;
	}
	
	/*public boolean getStatus() {
		return this.check;
	}
	
	public void setCheck() {
		this.check = true;
	}*/
	
	public double getSumValue() {
		return this.sum_sen_spe;
	}
	
	public double getSensitivity() {
		return this.har.getSensitivity();
	}
	
	public double getSpecificity() {
		return this.har.getSpecificy();
	}
	
	public int getNumberDominates() {
		return this.number_dominates;
	}
	
	public void updateNumberDominates() {
		this.number_dominates++;
	}
	
	public void decreaseNumberDominates() {
		this.number_dominates--;
	}
	
	public void setNumberDominates(int number_dominates) {
		this.number_dominates = number_dominates;
	}
	
	public int getRank() {
		return this.rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public double getCrowdDistance() {
		return this.crowd_distance;
	}
	
	public void setCrowdDistance(double crowd_distance) {
		this.crowd_distance = crowd_distance;
	}
	
	public ArrayList<Integer> getDominators() {
		return this.dominators;
	}
	
	public void setDominators(ArrayList<Integer> dominators) {
		this.dominators.clear();
		for (int i = 0; i < dominators.size(); i++) {
			int value = dominators.get(i);
			this.dominators.add(value);
		}
	}
	
	public int getSpecificDominator(int index) {
		return this.dominators.get(index);
	}
	
	public void removeSpecificDominator(int index) {
		this.dominators.remove(index);
	}
	
	public double[] getCutOff() {
		return this.har.getHarmonyValue();
	}
	
	public void setFitnessValue(double fitness_value) {
		this.fitness_value = fitness_value;
	}
	
	public void setSumValues(double sum_sen_spe) {
		this.sum_sen_spe = sum_sen_spe;
	}
	
	public void setFullChomosome(int[] chromosome) {
		buildChromosome(chromosome);
	}
	
	public void setChomosomeValue(int index, int value) {
		if (this.chromosome[index] != value) {
			if (value == 1) {
				this.totalgen++;
				this.gen.add(index);
				this.nogen.remove((Integer) index);
			} else {
				this.totalgen--;
				this.nogen.add(index);
				this.gen.remove((Integer) index);
			}
		}
		
		this.chromosome[index] = value;
	}
	
	public int getChomosomeValue(int index) {
		return this.chromosome[index];
	}
	
	public int[] getChomosome() {
		int[] cloned = new int[this.chromosome.length];
		for (int i = 0; i < cloned.length; i++) {
			cloned[i] = this.chromosome[i];
		}
		
		return cloned;
	}
	
	public void setHarmony(Harmony har) {
		this.har.setHarmony(har, this.chromosome);
		setFitnessValue(har.getFitness());
		double sens = har.getSensitivity();
		double spec = har.getSpecificy();
		double sum = sens + spec;
		setSumValues(sum);
	}
	
	public ArrayList<Integer> getChomosomeIndex() {
		return (ArrayList<Integer>)this.gen.clone();
	}
	
	public int getTotalGen() {
		return this.totalgen;
	}
	
	public void clear() {
		this.chromosome = new int[this.chromosome.length];
		this.totalgen = 0;
		this.fitness_value = Double.MAX_VALUE;
		this.sum_sen_spe = Double.MAX_VALUE;
		this.gen = new ArrayList<Integer>();
		this.nogen = new ArrayList<Integer>();
		for (int i = 0; i < this.chromosome.length; i++) {
			this.nogen.add(i);
		}
		// this.chromosome_index = new ArrayList<Integer>();
		NSGAIIInitialisation();
	}
	
	public void NSGAIIInitialisation() {
		this.rank = 0;
		this.dominators = new ArrayList<Integer>();
		this.number_dominates = 0;
		this.crowd_distance = 0.0;
		// this.check = false;
	}
	
	public void PintOnScreen() {
		System.out.println("The fitness value is " + this.fitness_value + " and sensitivity is "+this.har.getSensitivity() + " and the specificity is "+ this.har.getSpecificy());
		System.out.println("The panel is " + Arrays.toString(this.gen.toArray()));
		System.out.println("The cut-off of this panel is "+ Arrays.toString(this.har.getHarmonyValue()));
	}
	
	public static Comparator<Chromosome> sortByFITNESS = new Comparator<Chromosome>() {

		public int compare(Chromosome ch1, Chromosome ch2) {

		  double fitness1 = ch1.getFitnessValue();
		  double fitness2 = ch2.getFitnessValue();

		  /*For ascending order*/
		  if (fitness1-fitness2 > 0) {
			  return 1;
		  } 
		  if (fitness1-fitness2 < 0){
			  return -1;
		  }
		  return 0;  
		  

		   /*For descending order*/
		 /* if (fitness1-fitness2 > 0) {
			  return -1;
		  } 
		  
		  if (fitness1-fitness2 < 0){
			  return 1;
		  } 
		  
		  return 0;*/
	   }};
	
	public static Comparator<Chromosome> sortBySENS = new Comparator<Chromosome>() {

			public int compare(Chromosome ch1, Chromosome ch2) {

			  double sens1 = ch1.getSensitivity();
			  double sens2 = ch2.getSensitivity();
			  
			  /*For ascending order*/
			  /*if (sens1-sens2 > 0) {
				  return 1;
			  } 
			  if (sens1-sens2 < 0){
				  return -1;
			  }
			  return 0;  */
			  

			   /*For descending order*/
			  if (sens1-sens2 > 0) {
				  return -1;
			  } 
			  
			  if (sens1-sens2 < 0){
				  return 1;
			  } 
			  
			  return 0;
		   }};   
	
	public static Comparator<Chromosome> sortBySPEC = new Comparator<Chromosome>() {

				public int compare(Chromosome ch1, Chromosome ch2) {

				  double spec1 = ch1.getSpecificity();
				  double spec2 = ch2.getSpecificity();
				  
				  /*For ascending order*/
				  /*if (spec1-spec2 > 0) {
					  return 1;
				  } 
				  if (spec1-spec2 < 0){
					  return -1;
				  }
				  return 0;  */
				  

				   /*For descending order*/
				  if (spec1-spec2 > 0) {
					  return -1;
				  } 
				  
				  if (spec1-spec2 < 0){
					  return 1;
				  } 
				  
				  return 0;
			   }}; 	   

	public static Comparator<Chromosome> sortByDISTANCE = new Comparator<Chromosome>() {

					public int compare(Chromosome ch1, Chromosome ch2) {

					  double distance1 = ch1.getCrowdDistance();
					  double distance2 = ch2.getCrowdDistance();
					  
					  /*For ascending order*/
					  /*if (distance1-distance2 > 0) {
						  return 1;
					  } 
					  if (distance1-distance2 < 0){
						  return -1;
					  }
					  return 0;  */
					  

					   /*For descending order*/
					  if (distance1-distance2 > 0) {
						  return -1;
					  } 
					  
					  if (distance1-distance2 < 0){
						  return 1;
					  } 
					  
					  return 0;
				   }}; 				   
			   
	@Override
	public Chromosome clone() {
		Chromosome cloned = null;
		try {
			cloned = (Chromosome) super.clone();
		} catch (CloneNotSupportedException e) {
			
		}
		
		return cloned;
	}
}
