package project.start;

import project.files.*;
import project.data.*;
import java.util.*;

public class DataModify {

	DataModify() {}
	
	public void process(String input_file) {
		
		//String[] antigen_name = {"10P DDX48 (EIF4A3)","18P/s IGF2BP1","29P PEAK1","6P/s CEACAM5","8P/s CTAG1A (NY-ESO-1)","42P TIMP1"};
		//String[] antigen_name = {"16P ICAM-1/CD54",	"18P/s IGF2BP1","24P/s MSLN","2P/s Annexin A1",	"38P/s SQSTM1",	"6P/s CEACAM5"};
		// String[] antigen_name = {"17P IGF1R","2P/s Annexin A1",	"30P PGK1",	"38P/s SQSTM1",	"6P/s CEACAM5",	"C43/s MUC16"};
		//String[] antigen_name = {"18P/s IGF2BP1","21P/s K-Ras",	"28P/s p53","31P Prostate Stem Cell Antigen","33P/s RAF1","42P TIMP1","48P/s VEGFC","6P/s CEACAM5"};
		//String[] antigen_name = {"13P EZR",	"16P ICAM-1/CD54",	"19P IGFBP4",	"22P MAPK9",	"27P/s p16",	"2P/s Annexin A1",	"42P TIMP1",	"48P/s VEGFC",	"6P/s CEACAM5",	"7P CEACAM-1/CD66a"};
		String[] antigen_name = {"10P DDX48 (EIF4A3)","12P/s ENOA1","13P EZR","16P ICAM-1/CD54","17P IGF1R","1P ALCAM","2P/s Annexin A1","31P Prostate Stem Cell Antigen","42P TIMP1","5P/s Calreticulin (CALR)","6P/s CEACAM5","7P CEACAM-1/CD66a"};
		

		


		//double[] antigen_value = {12.5,46,476,3.5,40.5,1.5};
		//double[] antigen_value = {9.5,	82.5,	6.5,	12.5,	226.5,	3.5};
		//double[] antigen_value = {20.5,14,218.5,223.5,3.5,1.5};
		//double[] antigen_value = {44,	162,	210,	65,	4382,	1.5,	250.5,	6.5};
		//double[] antigen_value = {29,	6.5,	234,	10436.5,	46,	20.5,	2.5,	16.5,	3.5,	9};
		double[] antigen_value = {13.5,	3.5,	64,	3.5,	161,	17.5,	11.5,	22.5,	1.5,	197,	12.5,	10.5};


		//int threshold = 1;
		//int threshold = 3;
		//int threshold = 3;
		//int threshold = 1;
		//int threshold = 3;
		int threshold = 3;
		
		ReadFile rf = new ReadFile();
		rf.read(input_file);
		
		ArrayList<Patient> patient_information = new ArrayList<Patient>();
		patient_information = rf.getPatientInformation();
		
		ArrayList<Antigen> antigen_information = new ArrayList<Antigen>();
		antigen_information = rf.getAntigenInformation();
		
		int number_cancer = 0;
		int number_cancer_positive = 0;
		int number_stage_1 = 0;
		int number_stage_2 = 0;
		int number_stage_3 = 0;
		int number_stage_4 = 0;
		int number_stage_1_positive = 0;
		int number_stage_2_positive = 0;
		int number_stage_3_positive = 0;
		int number_stage_4_positive = 0;
		int number_stage_12 = 0;
		int number_stage_12_positive = 0;
		int number_stage_34 = 0;
		int number_stage_34_positive = 0;
		int number_control = 0;
		int number_control_positive = 0;
		
		for (int i = 0; i < patient_information.size(); i++) {
			Patient pat = patient_information.get(i);
			double[] patient_antigen_values = pat.getAntigenValues();
			int number_patient_threshold = 0;
			if (pat.isCancer()) {
				if (pat.getPatientStage().equals("IA") || pat.getPatientStage().equals("IB")|| pat.getPatientStage().equals("IIA") || pat.getPatientStage().equals("IIB")) {
					number_patient_threshold = getThresholdValue(antigen_name, antigen_value, patient_antigen_values, antigen_information);
					if (number_patient_threshold >= threshold) {
						
						if (pat.getPatientStage().equals("IA") || pat.getPatientStage().equals("IB")) {
							number_stage_1_positive++;
						}
						
						if (pat.getPatientStage().equals("IIA") || pat.getPatientStage().equals("IIB")) {
							number_stage_2_positive++;
						}
						
						number_stage_12_positive++;
						number_cancer_positive++;
					}
					
					if (pat.getPatientStage().equals("IA") || pat.getPatientStage().equals("IB")) {
						number_stage_1++;
					}
					
					if (pat.getPatientStage().equals("IIA") || pat.getPatientStage().equals("IIB")) {
						number_stage_2++;
					}
					
					number_stage_12++;
				} else {
					number_patient_threshold = getThresholdValue(antigen_name, antigen_value, patient_antigen_values, antigen_information);
					if (number_patient_threshold >= threshold) {
						number_stage_34_positive++;
						
						if (pat.getPatientStage().equals("III")) {
							number_stage_3_positive++;
							number_cancer_positive++;
						}
						
						if (pat.getPatientStage().equals("IV")) {
							number_stage_4_positive++;
							number_cancer_positive++;
						}
						
					}
					
					if (pat.getPatientStage().equals("III")) {
						number_stage_3++;
					}
					
					if (pat.getPatientStage().equals("IV")) {
						number_stage_4++;
					}
					
					number_stage_34++;
				}
				number_cancer++;
			} else {
				number_patient_threshold = getThresholdValue(antigen_name, antigen_value, patient_antigen_values, antigen_information);
				if (number_patient_threshold < threshold) {
					number_control_positive++;
				}
				
				number_control++;
			}
		}
		
		System.out.println("Number of PC: " + number_cancer);
		System.out.println("Number of PC with positive test: " + number_cancer_positive);
		
		System.out.println("Number of Control: " + number_control);
		System.out.println("Number of Control with positive test: " + number_control_positive);

		System.out.println("Number of Stage 1: " + number_stage_1);
		System.out.println("Number of Stage 1 with positive test: " + number_stage_1_positive);
		double sensitivity = number_stage_1_positive*1.0/number_stage_1;
		System.out.println("Sensitivity of stage 1 is: " + sensitivity);
		
		System.out.println("Number of Stage 2: " + number_stage_2);
		System.out.println("Number of Stage 2 with positive test: " + number_stage_2_positive);
		sensitivity = number_stage_2_positive*1.0/number_stage_2;
		System.out.println("Sensitivity of stage 2 is: " + sensitivity);
		
		
		System.out.println("Number of Stage 3: " + number_stage_3);
		System.out.println("Number of Stage 3 with positive test: " + number_stage_3_positive);
		sensitivity = number_stage_3_positive*1.0/number_stage_3*1.0;
		System.out.println("Sensitivity of stage 3 is: " + sensitivity);
		
		System.out.println("Number of Stage 4: " + number_stage_4);
		System.out.println("Number of Stage 4 with positive test: " + number_stage_4_positive);
		sensitivity = number_stage_4_positive*1.0/number_stage_4*1.0;
		System.out.println("Sensitivity of stage 4 is: " + sensitivity);
		
		System.out.println("Number of Stage 1 & 2: " + number_stage_12);
		System.out.println("Number of Stage 1 & 2 with positive test: " + number_stage_12_positive);
		sensitivity = number_stage_12_positive*1.0/number_stage_12*1.0;
		System.out.println("Sensitivity of stage 1 & 2 is: " + sensitivity);
		
		System.out.println("Number of Stage 3 & 4: " + number_stage_34);
		System.out.println("Number of Stage 3 & 4 with positive test: " + number_stage_34_positive);
		sensitivity = number_stage_34_positive*1.0/number_stage_34*1.0;
		System.out.println("Sensitivity of stage 3 & 4 is: " + sensitivity);
	}
	
	public int getThresholdValue(String[] antigen_name, double[] antigen_value, double[] patient_antigen_values, ArrayList<Antigen> antigen_information) {
		int number_threshold = 0;
		
		for (int j = 0; j < antigen_value.length; j++) {
			int index = getAntigenNameIndex(antigen_name[j], antigen_information);
			if (patient_antigen_values[index] >= antigen_value[j]) {
				number_threshold++;
			}
		}
		
		return number_threshold;
	}
	
	public int getAntigenNameIndex(String antigen_name, ArrayList<Antigen> antigen_info) {
		int index = -1;
		for (int i = 0; i < antigen_info.size(); i++) {
			if (antigen_info.get(i).getAntigenName().equals(antigen_name)) {
				return i;
			}
		}
		return index;
	}
}
