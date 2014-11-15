package model;

import java.util.ArrayList;

public class Planet {
	private String name;
	private double x;
	private double y;
	private ArrayList<Package> packages;
	private int index;

	public Planet() {
		this.packages = new ArrayList<Package>();
	}

	public Planet(String name, double x, double y, ArrayList<Package> packages) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.packages = packages;
	}

	public Package takePackage(int index) {
		Package temp = this.packages.get(index);
		this.packages.remove(index);
		return temp;
	}

	// Miért tennénk vissza??
	public boolean putPackage(Package pck) {
		if (pck.getTargetPlanet().equals(this.name)) {
			this.packages.add(pck);
			return true;
		}
		return false;
	}

	public Planet getCopy() {
		Planet planet = new Planet(this.name, this.x, this.y,
				new ArrayList<Package>());
		for (Package pck : this.packages) {
			planet.packages.add(pck);
		}
		planet.setIndex(this.index);
		return planet;
	}

	public boolean sameAs(Planet planet) {
		return planet.name.equals(this.name);
	}

	public double distFrom(Planet planet) {
		return Math.sqrt(Math.pow((planet.x - this.x), 2.0)
				+ Math.pow((planet.y - this.y), 2.0));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public ArrayList<Package> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}