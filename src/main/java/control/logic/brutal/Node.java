package control.logic.brutal;

import java.util.Arrays;
import java.util.LinkedList;

import model.ModelStore;
import model.Package;

public class Node {
	Package[] packs;
	LinkedList<String> path;
	double h;

	/**
	 * kiszámoljuk H-t. Ha ez magas akkor megéri a csomago(ka)t leszállítani
	 * 
	 * @param m
	 *            - csak a getdistance függvénye miatt kell
	 * @param packages
	 */
	public Node(ModelStore m, Package... packages) {
		packs = packages;
		// sebesség 0, 1, 2, ill 3 csomaggal
		double[] speed = { 170.0, 150.0, 130.0, 110.0 };

		if (packs.length == 1) {
			h = (speed[1] * ((double) packs[0].getFee()))
					/ (packs[0].getTargetPlanetDistance() + 1);
		}
		if (packs.length == 2) {
			double h1 = (speed[2] * ((double) packs[0].getFee()))
					/ (packs[0].getTargetPlanetDistance() + 1);
			double dist12 = m.getDistanceByName(packs[0].getTargetPlanet(),
					packs[1].getTargetPlanet()) + 1;
			double h2 = (speed[1] * ((double) packs[1].getFee())) / dist12;
			h = h1 + h2;
		}
		if (packs.length == 3) {
			double h1 = (speed[3] * ((double) packs[0].getFee()))
					/ (packs[0].getTargetPlanetDistance() + 1);
			double dist12 = m.getDistanceByName(packs[0].getTargetPlanet(),
					packs[1].getTargetPlanet()) + 1;
			double h2 = (speed[2] * ((double) packs[1].getFee())) / dist12;
			double dist23 = m.getDistanceByName(packs[1].getTargetPlanet(),
					packs[2].getTargetPlanet()) + 1;
			double h3 = (speed[1] * ((double) packs[2].getFee())) / dist23;
			h = h1 + h2 + h3;
		}
		setPath();
	}

	private void setPath() {
		for (Package p : packs) {
			if (!path.contains(p.getTargetPlanet())) {
				path.add(p.getTargetPlanet());
			}
		}
	}

	public int[] getPicks() {
		int[] ret = new int[3];
		int i = 0;
		for (Package p : packs) {
			ret[i] = p.getPackageId();
			i++;
		}
		return ret;
	}

	public boolean isEmpty() {
		return path.size() == 0 ? true : false;
	}
	
	public String getNextTarget() {
		return path.pollFirst();
	}

	public Package getNextPack(String act) {
		for (Package p : packs) {
			if (path.peekFirst().equals(p.getTargetPlanet()))
				return p;
		}
		return null;
	}

	public void pastePack(Package p) {
		Package[] newp = Arrays.copyOf(packs, packs.length + 1);
		for (int i = newp.length - 1; i > 0; i--) {
			newp[i] = newp[i - 1];
		}
		newp[0] = p;
		packs = newp;
		
		if (path.contains(p.getTargetPlanet())) {
			path.remove(p.getTargetPlanet());
			path.addFirst(p.getTargetPlanet());
		} else {
			path.addFirst(p.getTargetPlanet());
		}
	}
	
	public void dropPack(Package p){
		Package[] newp = new Package[packs.length-1];
		int i=0;
		for(Package pa : packs){
			if(pa.getPackageId()!=p.getPackageId()){
				newp[i]=pa;
				i++;
			}
		}
		packs = newp;
	}

	public double getH() {
		return h;
	}

	public int getPackSize() {
		return packs.length;
	}
}
