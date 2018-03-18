import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Location {
	public final static int SPACE_OPS = 3; //number of options for what's in a space
	private int width;
	private int height;
	private int[] currCoords;
	private boolean[] cans; //map of each coordinate to boolean representation of can presence

	public Location(int width, int height, double canProb) throws IllegalArgumentException {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("Width and height must both be greater than 0.");
		if (canProb < 0 || canProb > 1)
			throw new IllegalArgumentException("Probability must be between 0 and 1, inclusive.");
		this.width = width;
		this.height = height;
		currCoords = new int[2];
		genCans(canProb);
	}
	
	/**
	 * generate the can mapping for this location
	 * 
	 * @param canProb
	 * the probability of a can appearing in any single space
	 */
	private void genCans(double canProb) {
		//generate boolean array based on can presence probability
		cans = new boolean[width*height];
		Random rand = new Random();
		for (int i=0;i<cans.length;i++) {
			cans[i] = rand.nextDouble() <= canProb;
		}
	}
	
	/**
	 * results for checking spaces are encoded as follows:
	 * 0: empty
	 * 1: wall
	 * 2: can
	 * order of encoded spaces is:
	 * 0: current
	 * 1: up
	 * 2: right
	 * 3: down
	 * 4: left
	 * 
	 * @return 
	 * encoding of statuses of viewable spaces as integer array
	 */
	public List<Integer> environs() {
		int[] stati = new int[Robot.VISIBILITY];
		stati[0] = cans[currCoords[1]*width+currCoords[0]] ? 2 : 0; //current space can't be a wall, so either can or no can
		if (currCoords[1] == height-1) //up is a wall
			stati[1] = 1;
		else stati[1] = cans[(currCoords[1]+1)*width+currCoords[0]] ? 2 : 0; //up is can or no can
		if (currCoords[0] == width-1) //right is a wall
			stati[2] = 1;
		else stati[2] = cans[currCoords[1]*width+currCoords[0]+1] ? 2 : 0; //right is can or no can
		if (currCoords[1] == 0) //down is a wall
			stati[3] = 1;
		else stati[3] = cans[(currCoords[1]-1)*width+currCoords[0]] ? 2 : 0; //down is can or no can
		if (currCoords[0] == 0) //left is a wall
			stati[4] = 1;
		else stati[4] = cans[currCoords[1]*width+currCoords[0]-1] ? 2 : 0; //left is can or no can
		return Arrays.stream(stati).boxed().collect(Collectors.toList());
	}
	
	/**
	 * change the current location based on the given instructions
	 * 
	 * @param instruct
	 * the given instruction encoded as an integer:
	 * 0: do nothing
	 * 1: pick up can
	 * 2: go up
	 * 3: go right
	 * 4: go down
	 * 5: go left
	 * 6: pick a random movement (including 6)
	 * 
	 * @return
	 * an integer point value based on the success of the result
	 */
	public int move(int instruct) throws IllegalArgumentException {
		if (instruct < 0 || instruct >= Robot.MOVES_POSSIBLE)
			throw new IllegalArgumentException("Unrecognized instruction: " + instruct);
		switch(instruct) {
			case 1:
				if (cans[currCoords[1]*width+currCoords[0]]) { //found a can
					cans[currCoords[1]*width+currCoords[0]] = false;
					return 10; //10 point reward!
				}
				else return -1; //no can, 1 point penalty for failing can pickup
			case 2:
				if (currCoords[1] < height-1) { //space up is available
					currCoords[1]++; //move up
					return 0; //no point consequence
				}
				else return -10; //bumped into a wall, 10 point penalty!
			case 3:
				if (currCoords[0] < width-1) { //space right is available
					currCoords[0]++; //move right
					return 0; //no point consequence
				}
				else return -10; //bumped into a wall, 10 point penalty!
			case 4:
				if (currCoords[1] > 0) { //space down is available
					currCoords[1]--; //move down
					return 0; //no point consequence
				}
				else return -10; //bumped into a wall, 10 point penalty!
			case 5:
				if (currCoords[0] > 0) { //space left is available
					currCoords[0]--; //move left
					return 0; //no point consequence
				}
				else return -10; //bumped into a wall, 10 point penalty!
			case 6:
				return move(new Random().nextInt(Robot.MOVES_POSSIBLE));
			case 0: //the robot doesn't do anything
			default:
				return 0;
		}
	}
}
