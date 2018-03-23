package fr.imie.labyrinthsgenerator;


public class LabyrinthDisplayer {

	// Show the entire Labyrinth given in parameter
	public static void displayLabyrinth(Labyrinth lab) {
		for (Character cT[] : lab.getField()) {
			for (Character c : cT) {
				System.out.print(c);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	// Show all labyrinths from a Generator
	public static void displayLabyrinths(Generator gen) {
		for (Labyrinth lab : gen.getLab()) {
			displayLabyrinth(lab);
		}
	}
}
