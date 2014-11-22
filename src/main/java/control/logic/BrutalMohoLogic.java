package control.logic;

import java.util.ArrayList;
import java.util.PriorityQueue;

import model.ModelStore;
import model.Package;
import model.Planet;
import model.SpaceShip;
import control.logic.brutal.Node;
import control.logic.brutal.NodeComp;

public class BrutalMohoLogic implements Logic {

	private SpaceShip ship;
	private ModelStore ms;
	private String actualPlanetName;
	private ArrayList<Planet> planets;
	private PriorityQueue<Node> q;
	private Node route;

	public void init(ModelStore modelStrore) {
		this.ms = modelStrore;
		this.ship = modelStrore.getSpaceShip();
		this.actualPlanetName = ship.getPlanetName();
		this.planets = modelStrore.getPlanets();
		q = new PriorityQueue<Node>(new NodeComp());
	}

	public int[] drop() {
		int[] ret = new int[3];
		int i = 0;

		if (route == null) {
			for (Package p : ship.getPackages()) {
				ret[i] = p.getPackageId();
				i++;

			}
			return ret;
		}

		for (Package p : ship.getPackages()) {
			if (p.getTargetPlanet().equals(actualPlanetName)) {
				ret[i] = p.getPackageId();
				i++;
			}
		}
		return ret;
	}

	public int[] pick() {

		Planet pl = ms.getPlanetByName(ship.getPlanetName());
		if (ship.getPackages().size() == 0) {

			int minFee = 30;
			System.out.println("BrutalMOHÓ indul");
			for (Package p : pl.getPackages()) {
				if (p.getFee() > minFee) {
					q.add(new Node(ms, p));
					for (Package p2 : pl.getPackages()) {
						if (p2.getFee() > minFee) {
							if (p2.getPackageId() != p.getPackageId()) {
								q.add(new Node(ms, p, p2));
								for (Package p3 : pl.getPackages()) {
									if (p3.getFee() > minFee) {
										if (p3.getPackageId() != p2
												.getPackageId()
												&& p3.getPackageId() != p
														.getPackageId()) {
											Node c = new Node(ms, p, p2, p3);
											q.add(c);
											// System.out.println("3: "+c.getH());
										}
									}
								}
							}
						}
					}
				}
			}

			System.out.println("BrutalMohó vége: " + q.size());
			this.route = q.remove();
			// System.out.println("sfss: "+route.getH());
			return route.getPicks();
		} else {
			Planet next = ms.getPlanetByName(route.getNextPack()
					.getTargetPlanet());
			// 1 2 3
			double sheep = ship.getPackages().size();
			double[] speed = { 170, 150, 130, 110 };
			
			if(sheep==3){
				return null;
			}
			double feenopick = route.getNextPack().getFee();
			double speednopick = speed[(int) sheep];
			double distnopick = ms.getDistanceByName(pl.getName(),
					next.getName());
			double nopickH = (speednopick * feenopick) / distnopick;

			double pickMax = 0;
			Package pick = null;
			// pl -> p.target -> next
			for (Package p : pl.getPackages()) {
				double dist12 = ms.getDistanceByName(pl.getName(),
						p.getTargetPlanet());
				double dist23 = ms.getDistanceByName(p.getTargetPlanet(),
						next.getName());

				double feepick = p.getFee();
				double speedpick = speed[(int) sheep + 1];
				double pickH = (speedpick * feepick) / (dist12 + 1)
						+ ((speedpick + 20) * feenopick) / (dist23 + 1);
				if (pickH > nopickH) {
					if (pickMax < pickH) {
						System.out
								.println("Közben felszedünk egy csomagot, mert elvinni jobb("
										+ pickH + ") mint nem(" + nopickH + ")");
						pickMax = pickH;
						pick = p;
					}
				}
			}
			if (pick != null) {
				route.pastePack(pick);
				int[] k = new int[1];
				k[0] = pick.getPackageId();
				return k;
			}
		}
		return null;
	}

	public String go() {
		return route.getNext();
	}

}
