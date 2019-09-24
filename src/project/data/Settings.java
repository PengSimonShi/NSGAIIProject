package project.data;

public class Settings {

	// Antigen Settings
    private int panel_size;
    private int threshold;
    
    // Target Settings
    private double sens_goal;
    private double spec_goal;
    private int random_seed;
    private boolean generate_paretofront;
    
    // Genetic Algorithm Settings
    private int population_size;
    private int ga_iterations;
    private double cr;
    private double mr;
    
    // Harmony Search Settings
    private int harmony_size;
    private int hs_iterations;
    private double hscr;
    private double par;
    private double neighbour_gap;
    
    // File information
    private int experiment_id;
    String experiment_description;
    String dataset_name;
    int max_panel_size;
	
	public Settings() {}
	
	public Settings(int panel_size, int threshold) {
		this.panel_size = panel_size;
		this.threshold = threshold;
	}

	public Settings(int panel_size, int threshold, double sens_goal, double spec_goal) {
		this.panel_size = panel_size;
		this.threshold = threshold;
		this.sens_goal = sens_goal;
		this.spec_goal = spec_goal;
	}
	
	public void generateSettings(int experiment_id, int max_panel_size, String experiment_description, String dataset_name) {
		this.sens_goal = 1.0;
		this.spec_goal = 1.0;
		this.random_seed = 11;
		
		this.population_size = 200;
		this.ga_iterations = 200;
		this.cr = 0.5;
		this.mr = 0.05;
		
		this.harmony_size = 100;
		this.hs_iterations = 1000;
		this.hscr = 0.7;
		this.par = 0.15;
		this.neighbour_gap = 0.05;
		
		this.experiment_id = experiment_id;
		this.max_panel_size = max_panel_size;
		this.experiment_description = experiment_description;
		this.dataset_name = dataset_name;
	}
	
    public void AntigenSettings(int panel_size, int threshold) {
        this.panel_size = panel_size;
        this.threshold = threshold;
    }
    
    public void TargeSettings(double sens_goal, double spec_goal, int random_seed, boolean generate_paretofront) {
        this.sens_goal = sens_goal;
        this.spec_goal = spec_goal;
        this.random_seed = random_seed;
        this.generate_paretofront = generate_paretofront;
    }
    
    public void GASettings(int population_size, int ga_iterations, double cr, double mr) {
        this.population_size = population_size;
        this.ga_iterations = ga_iterations;
        this.cr = cr;
        this.mr = mr;
    }
    
    public void HSSettings(int harmony_size, int hs_iterations, double hscr, double par, double neighbour_gap) {
        this.harmony_size = harmony_size;
        this.hs_iterations = hs_iterations;
        this.hscr = hscr;
        this.par = par;
        this.neighbour_gap = neighbour_gap;
    }
    
    public int getPanelSize() {
        return this.panel_size;
    }

    public void setPanelSize(int panel_size) {
        this.panel_size = panel_size;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public double getSensGoal() {
        return this.sens_goal;
    }

    public void setSensGoal(double sens_goal) {
        this.sens_goal = sens_goal;
    }

    public double getSpecGoal() {
        return this.spec_goal;
    }

    public void setSpecGoal(double spec_goal) {
        this.spec_goal = spec_goal;
    }

    public int getRandomSeed() {
        return this.random_seed;
    }
    
    public void setRandomSeed(int random_seed) {
        this.random_seed = random_seed;
    }
    
    public boolean isGenerateParetoFront() {
        return this.generate_paretofront;
    }

    public void setGenerateParetoFront(boolean generate_paretofront) {
        this.generate_paretofront = generate_paretofront;
    }

    public int getPopulationSize() {
        return this.population_size;
    }

    public void setPopulationSize(int population_size) {
        this.population_size = population_size;
    }

    public int getGaIterations() {
        return this.ga_iterations;
    }

    public void setGaIterations(int ga_iterations) {
        this.ga_iterations = ga_iterations;
    }

    public double getCrossoverRate() {
        return this.cr;
    }

    public void setCrossoverRate(double cr) {
        this.cr = cr;
    }

    public double getMutationRate() {
        return this.mr;
    }

    public void setMutationRate(double mr) {
        this.mr = mr;
    }

    public int getHarmonySize() {
        return this.harmony_size;
    }

    public void setHarmonySize(int harmony_size) {
        this.harmony_size = harmony_size;
    }

    public int getHsIterations() {
        return this.hs_iterations;
    }

    public void setHsIterations(int hs_iterations) {
        this.hs_iterations = hs_iterations;
    }

    public double getHscr() {
        return this.hscr;
    }

    public void setHscr(double hscr) {
        this.hscr = hscr;
    }

    public double getPar() {
        return this.par;
    }

    public void setPar(double par) {
        this.par = par;
    }

    public double getNeighbourGap() {
        return this.neighbour_gap;
    }

    public void setNeighbourGap(double neighbour_gap) {
        this.neighbour_gap = neighbour_gap;
    }
    
    public int getExperimentID() {
    	return this.experiment_id;
    }
    
    public int getMaxPanelSize() {
    	return this.max_panel_size;
    }
    
    public String getExperimentDescription() {
    	return this.experiment_description;
    }
    
    public String getDatasetName() {
    	return this.dataset_name;
    }
    
    public void set(Settings s) {
        AntigenSettings(s.getPanelSize(), s.getThreshold());
        TargeSettings(s.getSensGoal(), s.getSpecGoal(), s.getRandomSeed(), s.isGenerateParetoFront());
        GASettings(s.getPopulationSize(), s.getGaIterations(), s.getCrossoverRate(), s.getMutationRate());
        HSSettings(s.getHarmonySize(), s.getHsIterations(), s.getHscr(), s.getPar(), s.getNeighbourGap());
    }
	
}
