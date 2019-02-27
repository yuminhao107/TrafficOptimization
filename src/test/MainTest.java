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
		boolean expectedResult=false;
		assertEquals(expectedResult,result);
	}
	
	@Test 
	void testNumOfTurns(){
		Path path=new Path();
		path.add(new Node("",0d,0d,10d));
		path.add(new Node("",0d,0d,20d));
		path.add(new Node("",0d,0d,30d));
		path.add(new Node("",0d,0d,40d));
		path.add(new Node("",0d,0d,50d));
		int result=path.numOfTurns();
		int expectedResult=0;
		assertEquals(expectedResult,result);
	}
	
	void testNumOfTurns2(){
		Path path=new Path();
		path.add(new Node("",0d,0d,0d));
		path.add(new Node("",0d,10d,0d));
		path.add(new Node("",0d,20d,0d));
		path.add(new Node("",10d,20d,0d));
		path.add(new Node("",20d,20d,0d));
		int result=path.numOfTurns();
		int expectedResult=1;
		assertEquals(expectedResult,result);
	}
	
	void testNumOfTurns3(){
		Path path=new Path();
		path.add(new Node("",0d,0d,0d));
		path.add(new Node("",0d,10d,0d));
		path.add(new Node("",10d,10d,0d));
		path.add(new Node("",10d,20d,0d));
		path.add(new Node("",20d,20d,0d));
		int result=path.numOfTurns();
		int expectedResult=3;
		assertEquals(expectedResult,result);
	}

}
