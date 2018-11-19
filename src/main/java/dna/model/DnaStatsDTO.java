package dna.model;

public class DnaStatsDTO {

	private final long countMutantDna;
	private final long countHumanDna;
	private final Double ratio;
	
	
	public DnaStatsDTO(long countMutantDna, long countHumanDna, Double ratio) {
		super();
		this.countMutantDna = countMutantDna;
		this.countHumanDna = countHumanDna;
		this.ratio = ratio;
	}

	public long getCountMutantDna() {
		return countMutantDna;
	}

	public long getCountHumanDna() {
		return countHumanDna;
	}

	public Double getRatio() {
		return ratio;
	}

	@Override
	public String toString() {
		return "DnaStatsDTO [countMutantDna=" + countMutantDna + ", countHumanDna=" + countHumanDna + ", ratio=" + ratio
				+ "]";
	}
	
	
}
