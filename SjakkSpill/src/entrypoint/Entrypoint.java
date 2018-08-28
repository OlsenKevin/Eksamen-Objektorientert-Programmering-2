package entrypoint;

import javax.swing.JFrame;

import spillet.SjakkSpill;
import stockfish.Stockfish;

public class Entrypoint {
	public static void main(String[] args)
	{
		//All code in entrypoint is created by us.
	 	  
	 	  Stockfish stockfish = new Stockfish(); 
	 	  String startFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; 
	 	 
	 	  // initialize and connect to engine
	 	  if (stockfish.startEngine()) { 
	 	   System.out.println("Stocfish engine is running"); 
	 	  }
	 	  else { 
	 	   System.out.println("Stockfish engine failed to start"); 
	 	  } 
	 	  
	 	  // initialize two chess boards
	 	  SjakkSpill brett = new SjakkSpill(stockfish);
	 	  brett.setLocation(200, 0);
	 	  brett.setTitle("Brett 1");
	 	  
	 	  SjakkSpill brett2 = new SjakkSpill(stockfish);
	 	  brett2.setLocation(800, 0);
	 	  brett2.setTitle("Brett 2");
	 	  
	 	  brett.add_competitor(brett2);
	 	  brett2.add_competitor(brett);	 	 

	 	 
	 	  // drawing the board from its starting position 
	 	  System.out.println("Board state :"); 
	 	  stockfish.drawBoard(startFEN); 
	 	  	 	   
	 	 } 
	 	}