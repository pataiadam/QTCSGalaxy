package model;

import java.util.ArrayList;

import control.api.RestAPI;

public class ModelStore {

	private ArrayList<Planet> planets;
	private ArrayList<Package> shipsPackages;
	private ArrayList<Package> planetsPackages;
	
	
	
	public void refreshModel(RestAPI galaxy) {
		/*
		 * megkapja az apit, és szépen belövi az adattagokat
		 */
	}

	public ArrayList<Planet> getPlanets() {
		return planets;
	}

	public ArrayList<Package> getShipsPackages() {
		return shipsPackages;
	}

	public ArrayList<Package> getPlanetsPackages() {
		return planetsPackages;
	}
	
}
