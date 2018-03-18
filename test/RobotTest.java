import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RobotTest {

	@Test
	void equalityTest() {
		Robot rob1 = new Robot();
		Robot rob2 = new Robot();
		assert(rob1.equals(rob1));
		assertFalse(rob1.equals(rob2));
	}
	
	@Test
	void reproduceTest() {
		Robot rob1 = new Robot();
		Robot rob2 = new Robot();
		Robot[] children = Robot.reproduce(rob1, rob2);
		assertFalse(rob1.equals(children[0]));
		assertFalse(rob1.equals(children[1]));
		assertFalse(rob2.equals(children[0]));
		assertFalse(rob2.equals(children[1]));
	}
	
	@Test
	void runSetupTest() {
		Robot rob = new Robot();
		Throwable locError = assertThrows(NullPointerException.class, () -> rob.run(null, 1));
		assertEquals(locError.getMessage(),"Location cannot be null.");
		
		Throwable movesError = assertThrows(IllegalArgumentException.class, () -> rob.run(new Location(1,1,0), -1));
		assertEquals(movesError.getMessage(),"Number of moves cannot be negative.");
	}
	
	@Test
	void runTest() {
		Robot rob = new Robot();
		Location loc = new Location(5,5,.3);
		assertEquals(rob.run(loc, 0), 0);
		assertEquals(rob.run(loc, 20), rob.run(loc, 20));
	}
}
