package com.chess.engine.pieces;

import com.chess.engine.board.*;
import com.chess.engine.Alliance;
import com.chess.engine.board.Move.*;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class Queen extends Piece {
	private static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9,-8,-7,-1,1,7,8,9};
	
	public Queen(final Alliance pieceAlliance, final int piecePosition){
		super(PieceType.QUEEN, piecePosition, pieceAlliance,true);
	}
	public Queen(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove){
		super(PieceType.QUEEN, piecePosition, pieceAlliance,isFirstMove);
	}
	@Override
	public Collection<Move> calculateLegalMoves(final Board board){
		final List<Move> legalMoves = new ArrayList<>();
		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
			int candidateDestinationCoordinate = this.piecePosition;
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				if(isFirstColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)||
					isEighthColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)){
					break;
				}
				candidateDestinationCoordinate += candidateCoordinateOffset;
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
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
						break;
					}
				}
			}
		}
		return Collections.unmodifiableCollection(legalMoves);
	}
	@Override
	public Queen movePiece(final Move move){
		return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	@Override
	public String toString(){
		return PieceType.QUEEN.toString();
	}
	private static boolean isFirstColumnExclusion(final int currentPosition, final int coordinateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (coordinateOffset == -1 || coordinateOffset == -9 || coordinateOffset == 7);
	}
	private static boolean isEighthColumnExclusion(final int currentPosition, final int coordinateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (coordinateOffset == 1 || coordinateOffset == -7 || coordinateOffset == 9);
	}
}
