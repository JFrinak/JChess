package com.chess.engine.pieces;

import com.chess.engine.board.*;
import com.chess.engine.board.Move.*;
import com.chess.engine.Alliance;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class King extends Piece {
	private static int[] CANDIDATE_MOVE_COORDINATES = {-9,-8,-7,-1,1,7,8,9};
	private final boolean isCastled;
	private final boolean kingSideCastleCapable;
	private final boolean queenSideCastleCapable;

	public King(final Alliance alliance,
				final int piecePosition,
				final boolean kingSideCastleCapable,
				final boolean queenSideCastleCapable) {
		super(PieceType.KING, piecePosition, alliance, true);
		this.isCastled = false;
		this.kingSideCastleCapable = kingSideCastleCapable;
		this.queenSideCastleCapable = queenSideCastleCapable;
	}

	public King(final Alliance alliance,
				final int piecePosition,
				final boolean isFirstMove,
				final boolean isCastled,
				final boolean kingSideCastleCapable,
				final boolean queenSideCastleCapable) {
		super(PieceType.KING, piecePosition, alliance, isFirstMove);
		this.isCastled = isCastled;
		this.kingSideCastleCapable = kingSideCastleCapable;
		this.queenSideCastleCapable = queenSideCastleCapable;
	}

	public boolean isCastled(){
		return this.isCastled;
	}
	@Override
	public Collection<Move> calculateLegalMoves(final Board board){
		final List<Move> legalMoves = new ArrayList<>();
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffset)||
				isEighthColumnExclusion(this.piecePosition,currentCandidateOffset)){
				continue;
			}
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
				}
			}
		}
		return Collections.unmodifiableCollection(legalMoves);
	}
	@Override
	public King movePiece(final Move move){
		return new King(move.getMovedPiece().getPieceAlliance(),
						move.getDestinationCoordinate(),
						false,
						move.isCastlingMove(),
						false,
						false);
	}
	@Override
	public String toString(){
		return PieceType.KING.toString();
	}
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof King)) {
			return false;
		}
		if (!super.equals(other)) {
			return false;
		}
		final King king = (King) other;
		return isCastled == king.isCastled;
	}
	@Override
	public int hashCode() {
		return (31 * super.hashCode()) + (isCastled ? 1 : 0);
	}
	private static boolean isFirstColumnExclusion(final int currentPosition, final int coordinateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && ((coordinateOffset == -9) || (coordinateOffset == -1) || (coordinateOffset == 7));
	}
	private static boolean isEighthColumnExclusion(final int currentPosition, final int coordinateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((coordinateOffset == -7) || (coordinateOffset == 1) || (coordinateOffset == 9));
	}

	public boolean isKingSideCastleCapable() {
		return this.kingSideCastleCapable;
	}

	public boolean isQueenSideCastleCapable() {
		return this.queenSideCastleCapable;
	}
	public boolean getIsCastled(){
		return this.isCastled;
	}
}
