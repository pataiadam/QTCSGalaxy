package control.logic.adam;

import model.Package;

public class Node {

	private Node parent;
	private Package pack;
	private double profit;

	public Node(Node parent, Package pack, double profit) {
		this.parent = parent;
		this.pack=pack;
		this.profit = profit;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public Package getPack() {
		return pack;
	}

	public void setPack(Package pack) {
		this.pack = pack;
	}

}
