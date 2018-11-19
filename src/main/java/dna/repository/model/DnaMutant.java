package dna.repository.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "DNA_MUTANT")
public class DnaMutant  {

	private String dna;

	public static DnaMutant create(String dna) {
		final DnaMutant result= new DnaMutant();
		result.setDna(dna);
		return result;
	}

	@DynamoDBHashKey(attributeName="id")
	public String getDna() {
		return dna;
	}

	public void setDna(String dna) {
		this.dna = dna;
	}
}