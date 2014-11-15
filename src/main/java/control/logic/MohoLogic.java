package control.logic;

import java.util.ArrayList;

import model.ModelStore;
import model.Package;
import model.Planet;
import model.SpaceShip;

public class MohoLogic implements Logic {

	private int totalFee=0;
	private SpaceShip ship;
	private String actualPlanetName;
	private String targetPlanetName;
	private ArrayList<Planet> planets;

	public void init(ModelStore modelStrore) {
		this.ship = modelStrore.getSpaceShip();
		this.actualPlanetName = ship.getPlanetName();
		this.planets = modelStrore.getPlanets();
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
		
		double maxFeePerDist = 0;
		Package pack = null;
		for (Package pck : actPlanet.getPackages()) {
			if (!pck.getTargetPlanet().equals(actualPlanetName)) {
				Planet target = getPlanetByName(pck.getTargetPlanet());
				double dist = actPlanet.distFrom(target);
				double feePerDist=((double)pck.getFee())/dist;
				if (feePerDist > maxFeePerDist) {
					pack = pck;
					maxFeePerDist = feePerDist;
				}
			}
		}
		if (pack != null){
			ret[0] = pack.getPackageId();
			totalFee+=pack.getFee();
			System.out.println("MohoFee: "+pack.getFee()+" (total: "+totalFee+")");
			targetPlanetName=pack.getTargetPlanet();
		}else{
			//hát ha ebbe belefut akkor a bolygón minden csomag target csomag
			//TODO: goto random planet
		}

		return ret;
	}

	public String go() {
		return targetPlanetName;
	}

	private Planet getPlanetByName(String name){
		Planet actPlanet = null;
		for (Planet p : planets) {
			if (p.getName().equals(name)) {
				actPlanet = p;
				break;
			}
		}
		return actPlanet;
	}
	
}
