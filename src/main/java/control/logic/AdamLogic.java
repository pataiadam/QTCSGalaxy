package control.logic;

import java.util.ArrayList;
import java.util.Random;

import model.ModelStore;
import model.Planet;
import model.SpaceShip;

public class AdamLogic implements Logic {

	ModelStore m;

	public void init(ModelStore modelStrore) {
		m=modelStrore;
	}

	public int[] drop() {
		int[] t = new int[1];
		t[0] = 5138166;
		return t;
	}

	public int[] pick() {
		int[] t = new int[1];
		t[0] = 5138166;
		return t;
	}

	public String go() {
		return getRandomPlanet();
	}

	private String getRandomPlanet(){
		Random r = new Random();
		return m.getPlanets().get(r.nextInt(m.getPlanets().size())).getName();
	}
	
	private String getNearest(SpaceShip spaceShip, ArrayList<Planet> planets) {
		double min = Double.MAX_VALUE;
		Planet origin=null;
		for (Planet planet : planets) {
			if(spaceShip.getPlanetName().equals(planet.getName())){
				origin=planet;
				break;
			}
		}
		String ret="";
		for (Planet planet : planets) {
			if(origin.distFrom(planet)<min && !origin.equals(planet)){
				ret=planet.getName();
				min=origin.distFrom(planet);
			}
		}
		
		return ret;
	}

}
