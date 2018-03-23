package fr.imie.labyrinthsgenerator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.imie.labyrinthssolver.Solver;

public class Generator {

	private String mode;
	private ArrayList<Labyrinth> lab;
	private String saveFile;
	private int labWidth;
	private int labHeight;

	// Gen constructor for single mode
	public Generator(String mode, int width, int height, String fileName) {
		try {
			if (!mode.equals("single")) {
				throw new Exception("The mode is unknown.");
			}
			this.mode = mode;
		} catch (Exception e) {
			System.err.println("The selected mode don't exists (only single or multiple)");
		}
		this.lab = new ArrayList<Labyrinth>();
		this.labWidth = width;
		this.labHeight = height;
		this.genLabyrinth();
		this.saveFile = fileName;
	}

	// Gen constructor for multiple mode
	public Generator(String mode, int labNb, int width, int height, String zipName) {
		try {
			if (!mode.equals("multiple")) {
				throw new Exception("The mode is unknown.");
			}
			this.mode = mode;
		} catch (Exception e) {
			System.err.println("The selected mode don't exists (only single or multiple)");
		}
		this.lab = new ArrayList<Labyrinth>();
		this.labWidth = width;
		this.labHeight = height;
		for (int i = 0; i < labNb; i++) {
			this.genLabyrinth();
		}
		this.saveFile = zipName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public ArrayList<Labyrinth> getLab() {
		return lab;
	}

	public void setLab(ArrayList<Labyrinth> lab) {
		this.lab = lab;
	}

	public String getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}

	public int getLabWidth() {
		return labWidth;
	}

	public void setLabWidth(int labWidth) {
		this.labWidth = labWidth;
	}

	public int getLabHeight() {
		return labHeight;
	}

	public void setLabHeight(int labHeight) {
		this.labHeight = labHeight;
	}

	// Method to generate valid labyrinths, if not, generate again
	public void genLabyrinth() {
		Solver solv = new Solver(null);
		Labyrinth tLab;
		do {
			do {
				tLab = new Labyrinth(labWidth, labHeight);
				tLab.placeLabels();
			} while (!this.checkDiffLabels(tLab));
			tLab.placeExternWall();
			tLab.placeInternWall();
			tLab.placeFeasible();
			
		} while (solv.CalculatePath(tLab) == null);
		this.lab.add(tLab);
	}
	
	//Check for each Labyrinths if the start point and the goal point are different 
	public boolean checkDiffLabels(Labyrinth lab) {
		boolean diff = true;
		for (Labyrinth gLab : this.lab) {
			diff = ((gLab.getsPos()[0] != lab.getsPos()[0] || gLab.getsPos()[1] != lab.getsPos()[1]) && (gLab.getgPos()[0] != lab.getgPos()[0] || gLab.getgPos()[1] != lab.getgPos()[1])) ? diff : false;
		}
		return diff;
	}
	
	// Save all the labyrinths of the Generator
	public int saveLabyrinths() {
		Integer nb = 1;
		for (Labyrinth lab : this.lab) {
			try {
				ObjectOutputStream oOS = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream(new File(this.saveFile + nb.toString()))));
				oOS.writeObject(lab);
				oOS.close();
			} catch (FileNotFoundException e) {
				System.err.println(
						"An error occured with the file opening/creating, the file may not exists or proper rights aren't set.");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println(
						"An error occured with the object writing, the object may not implements Serializable.");
				e.printStackTrace();
			}
			nb++;
		}
		return --nb;
	}

	// Compress and archive all the labyrinth files
	public void zipLabyrinths(int fileNb) {
		try {
			FileOutputStream file = new FileOutputStream(this.saveFile);
			BufferedOutputStream bOF = new BufferedOutputStream(file);
			ZipOutputStream zOF = new ZipOutputStream(bOF);
			zOF.setMethod(ZipOutputStream.DEFLATED);
			zOF.setLevel(9);
			byte buff[] = new byte[2048];
			for (Integer i = 1; i <= fileNb; i++) {
				String fName = this.saveFile + i.toString();
				FileInputStream fIS = new FileInputStream(fName);
				BufferedInputStream bIS = new BufferedInputStream(fIS);
				ZipEntry zE = new ZipEntry(fName);
				zOF.putNextEntry(zE);
				int read;
				while ((read = bIS.read(buff)) != -1) {
					zOF.write(buff, 0, read);
				}
				zOF.closeEntry();
				bIS.close();
				new File(fName).delete();
			}
			zOF.close();
		} catch (FileNotFoundException e) {
			System.err.println("An error occured with the zip file, it may not exists.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("An error occured with the linking of zip entries.");
			e.printStackTrace();
		}
	}
}
