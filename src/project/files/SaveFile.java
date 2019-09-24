package project.files;

import project.algorithm.HS.*;
import project.algorithm.NSGAII.*;
import project.data.*;

import java.io.*;
import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class SaveFile {

	public SaveFile() {
		
	}
	
	public void excuteHarmonyResults(String folder_name, int[] panel_index, ArrayList<Harmony> best_harmony) {
		String pre_name = folder_name;
		
		File file = new File(folder_name);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		for (int i = 0; i < panel_index.length; i++) {
			pre_name = pre_name+panel_index[i]+"-";
		}
		String cutoff_file = pre_name + "cutoff.csv";
		String var_file = pre_name + "value.csv";
		
		try{
			BufferedWriter bw_cut = new BufferedWriter(new FileWriter(cutoff_file));
			BufferedWriter bw_var = new BufferedWriter(new FileWriter(var_file));
			
			String content = "Sensitivity, Specificity";
			bw_var.write(content);
			bw_var.newLine();
			
			for (int i = 0; i < best_harmony.size(); i++) {
				bw_cut.write(Arrays.toString(best_harmony.get(i).getHarmonyValue()));
				bw_var.write(best_harmony.get(i).getSensitivity() + "," + best_harmony.get(i).getSpecificy());
				
				bw_cut.newLine();
				bw_var.newLine();
			}
			
			bw_cut.close();
			bw_var.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void excuteNSGAIIResults(String folder_name, String iterations, ArrayList<Chromosome> population, double time ) {
		String pre_name = folder_name+iterations+".csv";
		
		File file = new File(folder_name);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		try {
			BufferedWriter bw_value = new BufferedWriter(new FileWriter(pre_name));
			
			String content = "The running time is, " + time;
			bw_value.write(content);
			bw_value.newLine();
			
			content = "Panel, Sensitivity, Specificity, CutOff, Rank";
			bw_value.write(content);
			bw_value.newLine();
			
			for (int i = 0; i < population.size(); i++) {
				
				Chromosome chr = population.get(i);
				
				ArrayList<Integer> index = chr.getChomosomeIndex();
				ArrayList<Integer> index_copy = new ArrayList<Integer>();
				for (int j = 0; j < index.size(); j++) {
					int a = index.get(j)+1;
					index_copy.add(a);
				}
				content = Arrays.toString(index_copy.toArray());
				content = content.replace(",","|");
				content = content + "," + chr.getSensitivity()+","+chr.getSpecificity()+",";
				double[] value = chr.getCutOff();
				double[] cut_off = new double[index.size()];
				for (int j = 0; j < cut_off.length; j++) {
					cut_off[j] = value[index.get(j)];
				}
				String cut = Arrays.toString(cut_off);
				cut = cut.replace(",","|");
				content = content+cut+","+chr.getRank();
				
				bw_value.write(content);
				bw_value.newLine();
			}
			bw_value.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void excuteNSGAIIResults(int front_size, double time, String folder_name, String iterations, ArrayList<Chromosome> population, ArrayList<Antigen> antigen_info, Settings set) {
		String pre_name = folder_name+iterations+"_"+set.getExperimentID()+"_"+set.getPanelSize()+"_"+set.getThreshold()+".csv";
		
		File file = new File(folder_name);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		try {
			BufferedWriter bw_value = new BufferedWriter(new FileWriter(pre_name));
			
			String content = "Experiment ID, Experiment Description, ";
			for (int i = 0; i < set.getMaxPanelSize(); i++) {
				int index = i+1;
				content = content+"Antigen "+index+", ";
			}
			
			for (int i = 0; i < set.getMaxPanelSize(); i++) {
				int index = i+1;
				content = content+"Cutoff "+index+", ";
			}
			content = content+"Sensitivity, Specificity, Panel Size, Threshold, Dataset Name";
			bw_value.write(content);
			bw_value.newLine();
			
			System.out.println(front_size);
			
			ArrayList<Chromosome> saved_chr = new ArrayList<Chromosome>();
			for (int i = 0; i < front_size; i++) {
				
				if (i > population.size()-1) {
					break;
				}
				
				Chromosome chr = new Chromosome(population.get(i).getChomosome().length);
				chr = population.get(i);
				if (SavedChromosome(chr, saved_chr)) {
					continue;
				} else {
					
					content = set.getExperimentID()+","+ set.getExperimentDescription()+",";
					ArrayList<Integer> index = chr.getChomosomeIndex();
					for (int j = 0; j < index.size(); j++) {
						content = content+antigen_info.get(index.get(j)).getAntigenName()+",";
					}
					content = fillEmpty(index.size(), set.getMaxPanelSize(), content);
					
					double[] cut = chr.getCutOff();
					for (int j = 0; j < index.size(); j++) {
						content = content+cut[index.get(j)]+",";
					}
					content = fillEmpty(index.size(), set.getMaxPanelSize(), content);
					
					content = content + chr.getSensitivity()+","+chr.getSpecificity()+","+set.getPanelSize()+","+set.getThreshold()+","+set.getDatasetName();
					
					bw_value.write(content);
					bw_value.newLine();
					
					saved_chr.add(chr);
				}
			}
			bw_value.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public boolean SavedChromosome(Chromosome chr, ArrayList<Chromosome> saved) {
	
			for (int i = 0; i < saved.size(); i++) {
				Chromosome temp = saved.get(i);
				if (Arrays.equals(chr.getChomosome(), temp.getChomosome()) && chr.getSensitivity() == temp.getSensitivity() && chr.getSpecificity() == temp.getSpecificity()) {
					return true;
				}
			}
		
		return false;
	}
	
	public String fillEmpty(int panel_size, int max_panel_size, String content) {
		
		if (panel_size < max_panel_size) {
			int diff = max_panel_size - panel_size;
			while (diff > 0) {
				content = content + ",";
				diff--;
			}
		}
		
		return content;
		
	}
	
	public void excuteGAResults(String folder_name, ArrayList<double[]> best_values, double time) {
		String pre_name = folder_name+"paretofront.csv";
		
		File file = new File(folder_name);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		try {
			BufferedWriter bw_value = new BufferedWriter(new FileWriter(pre_name));
			
			String content = "The running time is, " + time;
			bw_value.write(content);
			bw_value.newLine();
			
			content = "Sensitivity, Specificity";
			bw_value.write(content);
			bw_value.newLine();
			
			for (int i = 0; i < best_values.size(); i++) {
				content = best_values.get(i)[0]+","+best_values.get(i)[1];
				bw_value.write(content);
				bw_value.newLine();
			}
			bw_value.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
