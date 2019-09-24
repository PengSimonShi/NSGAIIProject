package project.files;

import project.data.*;

import java.io.*;
import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class ReadFile {
	
	private ArrayList<Patient> patient_info = null;
	private ArrayList<Antigen> antigen_info = null;
	
	public ReadFile() {
		this.patient_info = new ArrayList<Patient>();
		this.antigen_info = new ArrayList<Antigen>();
	}
	
	/*public void read(String file_name) {
		String information = null;
		int line_number = 0;
		int number_antigens = 0;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(file_name));
			while ((information = br.readLine()) != null) {
				if (!information.trim().equals("")) {
					String[] info = information.split(",");
					if (line_number == 0) {
						number_antigens = info.length-1;
						for (int i = 0; i < number_antigens; i++) {
							Antigen ant = new  Antigen(info[i], i);
							this.antigen_info.add(ant);
						}
						
					} else {
						Patient pa = new Patient(line_number-1, info);
						this.patient_info.add(pa);
						for (int i = 0; i < info.length-1; i++) {
							this.antigen_info.get(i).update(Double.parseDouble(info[i]));
						}
					}
					
					line_number++;
				}
			}
			
			br.close();
			
		} catch(IOException ioe) {
			System.err.println("Scenario File Reading Error!!!!!");
			System.exit(-1);
		}
	}*/
	
	/*
	 * Updated based on the new dataset
	 */
	public void read(String file_name) {
		String information = null;
		int line_number = 0;
		int number_antigens = 21;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(file_name));
			while ((information = br.readLine()) != null) {
				if (!information.trim().equals("")) {
					String[] info = information.split(",");
					if (line_number == 0) {
						// number_antigens = 49;
						for (int i = 0; i < number_antigens; i++) {
							Antigen ant = new  Antigen(info[i+7], i);
							this.antigen_info.add(ant);
						}
						
					} else {
						Patient pa = new Patient(line_number-1,number_antigens, info);
						this.patient_info.add(pa);
						for (int i = 0; i < number_antigens; i++) {
							this.antigen_info.get(i).update(Double.parseDouble(info[i+7]));
						}
					}
					
					line_number++;
				}
			}
			
			br.close();
			
		} catch(IOException ioe) {
			System.err.println("Scenario File Reading Error!!!!!");
			System.exit(-1);
		}
	}
	
	public ArrayList<String[]> getFileInformation(String file_name) {
		String information = null;
		ArrayList<String[]> file_information = new ArrayList<String[]>();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(file_name));
			while ((information = br.readLine()) != null) {
				if (!information.trim().equals("")) {
					String[] info = information.split(",");
					file_information.add(info);
				}
			}
			
			br.close();
			
		} catch(IOException ioe) {
			System.err.println("Scenario File Reading Error!!!!!");
			System.exit(-1);
		}
		
		return file_information;
	}
	
	public ArrayList<Antigen> getAntigenInformation() {
		for (int i = 0; i < this.antigen_info.size(); i++) {
			this.antigen_info.get(i).PreScanning();
		}
		return this.antigen_info;
	}
	
	public ArrayList<Patient> getPatientInformation() {
		return this.patient_info;
	}
	
}
