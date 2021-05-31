package student_player;

import java.util.ArrayList;
import java.util.stream.IntStream;

import boardgame.Board;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;

public class Score {
	
private PentagoBoardState pbs;
private int defentScore = 1000;
private int attackScore = 1000;
private double attackCoef = 1;
	
	Score(PentagoBoardState pbs, double attackCoef){
		this.pbs = pbs;
		this.attackCoef = attackCoef;
	}
	
	public int utility(int print) {
		PentagoBoardState pbs = this.pbs;
		
		if (pbs.getWinner() == pbs.getTurnPlayer()) {  return Integer.MAX_VALUE; }
		else if (pbs.getWinner() == 1-pbs.getTurnPlayer()) {  return (-1)*Integer.MAX_VALUE; }
		else if (pbs.getWinner() == Board.DRAW) {  return 0; }
		
	    Piece myColor, opColor;
	    
	    if (pbs.getTurnPlayer() == 0) {opColor = Piece.BLACK; myColor = Piece.WHITE;}
	    else {opColor = Piece.WHITE; myColor = Piece.BLACK;}
	    
	    int myUtil = 0, opUtil = 0, utilVal = 0; //rowUtil = 0;
	    
	    myUtil = getScore(myColor, opColor, 0, print);
	    opUtil = getScore(opColor, myColor, 1, print);
	    utilVal = myUtil - opUtil;
	    
	    return utilVal;
	}
	
	
	public int getScore(Piece myColor, Piece opColor, int isEnemy, int print) {
		if(print == 1) {
			System.out.println("("+row_check(0, myColor, opColor, isEnemy)+", "+row_check(1, myColor, opColor, isEnemy)+", "+row_check(2, myColor, opColor, isEnemy)
			+", "+column_check(0, myColor, opColor, isEnemy)+", "+column_check(1, myColor, opColor, isEnemy)+", "+column_check(2, myColor, opColor, isEnemy)
			+", "+tl_br_corner_check(myColor, opColor, isEnemy)+", "+tr_bl_corner_check(myColor, opColor, isEnemy)
			+", "+tr_bl_outer_lower_check(myColor, opColor, isEnemy)+", "+tr_bl_outer_higher_check(myColor, opColor, isEnemy)+", "+tl_br_outer_lower_check(myColor, opColor, isEnemy)+", "+tl_br_outer_higher_check(myColor, opColor, isEnemy)+")");
		}
		return row_check(0, myColor, opColor, isEnemy)+row_check(1, myColor, opColor, isEnemy)+row_check(2, myColor, opColor, isEnemy)
	    +column_check(0, myColor, opColor, isEnemy)+column_check(1, myColor, opColor, isEnemy)+column_check(2, myColor, opColor, isEnemy)
	    +tl_br_corner_check(myColor, opColor, isEnemy)+tr_bl_corner_check(myColor, opColor, isEnemy)
	    +tr_bl_outer_lower_check(myColor, opColor, isEnemy)+tr_bl_outer_higher_check(myColor, opColor, isEnemy)+tl_br_outer_lower_check(myColor, opColor, isEnemy)+tl_br_outer_higher_check(myColor, opColor, isEnemy);
	}
	
	
	public int six_warning_check(int Sum, int isEnemy, int type10, int type8 , int type6 , int type3, int type2 , int type1, int type0) {
		if(isEnemy == 0) {
			//System.out.println("deathdeath1");
			if((type10>=1 && (type8+type3)>=1) || type10>=2)  {return Sum + 5*defentScore;}
			if((type10>=1 && (type6+type2)>=1) || (type8>=1 && type3>=1) || type8>=2)  {return Sum + 3*defentScore;}
			if((type10>=1 && type1>=1) || (type8>=1 && type6>=1) || (type8>=1 && type2>=2))  {return Sum + defentScore;}
		}
		else if(isEnemy == 1) {
			if((type10>=1 && (type8+type3)>=1) || type10>=2)  {return Sum + (int)(2*attackScore*attackCoef);}
			if((type10>=1 && (type6+type2)>=2) || (type8>=2 && type3>=1) || type8>=3)  {return Sum + (int)(1*attackScore*attackCoef);}
			if((type8>=2 && (type6+type2)>=2))  {return Sum + (int)(0.5*attackScore*attackCoef);}
		}
		return Sum;
	}
	
	
	public int five_warning_check(int Sum, int isEnemy, int type5, int type3 , int type2 , int type1) {
		if(((type5+type3>=3&&type5>=1) || type5 >= 2) && isEnemy==0) {
			if(type2>=2)  {return Sum += 2*defentScore;}
			if(type2>=1)  {return Sum += defentScore;}
		}
		
		if(type5 >= 3 && isEnemy==1) {
			if(type2>=2)  {return Sum += (int)(attackScore*attackCoef);}
		}
		return Sum;
	}
	
	
	
	//Check Row and Column=========================================================================================================================================================
	

	private int row_check(int section_i, Piece myColor, Piece opColor, int isEnemy) {
		int Sum = 0;
		int[] Index = {0, 3};
		int type10 = 0, type8 = 0, type6 = 0, type3 = 0, type2 = 0, type1 = 0, type0 = 0;
		
		for(int column_i: Index){
			for(int row_i: Index){
				int type = 0;
				if(pbs.getPieceAt(row_i+1, column_i+section_i)== opColor || (pbs.getPieceAt(row_i, column_i+section_i)== opColor && pbs.getPieceAt(row_i+2, column_i+section_i)== opColor)){type = 0;}
				else {
					int  opcolor_num = 0, empty_num = 0;
					for(int position_i: IntStream.range(0, 3).toArray()) {
						Piece tempColor = pbs.getPieceAt(row_i+position_i, column_i+section_i);
						if(tempColor== opColor){opcolor_num ++;}
						//else if(pbs.getPieceAt(row_i+position_i, column_i+section_i)== myColor){mycolor_num ++;}
						else if(tempColor== Piece.EMPTY){empty_num ++;}
					}
					if(opcolor_num == 0) {
						if(empty_num == 0) {type = 10; type10++;}
						else if(empty_num == 1) {type = 8; type8++;}
						else if(empty_num == 2) {type = 6; type6++;}
						else if(empty_num == 3) {type = 1; type1++;}
					}
					else if(opcolor_num == 1) {
						if(empty_num == 0) {type = 3;type3++;}
						else if(empty_num == 1) {type = 2;type2++;}
						else if(empty_num == 2) {type = 0;type0++;}
					}
				}
				Sum += type;
			}				
		}
		return six_warning_check( Sum,  isEnemy,  type10,  type8 ,  type6 ,  type3,  type2 ,  type1,  type0);
	}
	
	
	private int column_check(int section_i, Piece myColor, Piece opColor, int isEnemy) {
		int Sum = 0;
		int[] Index = {0, 3};
		int type10 = 0, type8 = 0, type6 = 0, type3 = 0, type2 = 0, type1 = 0, type0 = 0;
		
		for(int column_i: Index){
			for(int row_i: Index){
				int type = 0;
				if(pbs.getPieceAt(row_i+section_i, column_i+1)== opColor || (pbs.getPieceAt(row_i+section_i, column_i+0)== opColor && pbs.getPieceAt(row_i+section_i, column_i+2)== opColor)){type = 0;}
				else {
					int opcolor_num = 0, empty_num = 0;
					for(int position_i: IntStream.range(0, 3).toArray()) {
						Piece tempColor = pbs.getPieceAt(row_i+section_i, column_i+position_i);
						if(tempColor== opColor){opcolor_num ++;}
						//else if(pbs.getPieceAt(row_i+section_i, column_i+position_i)== myColor){mycolor_num ++;}
						else if(tempColor== Piece.EMPTY){empty_num ++;}
					}
					if(opcolor_num == 0) {
						if(empty_num == 0) {type = 10; type10++;}
						else if(empty_num == 1) {type = 8; type8++;}
						else if(empty_num == 2) {type = 6; type6++;}
						else if(empty_num == 3) {type = 1; type1++;}
					}
					else if(opcolor_num == 1) {
						if(empty_num == 0) {type = 3; type3++;}
						else if(empty_num == 1) {type = 2;type2++;}
						else if(empty_num == 2) {type = 0;type0++;}
					}
				}
				Sum += type;
			}				
		}
		return six_warning_check( Sum,  isEnemy,  type10,  type8 ,  type6 ,  type3,  type2 ,  type1,  type0);
	}
	
	
	//Check Corner Diagonal=========================================================================================================================================================
	
	
	private int tl_br_corner_check(Piece myColor, Piece opColor, int isEnemy){
		int Sum = 0;
		int[] Index = {0, 3};
		int type10 = 0, type8 = 0, type6 = 0, type3 = 0, type2 = 0, type1 = 0, type0 = 0;
		
		for(int column_i: Index){
			for(int row_i: Index){
				int type = 0;
				if(pbs.getPieceAt(row_i+1, column_i+1)== opColor || (pbs.getPieceAt(row_i, column_i)== opColor && pbs.getPieceAt(row_i+2, column_i+2)== opColor)){type = 0;}
				else {
					int opcolor_num = 0, empty_num = 0;
					for(int position_i: IntStream.range(0, 3).toArray()) {
						Piece tempColor = pbs.getPieceAt(row_i+position_i, column_i+position_i);
						if(tempColor== opColor){opcolor_num ++;}
						//else if(pbs.getPieceAt(row_i+position_i, column_i+position_i)== myColor){mycolor_num ++;}
						else if(tempColor== Piece.EMPTY){empty_num ++;}
					}
					if(opcolor_num == 0) {
						if(empty_num == 0) {type = 10; type10++;}
						else if(empty_num == 1) {type = 8; type8++;}
						else if(empty_num == 2) {type = 6; type6++;}
						else if(empty_num == 3) {type = 1; type1++;}
					}
					else if(opcolor_num == 1) {
						if(empty_num == 0) {type = 3;type3++;}
						else if(empty_num == 1) {type = 2; type2++;}
						else if(empty_num == 2) {type = 0; type0++;}
					}
				}
				Sum += type;
			}				
		}
		return six_warning_check( Sum,  isEnemy,  type10,  type8 ,  type6 ,  type3,  type2 ,  type1,  type0);
	}
	
	private int tr_bl_corner_check(Piece myColor, Piece opColor, int isEnemy){
		int Sum = 0;
		int[] Index = {0, 3};
		int type10 = 0, type8 = 0, type6 = 0, type3 = 0, type2 = 0, type1 = 0, type0 =0;
		
		for(int column_i: Index){
			for(int row_i: Index){
				int type = 0;
				if(pbs.getPieceAt(row_i+1, column_i+1)== opColor || (pbs.getPieceAt(row_i+2, column_i)== opColor && pbs.getPieceAt(row_i, column_i+2)== opColor)){type = 0;}
				else {
					int opcolor_num = 0, empty_num = 0;
					for(int position_i: IntStream.range(0, 3).toArray()) {
						Piece tempColor = pbs.getPieceAt(row_i+2-position_i, column_i+position_i);
						if(tempColor == opColor){opcolor_num ++;}
						//else if(pbs.getPieceAt(row_i+position_i, column_i+position_i)== myColor){mycolor_num ++;}
						else if(tempColor == Piece.EMPTY){empty_num ++;}
					}
					if(opcolor_num == 0) {
						if(empty_num == 0) {type = 10; type10++;}
						else if(empty_num == 1) {type = 8; type8++;}
						else if(empty_num == 2) {type = 6; type6++;}
						else if(empty_num == 3) {type = 1; type1++;}
					}
					else if(opcolor_num == 1) {
						if(empty_num == 0) {type = 3; type3++;}
						else if(empty_num == 1) {type = 2; type2++;}
						else if(empty_num == 2) {type = 0; type0++;}
					}
				}
				Sum += type;
			}				
		}
		return six_warning_check( Sum,  isEnemy,  type10,  type8 ,  type6 ,  type3,  type2 ,  type1,  type0);
	}
	
	
	//Check Outer Diagonal=========================================================================================================================================================
	
	
	private int tr_bl_outer_lower_check(Piece myColor, Piece opColor, int isEnemy){
		int Sum = 0;
		int[] Index = {0, 3};
		int type5 = 0, type3 =0, type2 = 0, type1 = 0;
		
		for(int column_i: Index){
			for(int row_i: Index){
				int type = 0;
				
				Piece lowerColor = pbs.getPieceAt(row_i+2, column_i+1), higherColor = pbs.getPieceAt(row_i+1, column_i+2);
				if(lowerColor == opColor || higherColor == opColor){type += 0;}
				else if(lowerColor == myColor && higherColor == myColor) {type += 5; type5 ++;}
				else if(lowerColor == myColor || higherColor == myColor) {type += 3; type3 ++;}
				else if(lowerColor == Piece.EMPTY && higherColor == Piece.EMPTY) {type += 1;}
				
				Piece cornerColor = pbs.getPieceAt(row_i, column_i);
				if(cornerColor == myColor) {type += 2; type2 ++;}
				else if(cornerColor == Piece.EMPTY){type += 1;type1 ++;}
				else if(cornerColor == opColor){type += 0;}
				
				Sum += type;
			}	
		}
		return five_warning_check( Sum,  isEnemy,  type5,  type3 ,  type2 ,  type1);
	}
	
	
	private int tr_bl_outer_higher_check(Piece myColor, Piece opColor, int isEnemy){
		int Sum = 0;
		int[] Index = {0, 3};
		int type5 = 0, type3 =0, type2 = 0, type1 = 0;
		
		for(int column_i: Index){
			for(int row_i: Index){
				int type = 0;
				
				Piece lowerColor = pbs.getPieceAt(row_i+1, column_i), higherColor = pbs.getPieceAt(row_i, column_i+1);
				if(lowerColor == opColor || higherColor == opColor){type += 0;}
				else if(lowerColor == myColor && higherColor == myColor) {type += 5; type5 ++;}
				else if(lowerColor == myColor || higherColor == myColor) {type += 3; type3 ++;}
				else if(lowerColor == Piece.EMPTY && higherColor == Piece.EMPTY) {type += 1;}
				
				Piece cornerColor = pbs.getPieceAt(row_i+2, column_i+2);
				if(cornerColor == myColor) {type += 2; type2 ++;}
				else if(cornerColor == Piece.EMPTY){type += 1; type1 ++;}
				else if(cornerColor == opColor){type += 0;}
				
				Sum += type;
			}				
		}
		return five_warning_check( Sum,  isEnemy,  type5,  type3 ,  type2 ,  type1);
	}
	
	
	private int tl_br_outer_lower_check(Piece myColor, Piece opColor, int isEnemy){
		int Sum = 0;
		int[] Index = {0, 3};
		int type5 = 0, type3 =0, type2 = 0, type1 = 0;
		
		for(int column_i: Index){
			for(int row_i: Index){
				int type = 0;
				
				Piece lowerColor = pbs.getPieceAt(row_i+2, column_i+1), higherColor = pbs.getPieceAt(row_i+1, column_i);
				if(lowerColor == opColor || higherColor == opColor){type += 0;}
				else if(lowerColor == myColor && higherColor == myColor) {type += 5; type5 ++;}
				else if(lowerColor == myColor || higherColor == myColor) {type += 3; type3 ++;}
				else if(lowerColor == Piece.EMPTY && higherColor == Piece.EMPTY) {type += 1;}
				
				Piece cornerColor = pbs.getPieceAt(row_i, column_i+2);
				if(cornerColor == myColor) {type += 2; type2 ++;}
				else if(cornerColor == Piece.EMPTY){type += 1; type1 ++;}
				else if(cornerColor == opColor){type += 0;}
				
				Sum += type;
			}				
		}
		return five_warning_check( Sum,  isEnemy,  type5,  type3 ,  type2 ,  type1);
	}
	
	
	private int tl_br_outer_higher_check(Piece myColor, Piece opColor, int isEnemy){
		int Sum = 0;
		int[] Index = {0, 3};
		int type5 = 0, type3 =0, type2 = 0, type1 = 0;
		
		for(int column_i: Index){
			for(int row_i: Index){
				int type = 0;
				
				Piece lowerColor = pbs.getPieceAt(row_i+1, column_i+2), higherColor = pbs.getPieceAt(row_i, column_i+1);
				if(lowerColor == opColor || higherColor == opColor){type += 0;}
				else if(lowerColor == myColor && higherColor == myColor) {type += 5; type5 ++;}
				else if(lowerColor == myColor || higherColor == myColor) {type += 3; type3 ++;}
				else if(lowerColor == Piece.EMPTY || higherColor == Piece.EMPTY) {type += 1;}
				
				Piece cornerColor = pbs.getPieceAt(row_i+2, column_i);
				if(cornerColor == myColor) {type += 2; type2 ++;}
				else if(cornerColor == Piece.EMPTY){type += 1; type1 ++;}
				else if(cornerColor == opColor){type += 0;}
				
				Sum += type;
			}				
		}
		return five_warning_check( Sum,  isEnemy,  type5,  type3 ,  type2 ,  type1);			
	}
}
