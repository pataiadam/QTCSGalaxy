package model;

import java.util.ArrayList;

public class SpaceShip {

	private String userName;
	private String planetName;
	private String targetPlanetName;
	private int arriveAfterMs;
	private ArrayList<Package> packages;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPlanetName() {
		return planetName;
	}

	public void setPlanetName(String planetName) {
		this.planetName = planetName;
	}

	public String getTargetPlanetName() {
		return targetPlanetName;
	}

	public void setTargetPlanetName(String targetPlanetName) {
		this.targetPlanetName = targetPlanetName;
	}

	public int getArriveAfterMs() {
		return arriveAfterMs;
	}

	public void setArriveAfterMs(int arriveAfterMs) {
		this.arriveAfterMs = arriveAfterMs;
	}

	public ArrayList<Package> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}
}
