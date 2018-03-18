import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class LocationTest {
	
	@Test
	void illegalDimensionsTest() {
		Throwable widthUnder0Error = assertThrows(IllegalArgumentException.class, () -> new Location(-5,3,.5));
		assertEquals(widthUnder0Error.getMessage(),"Width and height must both be greater than 0.");
		
		Throwable zeroHeightError = assertThrows(IllegalArgumentException.class, () -> new Location(3,0,.5));
		assertEquals(zeroHeightError.getMessage(),"Width and height must both be greater than 0.");
	}
	
	@Test
	void illegalProbabilityTest() {
		Throwable negativeCanProbError = assertThrows(IllegalArgumentException.class, () -> new Location(3,3,-.5));
		assertEquals(negativeCanProbError.getMessage(),"Probability must be between 0 and 1, inclusive.");
		
		Throwable overOneCanProbError = assertThrows(IllegalArgumentException.class, () -> new Location(3,3,1.3));
		assertEquals(overOneCanProbError.getMessage(),"Probability must be between 0 and 1, inclusive.");
	}
	
	@Test
	void envSingleSpaceTest() {
		Location loc = new Location(1,1,0);
		List<Integer> env = new ArrayList<>();
		env.add(0); //current
		env.add(1); //up
		env.add(1); //right
		env.add(1); //down
		env.add(1); //left
		assertEquals(loc.environs(), env);
		
		Location loc1 = new Location(1,1,1);
		List<Integer> env1 = new ArrayList<>();
		env1.add(2); //current
		env1.add(1); //up
		env1.add(1); //right
		env1.add(1); //down
		env1.add(1); //left
		assertEquals(loc1.environs(), env1);
	}
	
	@Test
	void largerWidthTest() {
		Location loc = new Location(2,1,0);
		List<Integer> env = new ArrayList<>();
		env.add(0); //current
		env.add(1); //up
		env.add(0); //right
		env.add(1); //down
		env.add(1); //left
		assertEquals(loc.environs(), env);
		
		Location loc1 = new Location(2,1,1);
		List<Integer> env1 = new ArrayList<>();
		env1.add(2); //current
		env1.add(1); //up
		env1.add(2); //right
		env1.add(1); //down
		env1.add(1); //left
		assertEquals(loc1.environs(), env1);
	}
	
	@Test
	void largerHeightTest() {
		Location loc = new Location(1,2,0);
		List<Integer> env = new ArrayList<>();
		env.add(0); //current
		env.add(0); //up
		env.add(1); //right
		env.add(1); //down
		env.add(1); //left
		assertEquals(loc.environs(), env);
		
		Location loc1 = new Location(1,2,1);
		List<Integer> env1 = new ArrayList<>();
		env1.add(2); //current
		env1.add(2); //up
		env1.add(1); //right
		env1.add(1); //down
		env1.add(1); //left
		assertEquals(loc1.environs(), env1);
	}
	
	@Test
	void largeSpaceTest() {
		Location loc = new Location(2,2,0);
		List<Integer> env = new ArrayList<>();
		env.add(0); //current
		env.add(0); //up
		env.add(0); //right
		env.add(1); //down
		env.add(1); //left
		assertEquals(loc.environs(), env);
		
		Location loc1 = new Location(2,2,1);
		List<Integer> env1 = new ArrayList<>();
		env1.add(2); //current
		env1.add(2); //up
		env1.add(2); //right
		env1.add(1); //down
		env1.add(1); //left
		assertEquals(loc1.environs(), env1);
	}
	
	@Test
	void movementTest() {
		Location loc = new Location(2,2,1);
		assertEquals(loc.move(0), 0);
		assertEquals(loc.move(1), 10);
		assertEquals(loc.move(1), -1);
		assertEquals(loc.move(4), -10);
		assertEquals(loc.move(5), -10);
		assertEquals(loc.move(2), 0);
		assertEquals(loc.move(3), 0);
		assertEquals(loc.move(4), 0);
		assertEquals(loc.move(1), 10);
		assertEquals(loc.move(5), 0);
	}
}
