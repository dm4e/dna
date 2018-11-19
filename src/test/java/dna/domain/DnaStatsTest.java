package dna.domain;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import dna.model.DnaStatsDTO;
import dna.repository.dao.DnaHumanDao;
import dna.repository.dao.DnaMutantDao;
import dna.repository.model.DnaHuman;
import dna.repository.model.DnaMutant;

@Test
public class DnaStatsTest {

	@InjectMocks
	private DnaStats target;
	
	@Mock
	private DnaMutantDao dnaMutantDao;
	
	@Mock
	private DnaHumanDao dnaHumanDao;
	
	@BeforeTest
    public void setUp() {
        MockitoAnnotations.initMocks(this);
	}
	
	@BeforeMethod
    public void reset() {
        Mockito.reset(this.dnaMutantDao);
        Mockito.reset(this.dnaHumanDao);
    }
	
	@DataProvider
    public Object[][] computeRatio_dataProvider() {
        //@formatter:off
        return new Object[][]{
        	{40, 100, 0.4D},
        	{0, 0, null},
        	{222, 0, null},
        	{0, 111, 0.0D},
        	{222, 0, null},
        	{2222, 33, 67.33333333333333D},
        	{33, 1111, 0.0297029702970297D}
        };
        //@formatter:on
    }

	@Test(dataProvider = "computeRatio_dataProvider")
	public void computeRatio(long mutant, long human, Double expectedResult) {
		final Double result = target.computeRatio(mutant, human);
		Assert.assertEquals(result, expectedResult);
	}
	
	public void addDna_human() {
		target.addDna(new String[] {"ABC", "123", "XYZ"}, false);
		
		final ArgumentCaptor<DnaHuman> argumentCaptor = ArgumentCaptor.forClass(DnaHuman.class);
		Mockito.verify(this.dnaHumanDao).save(argumentCaptor.capture());
		Assert.assertEquals(argumentCaptor.getValue().getDna(), "ABC,123,XYZ");
		Mockito.verifyNoMoreInteractions(this.dnaHumanDao, this.dnaMutantDao);
	}
	
	public void addDna_mutant() {
		target.addDna(new String[] {"ABC", "123", "XYZ"}, true);		
		final ArgumentCaptor<DnaMutant> argumentCaptor = ArgumentCaptor.forClass(DnaMutant.class);
		Mockito.verify(this.dnaMutantDao).save(argumentCaptor.capture());
		Assert.assertEquals(argumentCaptor.getValue().getDna(), "ABC,123,XYZ");
		Mockito.verifyNoMoreInteractions(this.dnaHumanDao, this.dnaMutantDao);
	}
	
	public void getStats() {
		Mockito.when(this.dnaHumanDao.count()).thenReturn(3l);
		Mockito.when(this.dnaMutantDao.count()).thenReturn(2l);
		
		final DnaStatsDTO result = target.getStats();
		
		Assert.assertEquals(result.getCountHumanDna(), 3l);
		Assert.assertEquals(result.getCountMutantDna(), 2l);
		Assert.assertEquals(result.getRatio(), 0.6666666666666666D);
		Assert.assertEquals(result.toString(), "DnaStatsDTO [countMutantDna=2, countHumanDna=3, ratio=0.6666666666666666]");
		
		Mockito.verify(this.dnaHumanDao).count();
		Mockito.verify(this.dnaMutantDao).count();
		Mockito.verifyNoMoreInteractions(this.dnaHumanDao, this.dnaMutantDao);
	}
	
}

