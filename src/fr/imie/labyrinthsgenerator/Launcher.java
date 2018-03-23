package fr.imie.labyrinthsgenerator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.imie.labyrinthssolver.Solver;

public class Launcher {

	public static void main(String[] args) {
		Generator gen = null;
		// Here we check if parameters have been typed
		try {
			// Depending on the first parameter, invoke the Generator or the Solver
			if (args[0].equals("LabyrinthGenerator")) {
				try {
					if (args.length == 5) {
						gen = new Generator(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[4]);
					} else if (args.length == 6) {
						gen = new Generator(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]),
								Integer.parseInt(args[4]), args[5]);
					} else {
						throw new Exception("Invalid parameters");
					}
				} catch (Exception e) {
					System.err.println("The number of parameter you entered is invalid.");
					e.printStackTrace();
				}
				LabyrinthDisplayer.displayLabyrinths(gen);
				int labNb = gen.saveLabyrinths();
				// Files are zipped if in multiple mode
				if (args[1].equals("multiple")) {
					gen.zipLabyrinths(labNb);
				}
			} else if (args[0].equals("LabyrinthSolver")) {
				Solver solv = null;
				try {
					if (args.length != 2) {
						throw new Exception("Invalid parameter number.");
					} else {
						solv = new Solver(args[1]);
						// If the name entered contains ".zip" we consider it is a zip file else it's a regular file
						Pattern pattern = Pattern.compile(".zip");
						Matcher match = pattern.matcher(solv.getLoadFile());
						if (!match.find()) {
							Labyrinth lab = solv.loadLabyrinthFile();
							LabyrinthDisplayer.displayLabyrinth(lab);
							solv.displaySolution(lab);
							LabyrinthDisplayer.displayLabyrinth(lab);
						} else {
							ArrayList<Labyrinth> labLst = solv.loadLabyrinthZip();
							for (Labyrinth lab : labLst) {
								solv.displaySolution(lab);
								LabyrinthDisplayer.displayLabyrinth(lab);
							}
						}
					}
				} catch (Exception e) {
					System.err.println("The number of parameter you entered is invalid.");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.err.println("You must enter parameters.");
		}
	}

}
