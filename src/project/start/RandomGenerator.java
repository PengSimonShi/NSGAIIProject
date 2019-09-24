package project.start;

import java.util.*;

/*
 * @author Peng Shi
 * @version 1.0
 * @since 2018-12-20
 */

public class RandomGenerator {

	private Random rnd = null;
	
	public RandomGenerator(int random_seed) {
		this.rnd = new Random(random_seed);
	}
	
	public double getRandomValue(double min, double max) {
		double value = 0.0;
		value = this.rnd.nextDouble()*(max - min)+min;
		return value;
	}
	
	public int getRandomIndex(int size) {
		int index = -1;
		index = (int) Math.round(this.rnd.nextDouble()*size);
		if (index == size)
			index--;
		return index;
	}
	
	
}
