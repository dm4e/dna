package dna.domain.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomStringUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import dna.domain.impl.ParallelDnaAnalyzer;
import dna.model.DiagonalDirection;

@Test
public class ParallelDnaAnalyzerTest {

	@InjectMocks
	private ParallelDnaAnalyzer target;
	
	@BeforeTest
    public void setUp() {
        MockitoAnnotations.initMocks(this);
	}
	
	@DataProvider
    public Object[][] findMutantSequence_dataProvider() {
        //@formatter:off
        return new Object[][]{
        	{null, 0},
        	{"", 0},
        	{"AAFAA", 0},
            {"AAAA", 1},
            {"TTTT", 1},
            {"CCCC", 1},
            {"GGGG", 1},
            {"FAAAAAF", 1},
            {"AAAAGGGGAAAACCCC", 2}
        };
        //@formatter:on
    }
	
	@Test(dataProvider = "findMutantSequence_dataProvider")
	public void findMutantSequence(String line, int expectedResult) {
		final AtomicInteger mutantLineCount = new AtomicInteger(0);
		target.findMutantSequence(line, mutantLineCount);
		Assert.assertEquals(mutantLineCount.get(), expectedResult);
	}
	
	public void obtainColumLines_ok() {
		final String[] matrix = new String[]{"12345T","12345T","12345T","12345T","123456","123456"};
		final String result =target.obtainColumLines(matrix);
		Assert.assertEquals(result,"111111,222222,333333,444444,555555,TTTT66,");
	}
	
	public void findRows() {
		final String[] matrix = new String[]{"123456","123456","12AAAA","123456","123456","123456"};
		final AtomicInteger mutantLineCount = new AtomicInteger(0);
		target.findRows(matrix, mutantLineCount);
		Assert.assertEquals(mutantLineCount.get(),1);
	}
	
	@Test(invocationTimeOut=10000)
	public void isMutant_largeMatrix_checkTime() {
		final int size=10000;
		final String[] matrix = new String[size];
		for(int i=0; i< size; i++) {
			matrix[i]= RandomStringUtils.random(size, "ATCG");
		}
		target.isMutant(matrix);
	}
	
	public void obtainDiagonalLines_top2Left() {
		final String[] matrix = new String[]{"123",
											 "456",
											 "789"};
		final String result =target.obtainDiagonalLines(matrix, DiagonalDirection.TOP2LEFT);
		Assert.assertEquals(result,"1,24,357,68,9,");
	}
	
	public void obtainDiagonalLines_bottom2left() {
		final String[] matrix = new String[]{"123",
											 "456",
											 "789"};
		final String result =target.obtainDiagonalLines(matrix, DiagonalDirection.BOTTOM2LEFT);
		Assert.assertEquals(result,"7,84,951,62,3,");
	}
	
	public void obtainDiagonalLines_bottom2Left_6x6() {
		final String[] matrix = new String[]{"12345T",
				                             "12345T",
				                             "12345T",
				                             "12345T",
				                             "123456",
				                             "123456"};
		final String result =target.obtainDiagonalLines(matrix, DiagonalDirection.BOTTOM2LEFT);
		Assert.assertEquals(result,"1,21,321,4321,54321,654321,65432,T543,T54,T5,T,");
	}

	public void isMutant_ok() {
		final String[] matrix = new String[]{"CCCC5T","TTTT5T","12345T","12345T","123456","123456"};
		final boolean result =target.isMutant(matrix);
		Assert.assertTrue(result);
	}

	public void isMutant_bigAdnMatrix_yield() {
		int size=1000;
		final String[] matrix = new String[size];
		for(int i=0; i< size; i++) {
			matrix[i]= RandomStringUtils.random(size-1, "1234")+"T";
		}
		final boolean result =target.isMutant(matrix);
		Assert.assertTrue(result);
	}
	
	public void isMutant_false() {
		final String[] matrix = new String[]{"12345T","12345T","12345T","12345T","123456","123456"};
		final boolean result =target.isMutant(matrix);
		Assert.assertFalse(result);
	}
}
