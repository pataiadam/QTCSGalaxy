package control.logic;

import java.util.ArrayList;
import java.util.PriorityQueue;

import model.ModelStore;
import model.Package;
import model.Planet;
import model.SpaceShip;
import control.Main;
import control.logic.brutal.Node;
import control.logic.brutal.NodeComp;

public class BrutalMohoLogic implements Logic {

	private SpaceShip ship;
	private ModelStore ms;
	private String actualPlanetName;
	private ArrayList<Planet> planets;
	private PriorityQueue<Node> q;
	private Node route;
	private int minFee;
	private boolean first;
	private int droppedCount;

	public void init(ModelStore modelStrore) {
		this.ms = modelStrore;
		this.ship = modelStrore.getSpaceShip();
		this.actualPlanetName = ship.getPlanetName();
		this.planets = modelStrore.getPlanets();
		q = new PriorityQueue<Node>(new NodeComp());
		minFee = 0;
	}

	public int[] drop() {
		int[] ret = new int[3];
		droppedCount = 0;

		if (route == null) {
			for (Package p : ship.getPackages()) {
				ret[droppedCount] = p.getPackageId();
				droppedCount++;

			}
			return ret;
		}

		for (Package p : ship.getPackages()) {
			if (p.getTargetPlanet().equals(actualPlanetName)) {
				ret[droppedCount] = p.getPackageId();
				droppedCount++;
			}
		}
		return ret;
	}

	public int[] pick() {
		Planet pl = ms.getPlanetByName(ship.getPlanetName());

		if (Main.DEBUG) {
			System.out.println("Jelenlegi bolygó: " + pl.getName());
			System.out.println("Ahova még menni kell:");
			for (Package p : ship.getPackages()) {
				System.out.println("     - " + p.getTargetPlanet());
			}
		}
		/**
		 * Itt kezdődik a lényeg: ha a hajó üres, vagy a tervezett útvonal null,
		 * akkor indul a Brutalmoho
		 */
		if (ship.getPackages().size() == droppedCount || route == null) {
			System.out.println("BrutalMOHÓ indul");
			/**
			 * Minden csomagra az aktuális bolygón:
			 */
			for (Package p : pl.getPackages()) {
				// szűrhetünk értékre, de ez most 0
				if (p.getFee() > minFee) {
					// betesszük a prioritysorba egyedül
					q.add(new Node(ms, p));
					for (Package p2 : pl.getPackages()) {
						if (p2.getFee() > minFee) {
							if (p2.getPackageId() != p.getPackageId()) {
								// majd betesszük egy másik csomaggal is
								q.add(new Node(ms, p, p2));
								for (Package p3 : pl.getPackages()) {
									if (p3.getFee() > minFee) {
										if (p3.getPackageId() != p2
												.getPackageId()
												&& p3.getPackageId() != p
														.getPackageId()) {
											Node c = new Node(ms, p, p2, p3);
											// majd egy harmadikkal is
											q.add(c);
										}
									}
								}
							}
						}
					}
				}
			}

			System.out.println("BrutalMohó vége: " + q.size());
			// ennek csak akkor van értelme ha a minfee nem 0
			if (q.size() == 0) {
				minFee -= 5;
				return pick();
			}
			// az útvonalat a Node route tartalmazza
			this.route = q.remove();
			// a getPicks megadja a felvenni kívánt csomagokat
			return route.getPicks();

		}
		/**
		 * Elvileg ez az ág lenne az, ami adna plusz csomagokat a hajóra ha
		 * azokat megérné útközben elvinni... valamiért nem megy
		 */
		else {
			Package npack = route.getNextPack(pl.getName());
			if (npack == null) {
				return null;
			}
			Planet next = ms.getPlanetByName(npack.getTargetPlanet());

			// 1 2 3
			double sheep = ship.getPackages().size();
			double[] speed = { 170, 150, 130, 110 };

			if (sheep == 3) {
				return null;
			}
			double feenopick = npack.getFee();
			double speednopick = speed[(int) sheep];
			double distnopick = ms.getDistanceByName(pl.getName(),
					next.getName());
			if (Main.DEBUG) {
				System.out.println(pl.getName() + " " + next.getName()
						+ " ::: " + distnopick);
			}
			double nopickH = (speednopick * feenopick) / (distnopick + 1);

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
				if (Main.DEBUG)
					System.out.println("ph: " + pickH + " nph: " + nopickH);
				if (pickH > nopickH) {
					if (pickMax < pickH) {
						if (Main.DEBUG)
							System.out
									.println("Közben felszedünk egy csomagot, mert elvinni jobb("
											+ pickH
											+ ") mint nem("
											+ nopickH
											+ ")");
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
