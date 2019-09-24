package project.start;

import project.algorithm.*;
import project.algorithm.HS.*;
import project.algorithm.NSGAII.*;
import project.data.*;
import project.files.*;

import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class Solver {

	private ArrayList<Patient> patient_info = null;
	private ArrayList<Antigen> antigen_info = null;
	
	private int panel_size;
	private boolean processGA = false;
	private boolean processHS = true;
	
	// private int[] chromosome_index = null;
	
	private double running_time = 0;
	
	private double[] best_values = null;
	
	private long start_time = 0;
	
	public Solver(long start_time) {
		initialise(start_time, 6);
	}
	
	public Solver(long start_time, int panel_size) {
		initialise(start_time, panel_size);
	}
	
	public void initialise(long start_time, int panel_size) {
		this.patient_info = new ArrayList<Patient>();
		this.antigen_info = new ArrayList<Antigen>();
		this.panel_size = panel_size;
		
		this.processGA = false;
		this.processHS = true;
		
		this.start_time = start_time;
		this.best_values = new double[2];
	}
	
	public void start(int random_seed, String file_name, double sensi_goal, double spec_goal, ReadFile rf, String save_folder, String final_result_folder, int threshold, Settings set) {
		
		System.out.println("The start running time is "+this.start_time + " and the panel size is "+this.panel_size + " and the threshold is "+threshold);
		
		this.patient_info = rf.getPatientInformation();
		this.antigen_info = rf.getAntigenInformation();
		
		NSGAII nsga = new NSGAII(this.antigen_info.size(), sensi_goal, spec_goal, random_seed);
		nsga.run(this.antigen_info.size(), this.panel_size, this.antigen_info, this.patient_info, save_folder, final_result_folder, this.start_time, threshold, set);
		
		/*double[] src = nsga.getBestValues();
		for (int i = 0; i < this.best_values.length; i++) {
			this.best_values[i] = src[i];
		}*/
		// long end_time = System.currentTimeMillis();
		
		// this.running_time = (end_time-start_time)*1.0/1000;
		// Chromosome chromo = new Chromosome(this.antigen_info.size());
		// chromo.buildChromosome(this.panel_size);
		
		// int[] chromosome_index = {0, 1, 2, 3, 4, 5};
		// chromo.buildChromosome(chromosome_index);
		
		// System.out.println("The selected antigen index is ");
		// System.out.println(Arrays.toString(chromo.getChomosomeIndex().toArray()));
		
		// if (processGA) {
			 
		// }
		
		// if (processHS) {
		// 	HarmonySearch hs = new HarmonySearch();
		// 	hs.run(chromo.getChomosome(), this.antigen_info, this.patient_info);
		// }
	}

	/*public void start(int random_seed, String file_name, String[] panel, int threshold) {
		
		this.panel_size = panel.length;
		
		ReadFile rf = new ReadFile();
		rf.read(file_name);
		this.patient_info = rf.getPatientInformation();
		this.antigen_info = rf.getAntigenInformation();
		
		int[] chromosome_index = new int[this.panel_size];
 		
		chromosome_index = getIndexOfAntigen(panel);
		
		Chromosome chromo = new Chromosome(this.antigen_info.size());
		chromo.buildChromosome(chromosome_index);
		
		System.out.println("The selected antigen index is ");
		System.out.println(Arrays.toString(chromo.getChomosomeIndex().toArray()));
		
		
		// if (processHS) {
			HarmonySearch hs = new HarmonySearch();
			hs.run(chromo.getChomosome(), this.antigen_info, this.patient_info, threshold, random_seed);
		// }
		
		// String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\NY\\";
		String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\Dundee\\";
		SaveFile sf = new SaveFile();
		ArrayList<Harmony> result = new ArrayList<Harmony>();
		result = hs.getBestHarmony();
		sf.excuteHarmonyResults(save_folder, chromosome_index, result);
	}*/
	
	public int[] getIndexOfAntigen(String[] panel) {
		int[] chromosome_index = new int[this.panel_size];
		for (int i = 0; i < panel.length; i++) {
			for (int j = 0; j < this.antigen_info.size(); j++) {
				if (this.antigen_info.get(j).getAntigenName().equals(panel[i])) {
					chromosome_index[i] = j;
					break;
				}
			}
		}
		
		return chromosome_index.clone();
	}
	
	public double[] getBestValues() {
		return this.best_values;
	}
}
