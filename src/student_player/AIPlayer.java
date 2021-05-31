package student_player;

import java.util.ArrayList;
import java.util.stream.IntStream;

import boardgame.Board;
import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoBoardState.Quadrant;
import pentago_swap.PentagoCoord;
import pentago_swap.PentagoMove;

public class AIPlayer extends PentagoPlayer {
      
    //==============================================================================
    
	private int DEPTH = 3;
	private Move nextMove;
	
	private int search_count = 0;
	private int cut_count = 0;
	private long startTime = 0;
	private int AIcolor;
	
	public ArrayList<PentagoMove> getAllSelfMoves(PentagoBoardState pbs) {
        ArrayList<PentagoMove> legalMoves = new ArrayList<PentagoMove>();
        ArrayList<PentagoMove> orderMoves = new ArrayList<PentagoMove>();
        for (int i = 0; i < 6; i++) { //Iterate through positions on board
            for (int j = 0; j < 6; j++) {
                if (pbs.getPieceAt(i, j) == Piece.EMPTY) {
                	if((i==1&&j==1) || (i==1&&j==4) || (i==4&&j==1) || (i==4&&j==4)) {
                		orderMoves.add(new PentagoMove(i, j, Quadrant.TL, Quadrant.TR, pbs.getTurnPlayer()));
                	}
                	else {legalMoves.add(new PentagoMove(i, j, Quadrant.TL, Quadrant.TR, pbs.getTurnPlayer()));}
                }
            }
        }
        orderMoves.addAll(legalMoves);
        return orderMoves;
    }
	
    
    private int negamax(PentagoBoardState pbs, int depth, int alpha, int beta) {
        if (depth == 0 || pbs.getWinner() != Board.NOBODY) 
        {
        	double attackCoef = 1;
        	int TurnNumber = pbs.getTurnNumber();
        	
        	if(AIcolor == 0) {
        		if(TurnNumber<=5) {attackCoef = 2;}
            	else {attackCoef = 1;}
        	}else if(AIcolor == 1) {attackCoef = 1;}
        	
        	Score score = new Score(pbs, attackCoef);
            return score.utility(0);
        }
        
        int hasWinmove = 0;
        if(depth == DEPTH) {
        	ArrayList<PentagoMove> moveList = pbs.getAllLegalMoves();
        	for ( PentagoMove move: moveList ) {
            	PentagoBoardState CloneState = (PentagoBoardState) pbs.clone();
        		CloneState.processMove(move);
        		if (CloneState.getWinner()== 1-CloneState.getTurnPlayer()) {
        			nextMove = move;
        			hasWinmove =1;
        			break;
        		}
            }
        }
        
        ArrayList<PentagoMove> SlefmoveList = getAllSelfMoves(pbs);
        
        for (PentagoMove move: SlefmoveList) {
        	if(hasWinmove ==1)  {break;}
        	
        	long endTime = System.nanoTime();
            if((endTime - startTime) > 1900000000) {break;};
            
            search_count ++;
           
            PentagoBoardState CloneState = (PentagoBoardState) pbs.clone();
    		CloneState.processMove(move);

            int value = -negamax(CloneState, depth - 1, -beta, -alpha);
            
            if(value > alpha) {
            	if(depth == DEPTH) {
            		nextMove = move;
            	}
            	if(value >= beta) {
            		cut_count ++;
            		return beta;
            	}
            	alpha = value;
            }
        }
        return alpha;    
    }
    
    //==============================================================================
  

    public Move chooseMove(PentagoBoardState boardState) {
    	
    	startTime = System.nanoTime();
    	nextMove = boardState.getRandomMove();
    	
    	search_count = 0;
    	cut_count = 0;
    	AIcolor = boardState.getTurnPlayer();

    	negamax(boardState, DEPTH, (-1)*Integer.MAX_VALUE, Integer.MAX_VALUE);
    	    	
    	//PentagoBoardState CloneState = (PentagoBoardState) boardState.clone();
    	//CloneState.processMove((PentagoMove) nextMove);
        //Score score2 = new Score(CloneState, 0);
        //System.out.print("###Score"+CloneState.getTurnPlayer()+"###: "+score2.utility(1)+"\n");
    	
        return nextMove;
    }
}