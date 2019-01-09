package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import igeo.IVec;
import main.*;

class MainTest {
	private double tolerance=0.000001d;

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
	
	@Test
	void testDist() {
		Node a=new Node("",1d,0d,0d);
		Node b=new Node("",10d,0d,0d);
		Node p=new Node("",3d,4d,0d);
		Edge edge=new Edge(a,b);
		double result=p.dist(edge);
		double expectedResult=4d;
		assertEquals(expectedResult,result,tolerance);
		
	}
	
	@Test
	void testIsOnEdge() {
		Node a=new Node("",1d,2d,0d);
		Node b=new Node("",10d,20d,0d);
		Node p=new Node("",4d,8d,0d);
		Edge edge=new Edge(a,b);
		boolean result=p.isOnEdge(edge);
		boolean expectedResult=true;
		assertEquals(expectedResult,result);
		
	}
	
	@Test
	void testHasId() {
		Node a=new Node(new IVec(1d,2d,3d));
		boolean result=a.hasId();
		boolean expectedResult=true;
		assertEquals(expectedResult,result);
	}

}
