package control.logic.laszlo;

import java.util.ArrayList;

import control.logic.LaszloLogic;
import model.Package;
import model.Planet;
import model.Tree;

public class FuckMeRight implements Runnable{
	private Thread t;
	private static final int MAX_NODE_NUM = 60000;
	private static final int MIN_NODE_NUM = 20000;
	private static final int MAX_CAPACITY = 3;
	private Tree mainRoot;
	
	private LaszloLogic buffer;
	// for speed
	private double bestRatio;
	private double actualRatio;
	private Tree bestLeaf;
	private ArrayList<Tree> allTreeNoRecursion;
	private ArrayList<Tree> nodesToCheck;
	private double[][] distances;
	private int planetNum;
	// encahncement
	private int currentNodeNum;

	public void start(LaszloLogic buffer) {
		//System.out.println("Buffer opened...");
		this.t = new Thread(this);
		this.buffer = buffer;
		this.t.start();
	}

	public void stop() {
		this.t = null;
		//System.out.println("Buffer closed...");
	}
	
	
	public static void main(String[] args) {
		ArrayList<Planet> galaxyState = new ArrayList<Planet>();
		Planet planet;
		planet = new Planet("1", 1, 1, new ArrayList<Package>());
		planet.getPackages().add(new Package(1, "1", "3", "", "1", 2));
		planet.getPackages().add(new Package(2, "1", "2", "", "1", 8));
		galaxyState.add(planet);
		planet = new Planet("2", 2, 3, new ArrayList<Package>());
		planet.getPackages().add(new Package(5, "2", "4", "", "2", 3));
		planet.getPackages().add(new Package(6, "2", "3", "", "2", 5));
		galaxyState.add(planet);
		planet = new Planet("3", 3, 2, new ArrayList<Package>());
		planet.getPackages().add(new Package(3, "3", "5", "", "3", 7));
		planet.getPackages().add(new Package(4, "3", "1", "", "3", 1));
		planet.getPackages().add(new Package(11, "1", "4", "", "3", 8));
		galaxyState.add(planet);
		planet = new Planet("4", 3, 5, new ArrayList<Package>());
		planet.getPackages().add(new Package(7, "4", "3", "", "4", 2));
		galaxyState.add(planet);
		planet = new Planet("5", 1, 6, new ArrayList<Package>());
		planet.getPackages().add(new Package(8, "5", "2", "", "5", 4));
		planet.getPackages().add(new Package(9, "5", "2", "", "5", 2));
		galaxyState.add(planet);
		planet = new Planet("6", 3, 7, new ArrayList<Package>());
		planet.getPackages().add(new Package(10, "6", "3", "", "6", 1));
		galaxyState.add(planet);
		FuckMeRight fmr = new FuckMeRight();
		Tree tree = new Tree();
		tree.setTime(0.0);
		tree.setIncome(0);
		tree.setActualGalaxyState(galaxyState);
		tree.setActualPlanet(galaxyState.get(2));
		fmr.setMainRoot(tree);
		fmr.setAllTreeNoRecursion(new ArrayList<Tree>());
		
	}

	public Tree getMainRoot() {
		return mainRoot;
	}

	public void setMainRoot(Tree mainRoot) {
		this.mainRoot = mainRoot;
	}

	public ArrayList<Tree> getAllTreeNoRecursion() {
		return allTreeNoRecursion;
	}

	public void setAllTreeNoRecursion(ArrayList<Tree> allTreeNoRecursion) {
		this.allTreeNoRecursion = allTreeNoRecursion;
	}

	public void run() {
		int rewardSum = 0;
		this.planetNum = this.mainRoot.getActualGalaxyState().size();
		for (int i = 0; i < this.mainRoot.getActualGalaxyState().size(); i++) {
			Planet planet = this.mainRoot.getActualGalaxyState().get(i);
			for (Package pck : planet.getPackages()){
				rewardSum += pck.getFee();
			}
			planet.setIndex(i);
		}
		this.distances = new double[planetNum][planetNum];
		for (int i = 0; i < this.planetNum; i++) {
			for (int k = 0; k < this.planetNum; k++) {
				if (k == i) {
					this.distances[i][k] = 0.0;
				} else if (distances[i][k] == 0.0) {
					this.distances[i][k] = this.mainRoot
							.getActualGalaxyState()
							.get(i)
							.distFrom(
									this.mainRoot.getActualGalaxyState().get(k));
					this.distances[k][i] = this.distances[i][k];
				}
			}
		}
		System.out.println("packageSum: " + rewardSum);
		this.nodesToCheck = new ArrayList<Tree>();
		while (true) {
			this.currentNodeNum = MIN_NODE_NUM + (10000
					* this.buffer.bufferSize());
			if (this.currentNodeNum > MAX_NODE_NUM)
				this.currentNodeNum = MAX_NODE_NUM;
			System.out.println("\ncurrentNodeNum: " + this.currentNodeNum);
			this.allTreeNoRecursion.add(this.mainRoot);
			int index = 0;

			try {
				while (allTreeNoRecursion.size() < this.currentNodeNum) {
					this.allTreeNoRecursion.addAll(this
							.buildTree(this.allTreeNoRecursion.get(index)));
					index++;
				}
			} catch (Exception e) {
				// Nincs t�bb megn�zend� �llapot
			}

			// Csak a root van benne
			if (this.allTreeNoRecursion.size() == 1) {
				break;
			}

			this.evaluate();
			this.findDirectSuccessorOfRoot();

			// System.out.println(bestLeaf);

			// System.out.println("Next step: " + nextStep.printNode());

			// System.out.println("nodes: " + allTreeNoRecursion.size() + "\n")

			if (this.mainRoot.getActualPlanet().getIndex() != this.bestLeaf
					.getActualPlanet().getIndex()) {
				this.buffer.addOrder(this.bestLeaf.getActualPlanet().getName(),
						-1, -1, this.bestLeaf.getIncome(), this.bestLeaf.getTime());
			} else if (this.mainRoot.getPackagesCarried().size() < this.bestLeaf
					.getPackagesCarried().size()) {
				for (Package pck : this.bestLeaf.getPackagesCarried()) {
					if (!this.mainRoot.getPackagesCarried().contains(pck)) {
						this.buffer.addOrder(null, pck.getPackageId(), -1, this.bestLeaf.getIncome(), this.bestLeaf.getTime());
					}
				}
			} else {
				for (Package pck : this.mainRoot.getPackagesCarried()) {
					if (!this.bestLeaf.getPackagesCarried().contains(pck)) {
						this.buffer.addOrder(null, -1, pck.getPackageId(), this.bestLeaf.getIncome(), this.bestLeaf.getTime());
					}
				}
			}
			

			this.mainRoot = this.bestLeaf;
			this.mainRoot.setParent(null);
			this.mainRoot.getChildren().clear();
			this.mainRoot.setTime(0.0);
			this.mainRoot.setIncome(0);

			this.allTreeNoRecursion.clear();
			this.nodesToCheck.clear();
		}

	}

	private boolean isNodeAlreadyExists(Tree toAdd) {
		for (Tree tree : this.nodesToCheck) {
			if (this.compareNodes(toAdd, tree)) {
				return true;
			}
		}
		return false;
	}

	private boolean compareNodes(Tree treeA, Tree treeB) {
		if ((Double.compare(treeA.getTime(), treeB.getTime()) != 0)
				|| treeA.getIncome() != treeB.getIncome()
				|| treeA.getPackagesCarried().size() != treeB
						.getPackagesCarried().size())
			return false;
		for (Package pck : treeA.getPackagesCarried()) {
			if (!treeB.getPackagesCarried().contains(pck))
				return false;
		}
		return true;
	}

	private boolean nothingToDoHere(Tree tree, Planet planet) {
		if (tree.getPackagesCarried().size() == 0
				&& planet.getPackages().size() == 0) {
			return true;
		} else if (tree.getPackagesCarried().size() == MAX_CAPACITY) {
			for (Package pck : tree.getPackagesCarried()) {
				if (pck.getOriginalPlanet().equals(planet.getName())
						|| pck.getTargetPlanet().equals(planet.getName())) {
					return false;
				}
			}
			return true;
		}
		return false;

	}

	private ArrayList<Tree> buildTree(Tree root) {

		// 1. LEAD�S HELYBEN
		for (Package pck : root.getPackagesCarried()) {
			if (pck.getTargetPlanet().equals(root.getActualPlanet().getName())) {
				Tree child = new Tree();
				child.setIncome(root.getIncome() + pck.getFee());
				child.setTime(root.getTime() + 0.5);

				child.getPackagesCarried().addAll(root.getPackagesCarried());
				child.getPackagesCarried().remove(pck);

				child.setDoneSomething(true);
				child.setActualGalaxyState(root.getActualGalaxyState());
				child.setActualPlanet(root.getActualPlanet());
				child.setParent(root);
				root.getChildren().add(child);
			}
		}

		// 2. FELV�TEL
		if (root.getPackagesCarried().size() < MAX_CAPACITY) {
			for (Package pck : root.getActualPlanet().getPackages()) {
				Tree child = new Tree();
				child.setTime(root.getTime() + 0.5);
				child.setIncome(root.getIncome());

				child.getPackagesCarried().addAll(root.getPackagesCarried());

				if (!this.isNodeAlreadyExists(child)) {
					// late initialize for speed
					child.getPackagesCarried().add(pck);
					child.setDoneSomething(true);
					Planet modifiedPlanet = root.getActualPlanet().getCopy();
					modifiedPlanet.getPackages().remove(pck);
					child.setActualPlanet(modifiedPlanet);
					child.setParent(root);
					child.setActualGalaxyState(new ArrayList<Planet>());
					child.getActualGalaxyState().addAll(
							root.getActualGalaxyState());
					child.getActualGalaxyState().remove(root.getActualPlanet());
					child.getActualGalaxyState().add(modifiedPlanet);
					root.getChildren().add(child);
					this.nodesToCheck.add(child);
				}
			}
		}

		// 3. �TUGR�S, HA AZ EL�Z� L�P�S NEM �TUGR�S VOLT
		if (root.isDoneSomething()) {
			for (Planet toGo : root.getActualGalaxyState()) {
				if (toGo.getIndex() != root.getActualPlanet().getIndex()
						&& !this.nothingToDoHere(root, toGo)) {
					Tree child = new Tree();
					child.setActualGalaxyState(root.getActualGalaxyState());
					child.setActualPlanet(toGo);
					child.setIncome(root.getIncome());
					child.setParent(root);
					child.setTime(root.getTime()
							+ this.distances[toGo.getIndex()][root
									.getActualPlanet().getIndex()]
							/ this.preconfiguredSpeed(root.getPackagesCarried()
									.size()));
					child.getPackagesCarried()
							.addAll(root.getPackagesCarried());
					root.getChildren().add(child);
				}
			}
		}
		return root.getChildren();
	}

	private void evaluate() {
		this.bestRatio = -1.0;
		for (Tree tree : this.allTreeNoRecursion) {
			if (tree.getChildren().size() == 0) { // lev�l
				this.actualRatio = tree.getIncome() / tree.getTime();
				if (this.actualRatio > this.bestRatio) {
					this.bestRatio = this.actualRatio;
					this.bestLeaf = tree;
				}
			}
		}
	}

	private void findDirectSuccessorOfRoot() {
		while (this.bestLeaf.getParent() != this.mainRoot) {
			this.bestLeaf = this.bestLeaf.getParent();
		}
	}

	// for speed
	private int preconfiguredSpeed(int packageNum) {
		switch (packageNum) {
		case 3:
			return 4;
		case 2:
			return 6;
		case 1:
			return 8;
		default:
			return 10;
		}
	}

}
