package project.algorithm.HS;

import project.data.*;
import project.files.SaveFile;
import project.start.*;

import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class HarmonySearch {

	private int hm_size;
	private int number_iterations;
	// hscr: considering rate of harmony search, to determine the new harmony value
	private double hscr;
	// par: pitch adjusting rate. If the new harmony value is selected from the harmony memory then adjust to a neighbour value.
	private double par;
	private double neighbour_gap; 
	
	private double sensi_goal;
	private double speci_goal;
	
	private ArrayList<Harmony> harmony_memory = null;
	private ArrayList<Harmony> best_harmony = null;
	private ArrayList<Harmony> update_harmony = null; 
	
	public HarmonySearch() {
		initialise(1, 1, 100, 400, 0.7, 0.15, 0.05);
	}
	
	public HarmonySearch(double sensi_goal, double speci_goal) {
		initialise(sensi_goal, speci_goal, 100, 400, 0.7, 0.15, 0.05);
	}
	
	public HarmonySearch(int hm_size, int iterations, double hscr, double par, double neighbour_gap, double sensi_goal, double speci_goal) {
		initialise(sensi_goal, speci_goal, hm_size, iterations, hscr, par, neighbour_gap);
	}
	
	public void initialise(double sensi_goal, double speci_goal, int hm_size, int number_iterations, double hscr, double par, double neighbour_gap) {
		this.hm_size = hm_size;
		this.number_iterations = number_iterations;
		this.hscr = hscr;
		this.par = par;
		this.neighbour_gap = neighbour_gap;
		this.harmony_memory = new ArrayList<Harmony>();
		this.best_harmony = new ArrayList<Harmony>();
		this.update_harmony = new ArrayList<Harmony>();
		this.sensi_goal = sensi_goal;
		this.speci_goal = speci_goal;
	}
	
	public void run(int[] chromosome, ArrayList<Antigen> antigen_info, ArrayList<Patient> patient_info, int threshold, int random_seed, int iteration, int total_iteration) {
		initialHarmonyBuild(chromosome, antigen_info, patient_info, threshold, random_seed, iteration, total_iteration);
		updateHarmony(chromosome, antigen_info, patient_info, threshold, random_seed, iteration, total_iteration);
		// System.out.println("The last Harmony Memory is ");
		// PrintOnScreenBest();
	}
	
	public void initialHarmonyBuild(int[] chromosome, ArrayList<Antigen> antigen_info, ArrayList<Patient> patient_info, int threshold, int random_seed, int iteration, int total_iteration) {
		for (int i = 0; i < this.hm_size; i++) {
			Harmony har = new Harmony(antigen_info.size());
			har.build(chromosome, antigen_info, random_seed);
			har.calculateVar(patient_info, threshold);
			har.calculateFitness(this.sensi_goal, this.speci_goal, iteration, total_iteration);
			this.harmony_memory.add(har);
		}
		
		Collections.sort(this.harmony_memory, Harmony.sortByFITNESS);
		
		// System.out.println("The initial Harmony Memory is ");
		// PrintOnScreenBest();
	}
	
	public void updateHarmony(int[] chromosome, ArrayList<Antigen> antigen_info, ArrayList<Patient> patient_info, int threshold, int random_seed, int iteration, int total_iteration) {
		int iter = 0;
		RandomGenerator rnd = new RandomGenerator(random_seed);
		while (iter < this.number_iterations) {
			Harmony new_har = new Harmony(antigen_info.size());
			new_har.setChromosome(chromosome);
			for (int i = 0; i < chromosome.length; i++) {
				if (chromosome[i] == 1) {
					double par_hscr = rnd.getRandomValue(0, 1);
					double par_par = rnd.getRandomValue(0, 1);
					if (par_hscr > this.hscr) {
						int index = rnd.getRandomIndex(antigen_info.get(i).getCutOffSize());
						new_har.setHarmonyIndexValue(i, antigen_info.get(i).getCutOffValue(index));
					} else {
						int index = (int)rnd.getRandomIndex(this.hm_size);
						double val = this.harmony_memory.get(index).getHarmonyIndexValue(i);
						
						/*if (par_par < this.par) {
							double change = Math.abs(antigen_info.get(i).getMinValue() - antigen_info.get(i).getMaxValue())*this.neighbour_gap;
							
							if (val + change >= antigen_info.get(i).getMaxValue() && val - change >= antigen_info.get(i).getMinValue()) {
								val = val - change;
							} else if (val + change <= antigen_info.get(i).getMaxValue()) {
								val = val + change;
							}
						}*/
						
						if (par_par < this.par) {
							Antigen anti = antigen_info.get(i);
							int value_index = anti.getValueIndex(val);
							int neigh_index = anti.getNeighbourIndex(value_index, this.neighbour_gap);
							val = anti.getCutOffValue(neigh_index);
						}
						new_har.setHarmonyIndexValue(i, val);
					}
				} else {
					new_har.setHarmonyIndexValue(i, 0);
				}
			}
			
			new_har.calculateVar(patient_info, threshold);
			new_har.calculateFitness(this.sensi_goal, this.speci_goal, iteration, total_iteration);
			
			
			// version 1.0: if the order of the list is in descent order.
			/*if (new_har.getFitness() > this.harmony_memory.get(this.hm_size-1).getFitness()) {
				this.harmony_memory.remove(this.hm_size-1);
				this.harmony_memory.add(new_har);
				
				if (new_har.getFitness() > this.harmony_memory.get(0).getFitness()) {
					this.update_harmony.add(new_har);
				}
						
				Collections.sort(this.harmony_memory, Harmony.sortByFITNESS);
			}*/
			
			// version 1.1: if the order of the list is in accedent order.
			/*if (new_har.getFitness() > this.harmony_memory.get(0).getFitness()) {
				this.harmony_memory.remove(0);
				this.harmony_memory.add(new_har);
				
				if (new_har.getFitness() > this.harmony_memory.get(this.hm_size-1).getFitness()) {
					this.update_harmony.add(new_har);
				}
						
				Collections.sort(this.harmony_memory, Harmony.sortByFITNESS);
			}*/
			
			// version 1.2: if the order of the list is in acedent order and the fitness value is to select the max value
			if (new_har.getFitness() < this.harmony_memory.get(this.hm_size-1).getFitness()) {
				this.harmony_memory.remove(this.hm_size-1);
				this.harmony_memory.add(new_har);
				
				if (new_har.getFitness() < this.harmony_memory.get(0).getFitness()) {
					this.update_harmony.add(new_har);
				}
						
				Collections.sort(this.harmony_memory, Harmony.sortByFITNESS);
			}
			
			iter++;
			
			Harmony best_har = new Harmony(antigen_info.size());
			// best_har = this.harmony_memory.get(this.hm_size-1);
			best_har = this.harmony_memory.get(0);
			this.best_harmony.add(best_har);
		}
	}
	
	public ArrayList<Harmony> getHarmonyMemory() {
		return (ArrayList<Harmony>)this.harmony_memory.clone();
	}
	
	public Harmony getFinalBestHarmony() {
		return this.harmony_memory.get(0);
	}
	
	public ArrayList<Harmony> getBestHarmony() {
		return (ArrayList<Harmony>) this.best_harmony.clone();
	}
	
	public ArrayList<Harmony> getUpdateHarmony () {
		return (ArrayList<Harmony>) this.update_harmony.clone(); 
	}
	
	public void PrintOnScreen() {
		for (int i = 0; i < this.harmony_memory.size(); i++) {
			this.harmony_memory.get(i).PrintOnScreen();
		}
	}
	
	public void PrintOnScreenBest() {
		this.harmony_memory.get(0).PrintOnScreen();
	}
}
