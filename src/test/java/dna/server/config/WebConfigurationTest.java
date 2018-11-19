package dna.server.config;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.HttpMessageConverter;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

@Test
public class WebConfigurationTest {

	@InjectMocks
	private WebConfiguration target;
	
	 @BeforeTest
	    public void initTest() {
	        MockitoAnnotations.initMocks(this);
	    }

	 
	public void configureMessageConverters() {
		final List<HttpMessageConverter<?>> converters =  Lists.newArrayList();
		this.target.configureMessageConverters(converters);
		Assert.assertEquals(converters.size(), 2);
	}
}
