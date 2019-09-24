package project.data;

import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class Antigen {

	private String antigen_name;
	private int antigen_id;
	private double max_value;
	private double min_value;
	private ArrayList<Double> antigen_values= null;
	
	// Integer or should be real?
	private ArrayList<Double> cutoff_windows = null;
	
	public Antigen(String antigen_name, int antigen_id) {
		this.antigen_name = antigen_name;
		this.antigen_id = antigen_id;
		initialise();
	}
	
	public void initialise() {
		this.max_value = Double.MIN_VALUE;
		this.min_value = Double.MAX_VALUE;
		this.antigen_values = new ArrayList<Double>();
		this.cutoff_windows = new ArrayList<Double>();
	}
	
	public void update(double value) {
		if (this.min_value > value) {
			this.min_value = value;
		}
		
		if(this.max_value < value) {
			this.max_value = value;
		}
		
		this.antigen_values.add(value);
	}

	public void PreScanning() {
		Collections.sort(this.antigen_values);
		for (int i = 0; i < this.antigen_values.size()-1; i++) {
			double recent = this.antigen_values.get(i);
			double next = this.antigen_values.get(i+1);
			if (recent != next) {
				double window = recent + (next-recent)/2;
				this.cutoff_windows.add(window);
			}
		}
	}
	
	public int getNeighbourIndex(int index, double neighbor_range) {
		int neigh_index = -1;
		int change = (int) Math.round(this.cutoff_windows.size()*neighbor_range);
		if (index + change > this.cutoff_windows.size() && index - change > -1) {
			neigh_index = index - change;
		} else if (index + change < this.cutoff_windows.size()) {
			neigh_index = index + change;
		} else {
			neigh_index = index - change;
		}
		
		if (neigh_index < 0)
			neigh_index = 0;
		
		return neigh_index;
	}
	
	public int getValueIndex(double value) {
		int index = -1;
		int neighbor_index = -1;
		for (int i = 0; i < this.cutoff_windows.size(); i++) {
			if (this.cutoff_windows.get(i) == value) {
				index = i;
				break;
			} else if(this.cutoff_windows.get(i) > value) {
				neighbor_index = i-1;
				break;
			}
		}
		
		if (index == -1) {
			if (neighbor_index == -1) {
				index = this.cutoff_windows.size()-1;
			} else {
				index = neighbor_index;
			}
		}
		
		return index;
	}
	
	public double getNeighbourValue(double value) {
		int neighbour_index = 0;
		return this.cutoff_windows.get(neighbour_index);
	}
	
	public String getAntigenName() {
		return this.antigen_name;
	}

	public void setAntigenName(String antigen_name) {
		this.antigen_name = antigen_name;
	}

	public int getAntigenId() {
		return this.antigen_id;
	}

	public void setAntigenId(int antigen_id) {
		this.antigen_id = antigen_id;
	}

	public double getMaxValue() {
		return this.max_value;
	}

	public void setMaxValue(double max_value) {
		this.max_value = max_value;
	}

	public double getMinValue() {
		return this.min_value;
	}

	public void setMinValue(double min_value) {
		this.min_value = min_value;
	}
	
	public void setAntigenValues(ArrayList<Double> antigen_values) {
		Collections.copy(this.antigen_values, antigen_values);
	}
	
	public ArrayList<Double> getAntigenValues() {
		return this.antigen_values;
	}
	
	public ArrayList<Double> getWholeCutoffWindows() {
		return this.cutoff_windows;
	}
	
	public int getCutOffSize() {
		return this.cutoff_windows.size();
	}
	
	public double getCutOffValue(int index) {
		return this.cutoff_windows.get(index);
	}
	
	@Override
	public Antigen clone() {
		Antigen cloned = null;
		try {
			cloned = (Antigen) super.clone();
		} catch (CloneNotSupportedException e) {
			
		}
		
		return cloned;
	}
}
