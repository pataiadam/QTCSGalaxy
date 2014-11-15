package control.logic;

import java.util.ArrayList;

import model.ModelStore;
import model.Package;
import model.Planet;
import model.SpaceShip;

public class MohoLogic implements Logic {

	private int totalFee = 0;
	private SpaceShip ship;
	private String actualPlanetName;
	private String targetPlanetName;
	private ArrayList<Planet> planets;
	private double[][] distances;
	private boolean firstInit;

	private ArrayList<String> targetStack;

	public void init(ModelStore modelStrore) {
		this.ship = modelStrore.getSpaceShip();
		this.actualPlanetName = ship.getPlanetName();
		this.planets = modelStrore.getPlanets();
		this.targetStack = new ArrayList<String>();
		if (firstInit) {
			firstInit = false;
			setDistances();
		}
	}

	public int[] drop() {
		int[] ret = new int[3];
		int i = 0;
		for (Package p : ship.getPackages()) {
			if (p.getTargetPlanet().equals(actualPlanetName)) {
				ret[i] = p.getPackageId();
				i++;
			}
		}
		return ret;
	}

	public int[] pick() {
		int[] ret = new int[3];
		Planet actPlanet = getPlanetByName(actualPlanetName);

		Package pack = getBestPackage(actPlanet);
		if (pack != null) {
			ret[0] = pack.getPackageId();
			totalFee += pack.getFee();
			System.out.println("MohoFee: " + pack.getFee() + " (total: "
					+ totalFee + ")");
			targetPlanetName = pack.getTargetPlanet();
		} else {
			// TODO: goto random planet
		}

		return ret;
	}

	public String go() {
		//return targetPlanetName;
		return ship.getPackages().get(0).getTargetPlanet();
	}

	private Planet getPlanetByName(String name) {
		Planet actPlanet = null;
		for (Planet p : planets) {
			if (p.getName().equals(name)) {
				actPlanet = p;
				break;
			}
		}
		return actPlanet;
	}

	private void setDistances() {
		int planetNum = planets.size();
		this.distances = new double[planetNum][planetNum];
		for (int i = 0; i < planetNum; i++) {
			for (int k = 0; k < planetNum; k++) {
				if (k == i) {
					this.distances[i][k] = 0.0;
				} else if (distances[i][k] == 0.0) {
					this.distances[i][k] = planets.get(i).distFrom(
							planets.get(k));
					this.distances[k][i] = this.distances[i][k];
				}
			}
		}
	}

	private Package getBestPackage(Planet actPlanet) {
		Package pack = null;
		double maxFeePerDist = 0;
		for (Package pck : actPlanet.getPackages()) {
			if (!pck.getTargetPlanet().equals(actualPlanetName)) {
				Planet target = getPlanetByName(pck.getTargetPlanet());
				double dist = actPlanet.distFrom(target);
				//double feePerDist = ((double) pck.getFee()) / dist;
				double feePerDist = pck.getFee();
				if (feePerDist > maxFeePerDist) {
					pack = pck;
					maxFeePerDist = feePerDist;
				}
			}
		}
		return pack;
	}

}
