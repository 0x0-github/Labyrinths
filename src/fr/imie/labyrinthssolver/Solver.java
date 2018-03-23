package fr.imie.labyrinthssolver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fr.imie.labyrinthsgenerator.Labyrinth;

public class Solver {

	public static final Character PATH = '+';

	private String loadFile;

	public Solver(String fileName) {
		this.loadFile = fileName;
	}

	public String getLoadFile() {
		return loadFile;
	}

	public void setLoadFile(String loadFile) {
		this.loadFile = loadFile;
	}

	// Load a Labyrinth object from a file (name = this.loadFile)
	public Labyrinth loadLabyrinthFile() {
		try {
			FileInputStream file = new FileInputStream(this.loadFile);
			ObjectInputStream oIS = new ObjectInputStream(file);
			Labyrinth lab = (Labyrinth) oIS.readObject();
			oIS.close();
			return lab;
		} catch (IOException e) {
			System.err.println("An error occured with the object/file reading.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("An error occured with the object convertion, the class may not exists.");
			e.printStackTrace();
		}
		return null;
	}

	// Load a Labyrinth object from the file given in parameter
	public Labyrinth loadLabyrinthFile(String fName) {
		try {
			FileInputStream file = new FileInputStream(fName);
			ObjectInputStream oIS = new ObjectInputStream(file);
			Labyrinth lab = (Labyrinth) oIS.readObject();
			oIS.close();
			return lab;
		} catch (IOException e) {
			System.err.println("An error occured with the object/file reading.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("An error occured with the object convertion, the class may not exists.");
			e.printStackTrace();
		}
		return null;
	}

	// Load Labyrinths from a zip file (name = this.loadFile)
	public ArrayList<Labyrinth> loadLabyrinthZip() {
		try {
			ZipEntry zE;
			byte buff[] = new byte[2048];
			ArrayList<Labyrinth> labLst = new ArrayList<Labyrinth>();
			ZipInputStream zIS = new ZipInputStream(new BufferedInputStream(new FileInputStream(this.loadFile)));
			while ((zE = zIS.getNextEntry()) != null) {
				File f = new File(zE.getName());
				BufferedOutputStream bOS = new BufferedOutputStream(new FileOutputStream(f));
				int read;
				while ((read = zIS.read(buff)) != -1) {
					bOS.write(buff, 0, read);
				}
				bOS.flush();
				labLst.add(this.loadLabyrinthFile(f.getName()));
				bOS.close();
			}
			zIS.close();
			return labLst;
		} catch (FileNotFoundException e) {
			System.err.println("The file can not be found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("An error occured with the object/file reading.");
			e.printStackTrace();
		}
		return null;
	}

	// Working on the equivalent of the Labyrinth's field given in parameter in a
	// int double array to display the number of moves from the start cell to the
	// last one. If unsolvable, returns null
	public int[][] CalculatePath(Labyrinth lab) {
		int field[][] = lab.getIntField();
		int sPos[] = lab.getsPos();
		int gPos[] = lab.getgPos();
		ArrayList<int[]> list = new ArrayList<int[]>();
		list.add(sPos);
		while (!list.isEmpty()) {
			ArrayList<int[]> nextList = new ArrayList<int[]>();
			for (int[] pos : list) {
				int t[] = { (pos[0] - 1), pos[1] };
				if (field[t[0]][t[1]] == 0 || (field[pos[0]][pos[1]] + 1) < field[t[0]][t[1]]) {
					field[t[0]][t[1]] = (field[pos[0]][pos[1]] + 1);
					nextList.add(t);
				}
				int b[] = { (pos[0] + 1), pos[1] };
				if (field[b[0]][b[1]] == 0 || (field[pos[0]][pos[1]] + 1) < field[b[0]][b[1]]) {
					field[b[0]][b[1]] = (field[pos[0]][pos[1]] + 1);
					nextList.add(b);
				}
				int l[] = { pos[0], (pos[1] - 1) };
				if (field[l[0]][l[1]] == 0 || (field[pos[0]][pos[1]] + 1) < field[l[0]][l[1]]) {
					field[l[0]][l[1]] = (field[pos[0]][pos[1]] + 1);
					nextList.add(l);
				}
				int r[] = { pos[0], (pos[1] + 1) };
				if (field[r[0]][r[1]] == 0 || (field[pos[0]][pos[1]] + 1) < field[r[0]][r[1]]) {
					field[r[0]][r[1]] = (field[pos[0]][pos[1]] + 1);
					nextList.add(r);
				}
			}
			list = nextList;
		}
		field[sPos[0]][sPos[1]] = 0;
		if (field[gPos[0]][gPos[1]] == 0) {
			return null;
		} else {
			return field;
		}
	}

	// Returns an int[] array containing each moves between the start point to the
	// goal point. It starts from the end and decrease 1 by 1 until the start point
	// is reached to get the shortest path
	public ArrayList<int[]> findShortestPath(Labyrinth lab) {
		int sPos[] = lab.getsPos();
		int gPos[] = lab.getgPos();
		ArrayList<int[]> path = new ArrayList<int[]>();
		int field[][] = this.CalculatePath(lab);
		int curr[] = {gPos[0], gPos[1]};
		while (curr[0] != sPos[0] || curr[1] != sPos[1]) {
			boolean breaked = false;
			for (int i = (curr[0] - 1); i <= (curr[0] + 1) && i != -1; i++) {
				for (int j = (curr[1] - 1); j <= (curr[1] + 1) && j != -1; j++) {
					if (field[i][j] == (field[curr[0]][curr[1]] - 1)) {
						curr = new int[] {i, j};
						breaked = true;
						break;
					}
				}
				if (breaked) {
					break;
				}
			}
			if (curr[0] != sPos[0] || curr[1] != sPos[1]) {
				path.add(curr);
			}
		}
		return path;
	}

	// Display the shortest path directly on the Labyrinth
	public void displaySolution(Labyrinth lab) {
		Character field[][] = lab.getField();
		ArrayList<int[]> path = this.findShortestPath(lab);
		for (int pos[] : path) {
			field[pos[0]][pos[1]] = '+';
		}
	}
}
