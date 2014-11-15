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

		if (ship.getPackages().size() == 0) {
			Package pack = getBestPackage(actPlanet);
			Package pack2 = getSecondPackage(actPlanet, pack);

			if (pack != null) {
				ret[0] = pack.getPackageId();
				totalFee += pack.getFee();
				System.out.println("MohoFee: " + pack.getFee() + " (total: "
						+ totalFee + ")");
				targetStack.add(pack.getTargetPlanet());
			}  
			if (pack2 != null){
				ret[1] = pack2.getPackageId();
				totalFee += pack2.getFee();
				System.out.println("MohoFee: " + pack.getFee() + " (total: "
						+ totalFee + ")");
				targetStack.add(pack2.getTargetPlanet());
			}
		}

		return ret;
	}

	public String go() {
		String target = targetStack.get(targetStack.size() - 1);
		targetStack.remove(targetStack.size() - 1);
		return target;
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
				double feePerDist = ((double) pck.getFee()) / dist;
				if (feePerDist > maxFeePerDist) {
					pack = pck;
					maxFeePerDist = feePerDist;
				}
			}
		}
		return pack;
	}

	private Package getSecondPackage(Planet actPlanet, Package bpack) {
		// A - actPlanet, B - target, C - middle planet
		Package pack = null;
		Planet B = getPlanetByName(bpack.getTargetPlanet());
		double Bf = bpack.getFee();
		// timeWoSp
		double timeAB = distances[actPlanet.getIndex()][B.getIndex()] / 6;
		double timevalueRateAB = Bf / timeAB;
		double maxRate = 0.0;
		double z = 0.0;
		for (Package p : actPlanet.getPackages()) {
			if (!p.equals(bpack)) {
				if (!p.getTargetPlanet().equals(actualPlanetName)) {
					Planet C = getPlanetByName(p.getTargetPlanet());
					double Cf = p.getFee();
					double timeBC = distances[B.getIndex()][C.getIndex()] / 8;
					double timeAC = distances[actPlanet.getIndex()][C
							.getIndex()] / 8;
					double timeWithSecondPack = timeAC + timeBC;
					double rate = (Bf + Cf) / timeWithSecondPack;

					if (rate > timevalueRateAB && rate > maxRate) {
						maxRate = rate;
						pack = p;
					}

				}
			}
		}
		return pack;
	}

}
