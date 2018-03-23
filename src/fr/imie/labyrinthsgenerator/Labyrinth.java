package fr.imie.labyrinthsgenerator;

import java.io.Serializable;

public class Labyrinth implements Serializable {

	private static final long serialVersionUID = 1L;
	// Constants representing each symbols of the labyrinth for a better reading of
	// the code
	public static final Character WALL = 'X';
	public static final Character FEASIBLE = '_';
	public static final Character START = 'S';
	public static final Character GOAL = 'G';

	// The field of the labyrinth is represented by a double dimention's Character
	// array
	private Character field[][];
	// The start and the goal positions are stored in our Labyrinth instances
	private int sPos[];
	private int gPos[];

	public Labyrinth(int x, int y) {
		this.field = new Character[x][y];
	}

	public Character[][] getField() {
		return field;
	}

	public void setField(Character[][] field) {
		this.field = field;
	}

	public int[] getsPos() {
		return sPos;
	}

	public void setsPos(int[] sPos) {
		this.sPos = sPos;
	}

	public int[] getgPos() {
		return gPos;
	}

	public void setgPos(int[] gPos) {
		this.gPos = gPos;
	}

	// Method used to place the boundary of the labyrinth
	public void placeExternWall() {
		for (int i = 0; i < this.field.length; i++) {
			for (int j = 0; j < this.field[0].length; j++) {
				if (i == 0 || i == (this.field.length - 1) || j == 0 || j == (this.field[0].length - 1)) {
					this.field[i][j] = WALL;
				}
			}
		}
	}

	// Method that returns a couple of int representing a empty cell of the
	// labyrinth
	public int[] getAnEmptyCell() {
		int xTemp = 0, yTemp = 0;
		do {
			xTemp = (int) (Math.random() * (this.field.length));
			yTemp = (int) (Math.random() * (this.field[0].length));

		} while (this.field[xTemp][yTemp] != null);
		return new int[] { xTemp, yTemp };
	}

	// Display the intern walls giving the number of these in parameter
	public void placeInternWall(int nb) {
		for (int i = 0; i < nb; i++) {
			int emptyCell[];
			boolean valid;
			do {
				valid = false;
				emptyCell = this.getAnEmptyCell();
				for (int x = (emptyCell[0] - 1); x <= (emptyCell[0] + 1); x++) {
					for (int y = (emptyCell[1] - 1); y <= (emptyCell[1] + 1); y++) {
						if (x != emptyCell[0] && y != emptyCell[1]) {
							valid = (this.field[x][y] == null) ? true : false;
						}
					}
				}
			} while (!valid);
			this.field[emptyCell[0]][emptyCell[1]] = WALL;
		}
	}

	// Display the intern walls by default (nb of wall = len(dim1) + len(dim2))
	public void placeInternWall() {
		for (int i = 0; i < (this.field.length + this.field[0].length); i++) {
			int emptyCell[] = this.getAnEmptyCell();
			this.field[emptyCell[0]][emptyCell[1]] = WALL;
		}
	}

	// Display the feasible characters on the field
	public void placeFeasible() {
		for (int i = 1; i < (this.field.length - 1); i++) {
			for (int j = 1; j < (this.field[0].length - 1); j++) {
				this.field[i][j] = (this.field[i][j] == null) ? FEASIBLE : this.field[i][j];
			}
		}
	}

	// Method that returns a couple of free positions to place labels in the
	// labyrinth
	public int[] getValidLabelCell() {
		int rdmPos[] = new int[2];
		do {
			rdmPos[0] = (int) (Math.random() * (this.field.length - 2) + 1);
			rdmPos[1] = (int) (Math.random() * (this.field[0].length - 2) + 1);
		} while (this.field[rdmPos[0]][rdmPos[1]] != null);
		return rdmPos;
	}

	// Method which place the start and goal labels around the bound of the
	// labyrinth. For higher difficulty, the position between both should be far
	// from len(dim1)/3 len(dim2)/3
	public void placeLabels() {
		int sRdmPos[], gRdmPos[];
		int biggest[] = new int[2], smallest[] = new int[2];
		boolean valid;
		do {
			valid = false;
			sRdmPos = this.getValidLabelCell();
			gRdmPos = this.getValidLabelCell();
			for (int i = 0; i < sRdmPos.length; i++) {
				biggest[i] = (sRdmPos[i] > gRdmPos[i]) ? sRdmPos[i] : gRdmPos[i];
				smallest[i] = (sRdmPos[i] < gRdmPos[i]) ? sRdmPos[i] : gRdmPos[i];
			}
			valid = ((biggest[0] - smallest[0]) > this.field.length / 3
					&& (biggest[1] - smallest[1]) > this.field[0].length / 3) ? true : valid;
		} while (!valid);
		this.sPos = sRdmPos;
		this.field[sRdmPos[0]][sRdmPos[1]] = START;
		this.gPos = gRdmPos;
		this.field[gRdmPos[0]][gRdmPos[1]] = GOAL;
	}

	// Returns an int double array corresponding to it's own labyrinth field (easier
	// to calculate the shortest path later)
	public int[][] getIntField() {
		int field[][] = new int[this.field.length][this.field[0].length];
		for (int i = 0; i < this.field.length; i++) {
			for (int j = 0; j < this.field[0].length; j++) {
				if (this.field[i][j].compareTo(WALL) == 0) {
					field[i][j] = -1;
				} else {
					field[i][j] = 0;
				}
			}
		}
		return field;
	}
}
