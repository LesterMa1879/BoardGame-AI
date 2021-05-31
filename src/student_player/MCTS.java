package student_player;
import java.util.HashMap;
import java.util.Map;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class MCTS {
	private PentagoBoardState pbs;
	private long calculation_time;
	private int max_actions;
	private float confient = (int) 1.96;
	private int max_depth = 1;
	private Map<PentagoMove, Integer> plays = new HashMap<PentagoMove, Integer>();
	private Map<PentagoMove, Integer> wins = new HashMap<PentagoMove, Integer>();
	
	MCTS(PentagoBoardState board, int max_actions){
		this.pbs = board;
		this.max_actions = max_actions;
	}
	
	public PentagoMove get_actions() {
		long startTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - startTime)< calculation_time) {
			PentagoBoardState CloneBoard = (PentagoBoardState) pbs.clone();
			run_simulation(CloneBoard);
		}
		return select_one_move();
	}
	
	public void run_simulation(PentagoBoardState pbs) {
		
	}
	
	public PentagoMove select_one_move() {
		return null;
	}
}
