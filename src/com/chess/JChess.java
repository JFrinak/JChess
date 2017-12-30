package com.chess;
import com.chess.engine.board.Board;
import com.chess.gui.*;
public class JChess {
	public static void  main(String[] args){
		Board board = Board.createStandardBoard();
		Table.get().show();
		System.out.println(board);
	}
}
