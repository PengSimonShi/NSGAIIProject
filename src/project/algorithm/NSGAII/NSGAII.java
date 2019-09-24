package project.algorithm.NSGAII;

import project.start.RandomGenerator;
import project.data.*;
import project.algorithm.HS.*;
import project.files.*;

import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class NSGAII {

	// the population size should be based on 4.
	private int population_size;
	private int number_iterations;
	private double crossover_rate;
	private double mutation_rate;
	private int chromosome_length;
	private int random_seed;
	
	private ArrayList<Chromosome> initial_population = null;
	private ArrayList<Chromosome> current_population = null;
	private ArrayList<Chromosome> best_population = null;
	private ArrayList<Chromosome> updated_population = null;
	
	private double sensitivity_goal = 0;
	private double specificity_goal = 0;
	
	private long start_time;
	
	public NSGAII() {
		RandomGenerator rnd = new RandomGenerator(0);
		double mt_rate = rnd.getRandomValue(0, 0.1);
		initialise(200, 500, 0.5, mt_rate, -1, 1, 1, 0);
	}
	
	public NSGAII(int random_seed) {
		RandomGenerator rnd = new RandomGenerator(random_seed);
		double mt_rate = rnd.getRandomValue(0, 0.1);
		initialise(200, 500, 0.5, mt_rate, -1, 1, 1, random_seed);
	}
	
	public NSGAII(int chromosome_length, int random_seed) {
		RandomGenerator rnd = new RandomGenerator(random_seed);
		double mt_rate = rnd.getRandomValue(0, 0.1);
		initialise(200, 500, 0.5, mt_rate, chromosome_length, 1, 1, random_seed);
	}
	
	public NSGAII(int chromosome_length, double sensi_goal, double spec_goal, int random_seed) {
		double mt_rate = 0.05;
		initialise(200, 500, 0.5, mt_rate, chromosome_length, sensi_goal, spec_goal, random_seed);
	}
	
	public NSGAII(int population_size, int number_iterations, double crossover_rate, double mutation_rate, int chromosome_length, int random_seed) {
		initialise(population_size, number_iterations, crossover_rate, mutation_rate, chromosome_length, 1, 1, random_seed);
	}
	
	public void initialise(int population_size, int number_iterations, double crossover_rate, double mutation_rate, int chromosome_length, double sensi_goal, double spec_goal, int random_seed) {
		this.population_size = population_size;
		this.number_iterations = number_iterations;
		this.crossover_rate = crossover_rate;
		this.mutation_rate = mutation_rate;
		this.chromosome_length = chromosome_length;
		this.random_seed = random_seed;
		
		this.initial_population = new ArrayList<Chromosome>();
		this.current_population = new ArrayList<Chromosome>();
		this.best_population = new ArrayList<Chromosome>();
		this.updated_population = new ArrayList<Chromosome>();
		
		this.sensitivity_goal = sensi_goal;
		this.specificity_goal = spec_goal;
		
		this.start_time = 0;
	}
	
	public void run(int chromosome_length, int panel_size, ArrayList<Antigen> antigen_info, ArrayList<Patient> patient_info, String save_folder, String final_result_folder, long start_time, int threshold, Settings set) {

		this.start_time = start_time;
		RandomGenerator rnd = new RandomGenerator(this.random_seed);
		PopulationInitialise(chromosome_length, panel_size, antigen_info, patient_info, save_folder, threshold, 0);
		this.current_population = (ArrayList<Chromosome>)this.initial_population.clone();
		
		int front_size = 0;
		int iteration = 0;
		while (iteration < this.number_iterations) {
			front_size = buildNextGeneration(chromosome_length, panel_size, antigen_info, patient_info, save_folder, iteration, threshold);
			iteration++;
		}
		
		SaveFile sf = new SaveFile();
		double running_time = getRunningTime();
		sf.excuteNSGAIIResults(front_size, running_time, final_result_folder, "Pareto_Front", this.current_population, antigen_info, set);
	}
	
	public void PopulationInitialise(int chromosome_length, int panel_size, ArrayList<Antigen> antigen_info, ArrayList<Patient> patient_info, String save_folder, int threshold, int iteration) {
		
		SaveFile sf = new SaveFile();
		for (int i = 0; i < this.population_size; i++) {
			Chromosome chr = new Chromosome(chromosome_length);
			chr.buildChromosome(panel_size, this.random_seed);
			HarmonySearch hs = new HarmonySearch(this.sensitivity_goal, this.specificity_goal);
			hs.run(chr.getChomosome(), antigen_info, patient_info, threshold, this.random_seed, iteration, this.number_iterations);
			chr.setHarmony(hs.getFinalBestHarmony());
			this.initial_population.add(chr);
		}
		
		ArrayList<ArrayList<Integer>> fronts = new ArrayList<ArrayList<Integer>>();
		fronts = NonDominatedSort(this.initial_population);
		
		double running_time = getRunningTime();
		sf.excuteNSGAIIResults(save_folder, "Initialisation", this.initial_population, running_time);
	}
	
	public int buildNextGeneration(int chromosome_length, int panel_size, ArrayList<Antigen> antigen_info, ArrayList<Patient> patient_info, String save_folder, int iteration, int threshold) {
		RandomGenerator rnd = new RandomGenerator(this.random_seed);
		SaveFile sf = new SaveFile();
		
		ArrayList<Integer> population_index = new ArrayList<Integer>();
		for (int i = 0; i < this.population_size; i++) {
			population_index.add(i);
		}
		
		
		ArrayList<Chromosome> overall_population = new ArrayList<Chromosome>();
		while (population_index.size() > 0) {
			int parent1_index = selectParent(population_index, rnd);
			population_index.remove((Integer) parent1_index);
			int parent2_index = selectParent(population_index, rnd);
			population_index.remove((Integer) parent2_index);
			
			Chromosome parent1 = new Chromosome(chromosome_length);
			Chromosome parent2 = new Chromosome(chromosome_length);
			parent1 = this.current_population.get(parent1_index);
			parent2 = this.current_population.get(parent2_index);
			
			int[] parent1_chr = parent1.getChomosome();
			int[] parent2_chr = parent2.getChomosome();
			
			int[] child1_chr = new int[parent1_chr.length];
			int[] child2_chr = new int[parent2_chr.length];
			
			for (int i = 0; i < chromosome_length; i++) {
				if (rnd.getRandomValue(0, 1) > this.crossover_rate) {
					child1_chr[i] = parent1_chr[i];
					child2_chr[i] = parent2_chr[i];
				} else {
					child1_chr[i] = parent2_chr[i];
					child2_chr[i] = parent1_chr[i];
				}
				
				if (rnd.getRandomValue(0, 1) < this.mutation_rate) {
					child1_chr[i] = 1- child1_chr[i];
				}
				
				if (rnd.getRandomValue(0, 1) < this.mutation_rate) {
					child2_chr[i] = 1- child2_chr[i];
				}
			}
			
			Chromosome child1 = new Chromosome(chromosome_length);
			buildChromosome(child1, child1_chr, panel_size, antigen_info, patient_info, threshold, iteration);
			overall_population.add(child1);
			
			Chromosome child2 = new Chromosome(chromosome_length);
			buildChromosome(child2, child2_chr, panel_size, antigen_info, patient_info, threshold, iteration);
			overall_population.add(child2);
			
			parent1.NSGAIIInitialisation();
			overall_population.add(parent1);
			parent2.NSGAIIInitialisation();
			overall_population.add(parent2);
			
		}
		
		ArrayList<ArrayList<Integer>> fronts = new ArrayList<ArrayList<Integer>>();
		fronts = NonDominatedSort(overall_population);
		
		ArrayList<Chromosome> current_temp  = new ArrayList<Chromosome>();
		int front_index = -1;
		for (int i = 0; i < fronts.size(); i++) {
			front_index = i;
			ArrayList<Integer> front = fronts.get(i);
			if (fronts.get(i).size() + current_temp.size() <= this.population_size) {
				for (int j = 0; j < front.size(); j++) {
					Chromosome chr = new Chromosome(chromosome_length);
					chr = overall_population.get(front.get(j));
					current_temp.add(chr);
				}
			} else {
				break;
			}
		}
		
		ArrayList<Integer> front = fronts.get(front_index);
		ArrayList<Chromosome> front_population = new ArrayList<Chromosome>();
		for (int i = 0; i < front.size(); i++) {
			Chromosome chr = new Chromosome(chromosome_length);
			chr = overall_population.get(front.get(i));
			front_population.add(chr);
		}
		
		CrowdingDistanceAssignment(front_population);
		Collections.sort(front_population, Chromosome.sortByDISTANCE);
		for (int i = 0; i < front_population.size(); i++) {
			if (current_temp.size() == this.population_size) {
				break;
			} else if (current_temp.size() < this.population_size) {
				Chromosome chr = new Chromosome(chromosome_length);
				chr = front_population.get(i);
				current_temp.add(chr);
			}
		}
		
		this.current_population.clear();
		this.current_population = (ArrayList<Chromosome>)current_temp.clone();
		String iter = "Iteration"+iteration;
		double running_time = getRunningTime();
		sf.excuteNSGAIIResults(save_folder, iter, current_temp, running_time);
		
		return fronts.get(0).size();
	}

	public ArrayList<ArrayList<Integer>> NonDominatedSort(ArrayList<Chromosome> population) {
		
		ArrayList<ArrayList<Integer>> fronts = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> first_front = new ArrayList<Integer>();
		ArrayList<Integer> uncheck_list = new ArrayList<Integer>();
		
		for (int i = 0; i < population.size(); i++) {
			int number_dominates = 0;
			ArrayList<Integer> dominators = new ArrayList<Integer>();
			double sens = population.get(i).getSensitivity();
			double spec = population.get(i).getSpecificity();
			
			for (int j = 0; j < population.size(); j++) {
				if (j != i) {
					double sens_compare = population.get(j).getSensitivity();
					double spec_compare = population.get(j).getSpecificity();
					
					if ((sens <= sens_compare && spec <= spec_compare) && (sens < sens_compare || spec < spec_compare)) {
						number_dominates++;
					} else if((sens_compare <= sens && spec_compare <= spec) && (sens_compare < sens || spec_compare < spec)) {
						dominators.add(j);
					}
				}
			}
			
			population.get(i).setDominators(dominators);
			population.get(i).setNumberDominates(number_dominates);
			uncheck_list.add(i);
			
			if (number_dominates == 0) {
				first_front.add(i);
				uncheck_list.remove((Integer) i);
				population.get(i).setRank(0);
			}
		}
		fronts.add(first_front);
		
		int rank = 1;
		while(!uncheck_list.isEmpty()) {
			ArrayList<Integer> current_front = new ArrayList<Integer>();
			ArrayList<Integer> front = new ArrayList<Integer>();
			front = (ArrayList<Integer>)fronts.get(fronts.size()-1).clone();
			for (int i = 0; i < front.size(); i++) {
				ArrayList<Integer> domators = population.get(front.get(i)).getDominators();
				for (int j = 0; j < domators.size(); j++) {
					population.get(domators.get(j)).decreaseNumberDominates();
				}
			}
			
			for (int i = 0; i < uncheck_list.size(); i++) {
				if (population.get(uncheck_list.get(i)).getNumberDominates() == 0) {
					current_front.add(uncheck_list.get(i));
					population.get(uncheck_list.get(i)).setRank(rank);
					uncheck_list.remove((Integer) uncheck_list.get(i));
					i--;
				}
			}
			fronts.add(current_front);
			rank++;
		}
		
		return (ArrayList<ArrayList<Integer>>)fronts.clone();
	}
	
	public void CrowdingDistanceAssignment(ArrayList<Chromosome> chromosomes) {
		double large_value = 10000.0;
		Collections.sort(chromosomes, Chromosome.sortBySENS);
		for (int i = 0; i < chromosomes.size(); i++) {
			double distance = chromosomes.get(i).getCrowdDistance();
			if (i == 0 || i == chromosomes.size()-1) {
				distance = distance + large_value;
			} else {
				distance = distance + (chromosomes.get(i-1).getSensitivity() - chromosomes.get(i+1).getSensitivity())/(chromosomes.get(0).getSensitivity() - chromosomes.get(chromosomes.size()-1).getSensitivity());
			}
			chromosomes.get(i).setCrowdDistance(distance);
		}
		
		Collections.sort(chromosomes, Chromosome.sortBySPEC);
		for (int i = 0; i < chromosomes.size(); i++) {
			double distance = chromosomes.get(i).getCrowdDistance();
			if (i == 0 || i == chromosomes.size()-1) {
				distance = distance + large_value;
			} else {
				distance = distance + (chromosomes.get(i-1).getSpecificity() - chromosomes.get(i+1).getSpecificity())/(chromosomes.get(0).getSpecificity() - chromosomes.get(chromosomes.size()-1).getSpecificity());
			}
			chromosomes.get(i).setCrowdDistance(distance);
		}
	}
	
	public int selectParent(ArrayList<Integer> population_index, RandomGenerator rnd) {
		int selection = -1;
		int parent1_index = population_index.get(rnd.getRandomIndex(population_index.size()));
		int parent2_index = population_index.get(rnd.getRandomIndex(population_index.size()));
		
		if (this.current_population.get(parent1_index).getRank() <= this.current_population.get(parent2_index).getRank()) {
			selection = parent1_index;
		} else {
			selection = parent2_index;
		}
		
		return selection;
	}
	
	public void buildChromosome(Chromosome chr, int[] chromosome_index, int panel_size, ArrayList<Antigen> antigen_info, ArrayList<Patient> patient_info, int threshold, int iteration) {
		chr.buildChromosome(chromosome_index);
		if (chr.getTotalGen() > panel_size) {
			chr.flipGen(panel_size, this.random_seed);
		} else if(chr.getTotalGen() < panel_size){
			chr.antiflipGen(panel_size, this.random_seed);
		}
		HarmonySearch hs = new HarmonySearch();
		hs.run(chr.getChomosome(), antigen_info, patient_info, threshold, this.random_seed, iteration, this.number_iterations);
		chr.setHarmony(hs.getFinalBestHarmony());
	}
	
	public double getRunningTime() {
		long end_time = System.currentTimeMillis();
		double running_time = (end_time - this.start_time)*1.0/1000;
		return running_time;
	}
	
	public int getPopulationSize() {
		return this.population_size;
	}

	public void setPopulationSize(int population_size) {
		this.population_size = population_size;
	}

	public int getNumberIteration() {
		return this.number_iterations;
	}

	public void setNumberIteration(int number_iteration) {
		this.number_iterations = number_iteration;
	}

	public double getCrossoverRate() {
		return this.crossover_rate;
	}

	public void setCrossoverRate(double crossover_rate) {
		this.crossover_rate = crossover_rate;
	}

	public double getMutationRate() {
		return this.mutation_rate;
	}

	public void setMutationRate(double mutation_rate) {
		this.mutation_rate = mutation_rate;
	}

	public int getChromosomeLength() {
		return this.chromosome_length;
	}

	public void setChromosomeLength(int chromosome_length) {
		this.chromosome_length = chromosome_length;
	}

	public ArrayList<Chromosome> getInitialPopulation() {
		return (ArrayList<Chromosome>) this.initial_population.clone();
	}

	public ArrayList<Chromosome> getBestPopulation() {
		return (ArrayList<Chromosome>) this.best_population.clone();
	}

	public ArrayList<Chromosome> getUpdatedPopulation() {
		return (ArrayList<Chromosome>) this.updated_population.clone();
	}
	
	public double[] getBestValues() {
		double[] values = new double[2];
		values[0] = this.best_population.get(0).getSensitivity();
		values[1] = this.best_population.get(0).getSpecificity();
		return values;
	}
}
