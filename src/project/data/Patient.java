package project.data;

import java.util.Arrays;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class Patient {

	private int patient_id = 0;
	private String patient_name = "";
	private int antigen_length;
	private boolean isCancer;
	private double[] antigen_values = null;
	
	// optional information
	private boolean necessary = false;
	private int age = -1;
	private String stage = "";
	private String gender = "";
	private String set = "";
	
	public Patient() {}
	
	public Patient(int antigen_length) {
		this.antigen_length = antigen_length;
		this.antigen_values = new double[antigen_length];
		this.isCancer = false;
	}
	
	public Patient(int patient_id, int antigen_length, boolean isCancer, double[] antigen_values) {
		this.patient_id = patient_id;
		this.antigen_length = antigen_length;
		this.isCancer = isCancer;
		this.antigen_values = new double[antigen_length];
		for (int i = 0; i < antigen_length; i++) {
			this.antigen_values[i] = antigen_values[i];
		}
	}
	
	public Patient(int patient_id, String[] information) {
		this.patient_id = patient_id;
		int information_length = information.length;
		this.antigen_length = information_length-1;
		this.isCancer = (Integer.parseInt(information[information_length-1]) == 0)?false:true;
		this.antigen_values = new double[this.antigen_length];
		for(int i = 0; i < this.antigen_length; i++) {
			this.antigen_values[i] = Double.parseDouble(information[i]);
		}
	}

	public Patient(int patient_id, int antigen_length, String[] information) {
		this.patient_id = patient_id;
		this.patient_name = information[0];
		this.antigen_length = antigen_length;

		if (information[2].contains("Cancer")) {
			this.isCancer = true;
		} else if (information[2].contains("Control")){
			this.isCancer = false;
		}
		
		this.antigen_values = new double[this.antigen_length];
		for(int i = 0; i < this.antigen_length; i++) {
			this.antigen_values[i] = Double.parseDouble(information[i+7]);
		}
		
		if (this.necessary && this.isCancer) {
			this.age = Integer.parseInt(information[5]);
			this.gender = information[6];
			this.stage = information[4];
		}
	}
	
	public int getPatientID() {
		return this.patient_id;
	}
	
	public int getAntigenLength() {
		return this.antigen_length;
	}

	public void setAntigenLength(int antigen_length) {
		this.antigen_length = antigen_length;
	}

	public boolean isCancer() {
		return this.isCancer;
	}

	public void setCancer(boolean isCancer) {
		this.isCancer = isCancer;
	}
	
	public double getAntigenValue(int index) {
		return this.antigen_values[index];
	}
	
	public double[] getAntigenValues() {
		return this.antigen_values.clone();
	}

	public void setAntigenValues(double[] antigen_values) {
		int length = antigen_values.length;
		this.antigen_values = new double[length];
		for (int i = 0; i < length; i++) {
			this.antigen_values[i] = antigen_values[i];
		}
	}
	
	public void fixAntigenValue(int index, double value) {
		this.antigen_values[index] = value;
	}
	
	public int getPatientAge() {
		return this.age;
	}
	
	public String getPatientStage() {
		return this.stage;
	}
	
	public String getPatientGender() {
		return this.gender;
	}
	
	public void printOnScreeen() {
		System.out.println(this.patient_id + ": "+ Arrays.toString(this.antigen_values));
		System.out.println("The cancer class of this person is "+this.isCancer);
	}
	
	@Override
	public Patient clone() {
		Patient cloned = null;
		try {
			cloned = (Patient) super.clone();
		} catch (CloneNotSupportedException e) {
			
		}
		
		return cloned;
	}
}
