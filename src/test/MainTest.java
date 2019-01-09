package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Main;

class MainTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testMergeNum() {
		double[] origin= {1.0,3.0,2.0,10.0,9.0,8.0,20.0,4.0};
		double[] result=Main.mergeNum(origin,2.0);
		double[] expectedResult= {2.0,2.0,2.0,9.0,9.0,9.0,20.0,2.0};
		assertArrayEquals(expectedResult,result);
	}

}
