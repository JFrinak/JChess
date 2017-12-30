package com.chess.engine.pieces;

import com.chess.engine.board.*;
import com.chess.engine.Alliance;
import com.chess.engine.board.Move.*;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class Knight extends Piece {
	
	private static int[] CANDIDATE_MOVE_COORDINATES = {-17,-15,-10,-6,6,10,15,17};

	public Knight(final Alliance pieceAlliance, final int piecePosition){
		super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
	}
	public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
		super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
	}
	@Override
	public Collection<Move> calculateLegalMoves(final Board board){
		final List<Move> legalMoves = new ArrayList<>();
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffset)||
					isSecondColumnExclusion(this.piecePosition,currentCandidateOffset)||
					isSeventhColumnExclusion(this.piecePosition,currentCandidateOffset)||
					isEighthColumnExclusion(this.piecePosition,currentCandidateOffset)){
					continue;
				}
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				//Tile is not occupied by a piece
				if(!candidateDestinationTile.isTileOccupied()){
					legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
				} else{ // Tile is occupied by a piece
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
					if(this.pieceAlliance != pieceAlliance){
						legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
					}
				}
			}
		}
		return Collections.unmodifiableCollection(legalMoves);
	}
	@Override
	public Knight movePiece(final Move move){
		return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	@Override
	public String toString(){
		return PieceType.KNIGHT.toString();
	}
	private static boolean isFirstColumnExclusion(final int currentPosition, final int coordinateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && ((coordinateOffset == -17) || (coordinateOffset == -10) || (coordinateOffset == 6) 
			|| (coordinateOffset == 15));
	}
	private static boolean isSecondColumnExclusion(final int currentPosition, final int coordinateOffset) {
		return BoardUtils.SECOND_COLUMN[currentPosition] &&  ((coordinateOffset == -10) || (coordinateOffset == 6));
	}
	private static boolean isSeventhColumnExclusion(final int currentPosition, final int coordinateOffset) {
		return BoardUtils.SEVENTH_COLUMN[currentPosition] &&  ((coordinateOffset == -6) || (coordinateOffset == 10));
	}
	private static boolean isEighthColumnExclusion(final int currentPosition, final int coordinateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((coordinateOffset == -15) || (coordinateOffset == -6) || (coordinateOffset == 10)
			|| (coordinateOffset == 17));
	}
}
