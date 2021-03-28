package primahackathon;

public class AverageGunHeadingChange {
	
	public static final int NB_CHANGES_AVG = 50;
	private boolean hasChangedAllOnce;
	private double[] lastFiftyChanges;
	private int indexToChange;
	
	public AverageGunHeadingChange() {
		lastFiftyChanges = new double[NB_CHANGES_AVG];
		indexToChange = 0;
		hasChangedAllOnce = false;
	}
	
	public void addNewChangeHeading(double valueChange) {
		lastFiftyChanges[indexToChange] = valueChange;
		indexToChange++;
		if(indexToChange == NB_CHANGES_AVG) {
			hasChangedAllOnce = true;
		}
		indexToChange%=NB_CHANGES_AVG;
	}
	
	public double getTotalChangeHeading() {
		double averageHeadingChange = 0;
		for(int i=0;i<NB_CHANGES_AVG;i++) {
			averageHeadingChange+=Math.abs(lastFiftyChanges[i]);
		}
		
		return averageHeadingChange;
	}
	
	public double getAverageChangeHeading() {
		double averageHeadingChange = 0;
		for(int i=0;i<NB_CHANGES_AVG;i++) {
			averageHeadingChange+=Math.abs(lastFiftyChanges[i]);
		}
		
		return averageHeadingChange/NB_CHANGES_AVG;
	}
	
	public boolean hasChangedAllValuesList() {
		return hasChangedAllOnce;
	}
}
