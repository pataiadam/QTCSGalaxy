package control.logic;

import java.util.ArrayList;

import model.ModelStore;
import model.Package;
import model.Planet;
import model.SpaceShip;
import control.logic.adam.Node;
import control.logic.adam.Reckoner;

public class AdamLogic implements Logic {

	private ModelStore modelStore;
	private ArrayList<Planet> planets;
	private SpaceShip ship;
	private Reckoner r = null;
	private String targetPlanet;
	
	private int total=0;

	public void init(ModelStore modelStrore) {
		this.modelStore = modelStrore;
		this.planets = modelStrore.getPlanets();
		this.ship = modelStrore.getSpaceShip();
	}

	public int[] drop() {
		int[] ret = new int[3];
		/**
		 * ha valamiért elszállt, akkor lehet lesz a hajón felesleges csomag
		 */
		if (r == null) {
			int i = 0;
			for (Package p : ship.getPackages()) {
				ret[i] = p.getPackageId();
				i++;
			}
		} else {
			int i = 0;
			for (Package p : ship.getPackages()) {
				if (p.getTargetPlanet().equals(ship.getPlanetName())) {
					ret[i] = p.getPackageId();
					i++;
				}
			}
		}
		return ret;
	}

	public int[] pick() {
		int[] t = new int[3];
		if (r == null) {
			t = moho();
			for (model.Package p : getPlanet(ship.getPlanetName())
					.getPackages()) {
				if (t[0] != 0) {
					if (p.getPackageId() == t[0]) {
						targetPlanet = p.getTargetPlanet();
						total+=p.getFee();
						break;
					}
				} else {
					System.err.println("A moho nem adott vissza packot :=(");
				}
			}
		} else {
			r.setShutdown(true);
			Node n = r.getBestNode();
			while (n.getParent() != null) {
				n = n.getParent();
			}
			if (n.getPack().getPackageId() != 0) {
				t[0] = n.getPack().getPackageId();
				targetPlanet = n.getPack().getTargetPlanet();
				total+=n.getPack().getFee();
			} else {
				System.err.println("Az Adam nem adott vissza packot :=(");
			}
		}
		System.out.println("          >>> AdamTotalGold: "+total+" <<<");
		return t;
	}

	private int[] moho() {
		int[] ret = new int[3];
		MohoLogic moho = new MohoLogic();
		moho.init(this.modelStore);
		return moho.pick();
	}

	public String go() {
		r = new Reckoner(targetPlanet, planets);
		r.run();
		return null;
	}

	private Planet getPlanet(String targetPlanet) {
		for (Planet p : planets) {
			if (p.getName().equals(targetPlanet)) {
				return p;
			}
		}
		return null;
	}

}
