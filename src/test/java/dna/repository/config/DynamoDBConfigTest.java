package dna.repository.config;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

@Test
public class DynamoDBConfigTest {

	@InjectMocks
	private DynamoDBConfig target;
	
	@BeforeTest
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(target, "amazonAWSAccessKey",  "a1");
        ReflectionTestUtils.setField(target, "amazonAWSSecretKey", "k2");
	}
	
	
	public void amazonDynamoDB() {
		final AWSCredentials amazonAWSCredentials = new BasicAWSCredentials("key", "secret");
		final AmazonDynamoDB result = target.amazonDynamoDB(amazonAWSCredentials );
		Assert.assertNotNull(result);
	
	}
	
	public void amazonAWSCredentials() {
		final AWSCredentials result = target.amazonAWSCredentials();
		Assert.assertEquals(result.getAWSAccessKeyId(), "a1");
		Assert.assertEquals(result.getAWSSecretKey(), "k2");
	}
}
