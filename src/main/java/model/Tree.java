package model;

import java.util.ArrayList;

public class Tree {
	private int income;
	private double time;
	private ArrayList<Planet> actualGalaxyState;
	private Planet actualPlanet;
	private ArrayList<Package> packagesCarried;
	private ArrayList<Tree> children;
	private Tree parent = null;
	private boolean doneSomething = false;

	public Tree() {
		this.packagesCarried = new ArrayList<Package>();
		this.children = new ArrayList<Tree>();
	}
	
	public String printNode(){
		String s = "\nTree\n";
		s += "actualPlanet: " + this.actualPlanet.getName() + "\n";
		s += "income: " + this.income + "\n";
		s += "time: " + this.time + "\n";
		s += "carriedPackages: \n";
		for (Package pck : this.packagesCarried) {
			s += pck.getPackageId() + " (" + pck.getText() + ")\n";
		}
		return s;
	}

	// NAGY FÁNÁL STACKOVERFLOW!
	public String toString() {
		String s = "\nTree\n";
		s += "actualPlanet: " + this.actualPlanet.getName() + "\n";
		s += "income: " + this.income + "\n";
		s += "time: " + this.time + "\n";
		s += "carriedPackages: \n";
		for (Package pck : this.packagesCarried) {
			s += pck.getPackageId() + " (" + pck.getText() + ")\n";
		}
		for (Tree tree : this.children) {
			s += tree;
		}
		return s;
	}
	
	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public ArrayList<Planet> getActualGalaxyState() {
		return actualGalaxyState;
	}

	public void setActualGalaxyState(ArrayList<Planet> actualGalaxyState) {
		this.actualGalaxyState = actualGalaxyState;
	}

	public Planet getActualPlanet() {
		return actualPlanet;
	}

	public void setActualPlanet(Planet actualPlanet) {
		this.actualPlanet = actualPlanet;
	}

	public ArrayList<Package> getPackagesCarried() {
		return packagesCarried;
	}

	public void setPackagesCarried(ArrayList<Package> packagesCarried) {
		this.packagesCarried = packagesCarried;
	}

	public ArrayList<Tree> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Tree> children) {
		this.children = children;
	}

	public Tree getParent() {
		return parent;
	}

	public void setParent(Tree parent) {
		this.parent = parent;
	}

	public boolean isDoneSomething() {
		return doneSomething;
	}

	public void setDoneSomething(boolean doneSomething) {
		this.doneSomething = doneSomething;
	}
}