import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {
	
	public static void main(String[] args) {
		/* book rules:
		 * 200 actions per bot
		 * start with 200 random bots
		 * repeat:
		 * 	eval each bot on 100 sessions and avg score
		 * 	(50% can prob for each space)
		 * 	evolution (to replace new population):
		 * 		repeat until new 200:
		 * 			choose two parents from current pop with fitness-based probability to have two children
		 */
		//generate first generation
		Set<Robot> curGen = new HashSet<>();
		ConcurrentHashMap<Robot, Integer> fitnesses = new ConcurrentHashMap<>();
		while (curGen.size() < 100)
			curGen.add(new Robot());
		
		//compete and evolve
		int min,max;
		for (int i=0;i<50;i++) {
			//run all the robots
			try {
				curGen.parallelStream().forEach((robot) -> {
					fitnesses.put(robot, (Integer)evalBot(robot,200,100,10,10,.5));
				});
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			min = 500;
			max = -1000;
			for (Integer score : fitnesses.values()) {
				if (score < min)
					min = score;
				if (score > max)
					max = score;
			}
			System.out.println("Generation " + i + ": min " + min + ", max " + max);
			//evolve robots
			curGen = reproduce(fitnesses);
			fitnesses.clear();
		}
		
		//run everything from final generation and analyze results
		curGen.parallelStream().forEach((robot) -> {
			fitnesses.put(robot, (Integer)evalBot(robot,200,100,10,10,.5));
		});
		min = 500;
		max = -1000;
		for (Integer score : fitnesses.values()) {
			if (score < min)
				min = score;
			if (score > max)
				max = score;
		}
		System.out.println("Final generation: min " + min + ", max " + max);
	}
	
	/**
	 * evaluate a robot over a certain number of sessions
	 * 
	 * @param bot
	 * the robot to be evaluated
	 * @param moves
	 * the number of moves the robot makes during the evaluation
	 * @param sessions
	 * the number of different sessions in which the robot is evaluated
	 * @param locWidth
	 * the width of the session locations
	 * @param locHeight
	 * the height of the session locations
	 * @param canProb
	 * the probability of a can being generated in any given space of a session location
	 * 
	 * @return
	 * the average fitness score for the robot over all sessions
	 */
	private static int evalBot(Robot bot, int moves, int sessions, int locWidth, int locHeight, double canProb) {
		int totScore = 0;
		Location loc;
		for (int i=0;i<sessions;i++) {
			loc = new Location(locWidth, locHeight, canProb);
			totScore += bot.run(loc, moves);
		}
		return totScore / sessions;
	}
	
	/**
	 * produce a new generation of robots based on the fitness of the current generation
	 * 
	 * @param curGen
	 * the current generation and their average fitness scores as evaluated over random sessions 
	 * 
	 * @return
	 * the new generation of robots
	 */
	private static Set<Robot> reproduce(ConcurrentHashMap<Robot, Integer> curGen) {
		Set<Robot> newGen = new HashSet<Robot>();
		//create a (completely positive) normalized probability array based on fitness array
		int totScores = 0;
		int min = 0;
		for (Integer fitness:curGen.values()) {
			totScores += fitness;
			if (fitness < min)
				min = fitness;
		}
		//will be subtracting minimum value (if below 0) from every score in order to make everything positive,
		//so need to compensate by adding to total score
		totScores -= min*curGen.size();
		ConcurrentHashMap<Robot, Double> probs = new ConcurrentHashMap<>();
		for (Entry<Robot, Integer> robot : curGen.entrySet()) {
			probs.put(robot.getKey(), ((double)(robot.getValue() - min))/(double)totScores);
		}
		//repeatedly select two parents to "mate" based on fitness-based probability until new generation is full
		double p, cumProb;
		Robot[] parents = new Robot[2];
		Robot[] children = new Robot[2];
		while (newGen.size() < curGen.size()) {
			for (int j=0;j<2;j++) {
				p = Math.random();
				cumProb = 0.0;
				for (Entry<Robot, Double> robot : probs.entrySet()) {
					cumProb += robot.getValue();
					if (p <= cumProb) {
						parents[j] = robot.getKey();
						break;
					}
				}
			}
			children = Robot.reproduce(parents[0], parents[1]);
			newGen.add(children[0]);
			newGen.add(children[1]);
		}
		return newGen;
	}

}
