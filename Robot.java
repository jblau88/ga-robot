import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Robot {
	public final static int VISIBILITY = 5; //number of spaces visible from current space
	public final static int MOVES_POSSIBLE = 7; //the number of possible moves the robot can make at a given time
	public final static double MUTATION_PROB = 0.1; //the probability of an inherited instruction "gene" randomly mutating
	private final int[] instruct;
	private final ConcurrentHashMap<List<Integer>, Integer> sitToInstructMap; //matches a given environment with the corresponding instruction (last index)

	public Robot () {
		instruct = randGen();
		sitToInstructMap = initMap();
	}
	
	private Robot (ConcurrentHashMap<List<Integer>, Integer> sitToInstructMap, int[] instructionSet) {
		this.sitToInstructMap = sitToInstructMap;
		this.instruct = instructionSet;
	}
	
	private ConcurrentHashMap<List<Integer>, Integer> initMap() {
		ConcurrentHashMap<List<Integer>, Integer> map = new ConcurrentHashMap<>();
		int[] visibleSpace = new int[VISIBILITY];
		int index = 0;
		for (int i=0;i<instruct.length;i++) {
			map.put(Arrays.stream(visibleSpace).boxed().collect(Collectors.toList()), i);
			while(index<visibleSpace.length && visibleSpace[index]==Location.SPACE_OPS-1) {
				visibleSpace[index] = 0;
				index++;
			}
			if (index<visibleSpace.length)
				visibleSpace[index]++;
			index = 0;
		}
		return map;
	}
	
	/**
	 * randomly generates an instruction set for this robot
	 * 
	 * @return
	 * the robot's instruction set
	 */
	private int[] randGen() {
		int[] instruct = new int[(int)java.lang.Math.pow(Location.SPACE_OPS,VISIBILITY)];
		Random rand = new Random();
		for (int i=0;i<instruct.length;i++) {
			instruct[i] = rand.nextInt(MOVES_POSSIBLE);
		}
		return instruct;
	}
	
	/**
	 * produces two new robots based on the instruction sets of two parent robots, with some random mutations
	 * 
	 * @param parent1
	 * one of the parents from which the two children will inherit
	 * @param parent2
	 * one of the parents from which the two children will inherit
	 * @return
	 * an array containing the two child robots
	 */
	public static Robot[] reproduce(Robot parent1, Robot parent2) {
		//choose split point
		Random rand = new Random();
		int splitChoice = rand.nextInt(parent1.instruct.length);
		return new Robot[] {inherit(parent1, parent2, splitChoice, true),
				inherit(parent1, parent2, splitChoice, false)};
	}
	
	private static Robot inherit(Robot parent1, Robot parent2, int splitPoint, boolean firstChild) {
		int[] instructions = new int[parent1.instruct.length];
		//fill new array based on parent instructions
		if (firstChild) {
			for (int i=0;i<splitPoint;i++) {
				instructions[i] = parent1.instruct[i];
			}
			for (int i=splitPoint;i<instructions.length;i++) {
				instructions[i] = parent2.instruct[i];
			}
		}
		else {
			for (int i=0;i<splitPoint;i++) {
				instructions[i] = parent2.instruct[i];
			}
			for (int i=splitPoint;i<instructions.length;i++) {
				instructions[i] = parent1.instruct[i];
			}
		}
		//random mutations with a given probability
		Random rand = new Random();
		for (int i=0;i<instructions.length;i++) {
			if (rand.nextDouble() < MUTATION_PROB) {
				instructions[i] = rand.nextInt(MOVES_POSSIBLE);
			}
		}
		return new Robot(parent1.sitToInstructMap, instructions);
	}
	
	public int run(Location loc, int moves) throws IllegalArgumentException,NullPointerException {
		if (loc == null)
			throw new NullPointerException("Location cannot be null.");
		if (moves < 0)
			throw new IllegalArgumentException("Number of moves cannot be negative.");
		int score = 0;
		for (int i=0;i<moves;i++) {
			score += loc.move(instruct[sitToInstructMap.get(loc.environs())]); //moves and changes score based on result
		}
		return score;
	}
	
	@Override
	public boolean equals(Object o) {
		
		// if object is compared with itself return true  
        if (o == this) {
            return true;
        }
 
        // if object isn't a robot return false
        if (!(o instanceof Robot)) {
            return false;
        }
         
        // typecast o to Robot so that we can compare data members 
        Robot r = (Robot) o;  
        // compare instructions 
        return Arrays.equals(r.instruct, this.instruct);
	}
}
