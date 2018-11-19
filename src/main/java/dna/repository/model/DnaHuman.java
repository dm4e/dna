package dna.repository.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "DNA_HUMAN")
public class DnaHuman  {

	private String dna;

	public static DnaHuman create(String dna) {
		final DnaHuman result= new DnaHuman();
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