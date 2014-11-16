package control.logic.adam;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import model.Package;
import model.Planet;

public class Reckoner implements Runnable {

	ArrayList<Planet> planets;
	Queue<Node> queue;
	double maxProfit;
	Node bestNode;
	
	volatile boolean shutdown = false;

	public Reckoner(String planetName, ArrayList<Planet> planets) {
		this.planets = planets;
		this.queue = new LinkedList<Node>();
		this.maxProfit = 0;
		this.bestNode = null;
		
		Package p = new Package();
		p.setTargetPlanet(planetName);
		queue.add(new Node(null, p , 0));
	}

	public void run() {
		while (!shutdown) {
			Node n = queue.poll();
			findBestNode(n);
		}
	}

	private void findBestNode(Node n) {
		Planet p = getPlanet(n.getPack().getTargetPlanet());
		for (model.Package pack : p.getPackages()) {
			if (isPackOK(pack) && !shutdown) {
				double profit = setProfit(p.getName(), pack)
						+ n.getProfit();
				Node newNode = new Node(n, pack, profit);
				this.queue.add(newNode);
				if(profit>this.maxProfit && !shutdown){
					this.maxProfit=profit;
					this.bestNode=newNode;
				}
			}
		}
	}

	private Planet getPlanet(String targetPlanet) {
		for (Planet p : planets) {
			if (p.getName().equals(targetPlanet)) {
				return p;
			}
		}
		return null;
	}

	private boolean isPackOK(Package pack) {
		if (pack.getFee() > 25) {
			return true;
		}
		return false;
	}

	private double setProfit(String targetPlanet, Package pack) {
		double distance = getPlanet(targetPlanet).distFrom(
				getPlanet(pack.getTargetPlanet()));
		double fee = pack.getFee();
		return fee / distance;
	}

	public double getMaxProfit() {
		return maxProfit;
	}

	public Node getBestNode() {
		return bestNode;
	}
	
	public boolean isShutdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

}
