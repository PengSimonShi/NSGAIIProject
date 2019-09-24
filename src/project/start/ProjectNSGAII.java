package project.start;

import project.algorithm.*;
import project.data.*;
import project.files.*;

import java.util.*;
import java.io.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */


/*
 * experiments a/a1: scale is 50 in the harmony class, which the sensi_goal is 1 and spec_goal is 1.
 * 
 * experiments b: scale is 50 in the harmony class, which the sensi_goal is 0.5 and spec_goal is 1.
 * 
 * experiments c: no scale and the sensi_goal is 1 and spec_goal is 1.
 * 
 * experiments d: no scale and the sensi_goal is 0.5 and spec_goal is 1.
 */
public class ProjectNSGAII {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*testFunction();
		System.exit(1);*/
		
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\CRC BC Research\\CRC data split\\MRC_Age_above_50.csv";
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\CRC BC Research\\HCC\\HCC results.csv";
		
		String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\CRC BC Research\\MRC\\MRC Term CEAC Run 1.csv";
		
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\NewData2.csv";
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\Daniyah.csv";
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\Data_58Antigens.csv";
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\DataRepeat2.csv";
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\PilotDataDundee.csv";
		// String panel_file = "D:\\Genetic Algorithm Developer (if possible)\\data\\DundeeInput.csv";
		
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\PilotDataNY.csv";
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\PittsburgData.csv";
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\CombinedData.csv";
		// String panel_file = "D:\\Genetic Algorithm Developer (if possible)\\data\\NYInput.csv";
		
		// ReadFile rf = new ReadFile();
		//  ArrayList<String[]> panels = new ArrayList<String[]>();
		// panels = rf.getFileInformation(panel_file);
		
		// int[] panel_size = {2, 4, 5, 6, 7, 8, 9, 10, 12, 15};
		// int[] panel_size = {6, 8, 10, 12, 15};
		
		
		/*DataModify dm = new DataModify();
		dm.process(file_name);
		System.exit(0);*/
		
		int[] threshold_set = {1, 3, 5};
		int panel_size = 6;
		int random_seed = 11;
		int max_panel_size = 15;
		int experiment_id = 1;
		String experiment_description = "MRC CEAC RUN 1's full results";
		String dataset_name = "MRC Term CEAC Run 1.csv";
		for (int i = 0; i < threshold_set.length; i++) {
			
			/*if (i > panel_size) {
				break;
			}*/
			
			Settings set = new Settings(panel_size, threshold_set[i]);
			set.generateSettings(experiment_id, max_panel_size, experiment_description, dataset_name);
			
			// String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\58Antigens\\NSGAII 28052019\\PanelSize"+panel_size+"\\Threshold"+i+"\\";
			
			// String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\CRC BC Research\\HCC\\Output\\NSGAII 04092019\\PanelSize"+panel_size+"\\Threshold"+threshold_set[i]+"\\";
			// String final_result_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\CRC BC Research\\HCC\\Output\\NSGAII 04092019\\results\\";
			
			String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\CRC BC Research\\MRC\\Run1\\NSGAII 05092019 a1\\PanelSize"+panel_size+"\\Threshold"+threshold_set[i]+"\\";
			String final_result_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\CRC BC Research\\MRC\\Run1\\NSGAII 05092019 a1\\results\\";
			
			
			// String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\Daniyah\\NSGAII 28052019\\PanelSize"+panel_size+"\\Threshold"+i+"\\";
			
			// String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\NewData2\\NSGAII 28052019\\PanelSize"+panel_size+"\\Threshold"+i+"\\";
			
			ReadFile rf = new ReadFile();
			rf.read(file_name);
			
			long start_time = System.currentTimeMillis();
			Solver solver = new Solver(start_time, panel_size);
			solver.start(random_seed, file_name, 1, 1, rf, save_folder, final_result_folder, threshold_set[i], set);
			experiment_id++;
		}
		
	}
	
	public static void testFunction() {
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\Data_58Antigens.csv";
		// String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\58Antigens\\NSGAII 28052019\\results manipulate\\";
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\NewData2.csv";
		// String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\NewData2\\NSGAII 28052019\\results manipulate\\";
		
		// String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\Daniyah.csv";
		// String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\Daniyah\\NSGAII 28052019\\results manipulate\\";
		
		String file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\DataRepeat2.csv";
		String save_folder = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\DataRepeat2\\NSGAII 04062019\\results manipulate\\";
		
		int[] panel_size_set = {6, 8, 10, 12, 15};
		// int[] panel_size_set = {2, 4, 5, 6, 7, 8, 9, 10, 12, 15};
		int[] threshold_set = {1, 2, 3, 4, 5, 6, 7};
		
		File file = new File(save_folder);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		ReadFile rf = new ReadFile();
		rf.read(file_name);
		
		ArrayList<Antigen> antigen_information = new ArrayList<Antigen>();
		antigen_information = (ArrayList<Antigen>)rf.getAntigenInformation().clone();
		
		// file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\58Antigens\\NSGAII 28052019\\data.csv";
		// file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\NewData2\\NSGAII 28052019\\data.csv";
		file_name = "D:\\Genetic Algorithm Developer (if possible)\\data\\experimental results\\DataRepeat2\\NSGAII 04062019\\data.csv";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file_name));
			String information = "";
			ArrayList<String[]> information_set = new ArrayList<String[]>();
			
			ArrayList<ArrayList<ArrayList<String[]>>> information_collection = new ArrayList<ArrayList<ArrayList<String[]>>>();
			
			int panel_size = -1;
			int threshold = -1;
			String time = "";
			while ((information = br.readLine()) != null) {
				if (!information.trim().equals("")) {
					if (information.contains("*")) {
						// PrintOutInformation(information_set, panel_size, threshold, time, save_folder, antigen_information);
						if (threshold - 1 >= information_collection.size()) {
							ArrayList<ArrayList<String[]>> temp = new ArrayList<ArrayList<String[]>>();
							temp.add((ArrayList<String[]>)information_set.clone()); 
							information_collection.add(temp);
						} else {
							ArrayList<ArrayList<String[]>> temp = new ArrayList<ArrayList<String[]>>();
							temp = information_collection.get(threshold-1);
							temp.add((ArrayList<String[]>)information_set.clone());
							information_collection.remove(threshold-1);
							information_collection.add(threshold-1, temp);
						}
						information_set.clear();
					} else {
						if (information.contains("CutOff")) {
							continue;
						} else if (information.contains("panel")) {
							String[] info_set = information.split(",");
							String info = info_set[3].trim();
							panel_size = Integer.parseInt(info);
							info = info_set[5].trim();
							threshold = Integer.parseInt(info);
							time = info_set[1].trim();
						} else {
							String[] info = information.split(",");
							String[] copy = new String[info.length];
							for (int i = 0; i < info.length; i++) {
								if (i < panel_size) {
									copy[i] = info[i];
								} else if (i < panel_size+2) {
									copy[i+panel_size] = info[i];
								} else {
									copy[i-2] = info[i];
								}
							}
							information_set.add(copy);
						}
					}
				}
			}
			
			PrintOutInformation(information_collection, panel_size_set, threshold_set, save_folder, antigen_information);
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	
	public static void PrintOutInformation(ArrayList<ArrayList<ArrayList<String[]>>> data, int[] panel_size, int[] threshold_size, String save_folder, ArrayList<Antigen> antigen_info) {
		String file_name = save_folder+"result summary.csv";
		String save_info = "";
		int line_number = 0;
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file_name, true));
			
			for (int i = 0; i < data.size(); i++) {
				ArrayList<ArrayList<String[]>> temp = new ArrayList<ArrayList<String[]>>();
				temp = data.get(i);
				save_info = ""+threshold_size[i]+"ANTIGENPOSITIVEFORCANCERPOSITIVE";
				bw.write(save_info);
				bw.newLine();
				
				for (int j = 0; j < temp.size(); j++) {
					int diff = panel_size.length - temp.size();
					ArrayList<String[]> sol_information = temp.get(j);
					save_info = ", ";
					for (int k = 0; k < panel_size[j+diff]; k++) {
						int index = k+1;
						save_info = save_info+"Antigen "+index+", ";
					}
					
					for (int k = 0; k < panel_size[j+diff]; k++) {
						int index = k+1;
						save_info = save_info+"cutt off "+index+",";
					}
					
					save_info = save_info+"sensitivity, specificity";
					line_number++;
					
					bw.write(save_info);
					bw.newLine();
					
					for (int k = 0; k < sol_information.size(); k++) {
						save_info = line_number+", ";
						String[] info = new String[sol_information.get(k).length];
						info = sol_information.get(k).clone();
						
						for (int m = 0; m < info.length; m++) {
							if (m < panel_size[j+diff]) {
								String in = info[m].trim();
								int index = Integer.parseInt(in);
								save_info = save_info + antigen_info.get(index-1).getAntigenName()+", ";
							} else {
								// System.out.println("panel size is "+panel_size+" and the related threshold is "+threshold);
								String in = info[m].trim();
								save_info = save_info + in + ", ";
							}
						}
						
						bw.write(save_info);
						bw.newLine();
						line_number++;
					}
					line_number = 0;
					
					bw.write(" ");
					bw.newLine();
					
				}
				line_number = 0;
				bw.write(" ");
				bw.newLine();
			}
			
			bw.close();
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	
	public static void PrintOutInformation(ArrayList<String[]> data, int panel_size, int threshold, String time, String save_folder, ArrayList<Antigen> antigen_info) {
		String file_name = save_folder+"NewData_"+panel_size+"_"+threshold+".csv";
		// String file_name = save_folder+"result summary.csv";
		String save_info = "";
		int line_number = 0;
		
		try {
			// BufferedWriter bw = new BufferedWriter(new FileWriter(file_name, true));
			BufferedWriter bw = new BufferedWriter(new FileWriter(file_name));
			
			save_info = ", ";
			for (int i = 0; i < panel_size; i++) {
				int index = i+1;
				save_info = save_info+"Antigen "+index+", ";
			}
			
			for (int i = 0; i < panel_size; i++) {
				int index = i+1;
				save_info = save_info+"cutt off "+index+",";
			}
			
			save_info = save_info+"sensitivity, specificity";
			line_number++;
			
			bw.write(save_info);
			bw.newLine();
			
			for (int i = 0; i < data.size(); i++) {
				save_info = line_number+", ";
				String[] info = new String[data.get(i).length];
				info = data.get(i).clone();
				
				for (int j = 0; j < info.length; j++) {
					if (j < panel_size) {
						String in = info[j].trim();
						int index = Integer.parseInt(in);
						save_info = save_info + antigen_info.get(index-1).getAntigenName()+", ";
					} else {
						// System.out.println("panel size is "+panel_size+" and the related threshold is "+threshold);
						String in = info[j].trim();
						save_info = save_info + in + ", ";
					}
				}
				
				bw.write(save_info);
				bw.newLine();
				line_number++;
			}
			
			bw.write(" ");
			bw.newLine();
			
			bw.close();
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
}
