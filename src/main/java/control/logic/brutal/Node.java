package control.logic.brutal;

import java.util.Arrays;

import model.ModelStore;
import model.Package;

public class Node {
	Package[] packs;
	double h;
	
	public Node(ModelStore m, Package...packages) {
		int i=0;
		packs=packages;
		double[] speed = {170.0, 150.0, 130.0, 110.0};
		if(packs.length==1){
			h=(speed[1]*((double)packs[0].getFee()))/(packs[0].getTargetPlanetDistance()+1);
		}
		if(packs.length==2){
			double h1=(speed[2]*((double)packs[0].getFee()))/(packs[0].getTargetPlanetDistance()+1);
			double dist12=m.getDistanceByName(packs[0].getTargetPlanet(), packs[1].getTargetPlanet())+1;
			double h2=(speed[1]*((double)packs[1].getFee()))/dist12;
			h=h1+h2;
		}
		if(packs.length==3){
			double h1=(speed[3]*((double)packs[0].getFee()))/(packs[0].getTargetPlanetDistance()+1);
			double dist12=m.getDistanceByName(packs[0].getTargetPlanet(), packs[1].getTargetPlanet())+1;
			double h2=(speed[2]*((double)packs[1].getFee()))/dist12;
			double dist23=m.getDistanceByName(packs[1].getTargetPlanet(), packs[2].getTargetPlanet())+1;
			double h3=(speed[1]*((double)packs[2].getFee()))/dist23;
			h=h1+h2+h3;
		}
	}

	public int[] getPicks() {
		int[] ret = new int[3];
		int i=0;
		for(Package p : packs){
			ret[i]=p.getPackageId();
			i++;
		}
		return ret;
	}

	public String getNext() {
		String ret = packs[0].getTargetPlanet();
		Package[] newp = Arrays.copyOfRange(packs, 1, packs.length);
		packs=newp;
		return ret;
	}
	
	public Package getNextPack(){
		return packs[0];
	}
	
	public void pastePack(Package p){
		Package[] newp = Arrays.copyOf(packs, packs.length+1);
		for(int i=newp.length-1; i>0; i--){
			newp[i]=newp[i-1];
		}
		newp[0]=p;
		packs=newp;
	}

	public double getH() {
		// TODO Auto-generated method stub
		return h;
	}
}
