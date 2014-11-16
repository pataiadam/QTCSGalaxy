package control.logic.laszlo;

import java.util.ArrayList;

import control.logic.LaszloLogic;
import model.Package;
import model.Planet;
import model.Tree;

public class FuckMeRight implements Runnable{
	private static final int MAX_NODE_NUM = 30000;
	private static final int MIN_NODE_NUM = 10000;
	private static final int MAX_CAPACITY = 3;
	private Thread t;

	private Tree mainRoot;
	private ArrayList<Tree> allTreeNoRecursion;
	private ArrayList<Tree> nodesToCheck;
	private LaszloLogic buffer;
	private int allowedNodeNum;
	private double[][] distances;
	private int planetNum;

	private double bestRatio;
	private double actualRatio;

	private Tree bestLeaf;
	private int incomeDiff;
	private double timeDiff;

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

		this.allTreeNoRecursion = new ArrayList<Tree>();
		this.nodesToCheck = new ArrayList<Tree>();
		this.incomeDiff = 0;
		this.timeDiff = 0.0;

		this.planetNum = this.mainRoot.getActualGalaxyState().size();

		int rewardSum = 0;
		for (Planet planet : this.mainRoot.getActualGalaxyState()) {
			for (Package pck : planet.getPackages()) {
				rewardSum += pck.getFee();
			}
		}

		// targetPlanetId beallitasa
		for (Planet planet : this.mainRoot.getActualGalaxyState()) {
			String planetName = planet.getName();
			for (Planet planet2 : this.mainRoot.getActualGalaxyState()) {
				for (Package pck : planet2.getPackages()) {
					if (pck.getOriginalPlanet().equals(planetName)) {
						pck.setOriginalPlanetIndex(planet.getIndex());
					}
					if (pck.getTargetPlanet().equals(planetName)) {
						pck.setTargetPlanetIndex(planet.getIndex());
					}
				}
			}
		}

		// tavolsagok tarolasa
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

		System.out.println("rewardSum: " + rewardSum);
		while (true) {
			this.allowedNodeNum = MIN_NODE_NUM 
				+	(5000 * (this.buffer.getBufferSize()-1));
			if (this.allowedNodeNum > MAX_NODE_NUM)
				this.allowedNodeNum = MAX_NODE_NUM;
			System.out.println("\nallowedNodeNum: " + this.allowedNodeNum);
			this.allTreeNoRecursion.add(this.mainRoot);
			int index = 0;

			try {
				while (allTreeNoRecursion.size() < this.allowedNodeNum) {
					for (Tree tree : this.buildTree(this.allTreeNoRecursion
							.get(index))) {
						this.allTreeNoRecursion.add(tree);
					}
					index++;
				}
			} catch (Exception e) {
				// nincs tobb megnezendo allapot
			}

			// csak a root van, gg
			if (this.allTreeNoRecursion.size() == 1) {
				break;
			}

			this.evaluate();
			this.findDirectSuccessorOfRoot();

			// uzenet epitese
			if (this.mainRoot.getActualPlanet().getIndex() != this.bestLeaf
					.getActualPlanet().getIndex()) {
				this.buffer.addOrder(this.bestLeaf.getActualPlanet().getName(),
						-1, -1, this.bestLeaf.getIncome(),
						this.bestLeaf.getTime());
			} else if (this.mainRoot.getPackagesCarried().size() < this.bestLeaf
					.getPackagesCarried().size()) {
				for (Package pck : this.bestLeaf.getPackagesCarried()) {
					if (!this.mainRoot.getPackagesCarried().contains(pck)) {
						this.buffer.addOrder(null, pck.getPackageId(), -1,
								this.bestLeaf.getIncome(),
								this.bestLeaf.getTime());
						break;
					}
				}
			} else {
				for (Package pck : this.mainRoot.getPackagesCarried()) {
					if (!this.bestLeaf.getPackagesCarried().contains(pck)) {
						this.buffer.addOrder(null, -1, pck.getPackageId(),
								this.bestLeaf.getIncome(),
								this.bestLeaf.getTime());
						break;
					}
				}
			}

			// ujrainditas
			this.mainRoot = this.bestLeaf;
			this.mainRoot.setParent(null);
			this.incomeDiff = this.mainRoot.getIncome();
			this.timeDiff = this.mainRoot.getTime();
			this.mainRoot.setTime(0.0);
			this.mainRoot.setIncome(0);
			this.allTreeNoRecursion.clear();
			this.nodesToCheck.clear();
		}
		System.out.println("Done.");

	}
	
		// azonos allapotok keresese
	private boolean isNodeExists(Tree toAdd) {
		for (Tree tree : this.nodesToCheck) {
			if (this.compareNodes(toAdd, tree)) {
				return true;
			}
		}
		return false;
	}

	// segedfuggveny
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

	// a deadend nem jo es nem is vicces
	// csak ugrasnak hivodik meg
	private boolean isDeadEnd(Tree root) {
		// nem tud felvenni
		if (root.getActualPlanet().getPackages().size() == 0
				|| root.getPackagesCarried().size() == MAX_CAPACITY) {
			// nem tud lerakni
			if (!this.canDropHere(root)) {
				return true;
			}
		}

		return false;
	}

	// segedfuggveny az isDeadEnd() -hez
	private boolean canDropHere(Tree root) {
		for (Package pck : root.getPackagesCarried()) {
			if (pck.getOriginalPlanetIndex() == root.getActualPlanet()
					.getIndex()
					|| pck.getTargetPlanetIndex() == root.getActualPlanet()
							.getIndex()) {
				return true;
			}
		}
		return false;
	}

	// fa epitese
	private ArrayList<Tree> buildTree(Tree root) {

		// ujrafelhasznalas - csak par ertek update, a statuszok nem valtoznak
		if (root.getChildren().size() > 0) {
			for (Tree child : root.getChildren()) {
				child.setIncome(child.getIncome() - this.incomeDiff);
				child.setTime(child.getTime() - this.timeDiff);
			}
		}

		// uj levelek letrehozasa
		else {
			// 1. LEADAS HELYBEN
			for (Package pck : root.getPackagesCarried()) {
				if (pck.getTargetPlanetIndex() == root.getActualPlanet()
						.getIndex()) {
					Tree child = new Tree();
					child.setIncome(root.getIncome() + pck.getFee());
					child.setTime(root.getTime() + 0.5);

					for (Package pck2 : root.getPackagesCarried()) {
						if (pck != pck2) {
							child.getPackagesCarried().add(pck2);
						}
					}

					if (!this.isNodeExists(child)) {
						child.setParent(root);
						child.setActualGalaxyState(root.getActualGalaxyState());
						child.setActualPlanet(root.getActualPlanet());

						root.getChildren().add(child);
						this.nodesToCheck.add(child);
					}
				}
				if (pck.getOriginalPlanetIndex() == root.getActualPlanet()
						.getIndex()) {
					Tree child = new Tree();
					child.setIncome(root.getIncome());
					child.setTime(root.getTime() + 0.5);

					for (Package pck2 : root.getPackagesCarried()) {
						if (pck != pck2) {
							child.getPackagesCarried().add(pck2);
						}
					}

					if (!this.isNodeExists(child)) {
						child.setParent(root);
						Planet modifiedPlanet = root.getActualPlanet()
								.getCopy();
						modifiedPlanet.getPackages().add(pck);
						child.setActualGalaxyState(new ArrayList<Planet>());
						for (Planet planet : root.getActualGalaxyState()) {
							if (planet != root.getActualPlanet()) {
								child.getActualGalaxyState().add(planet);
							}
						}
						child.getActualGalaxyState().add(modifiedPlanet);
						child.setActualPlanet(modifiedPlanet);

						root.getChildren().add(child);
						this.nodesToCheck.add(child);
					}
				}
			}

			// 2. FELVETEL
			if (root.getPackagesCarried().size() < MAX_CAPACITY) {
				for (Package pck : root.getActualPlanet().getPackages()) {
					Tree child = new Tree();
					child.setTime(root.getTime() + 0.5);
					child.setIncome(root.getIncome());

					for (Package pck2 : root.getPackagesCarried()) {
						child.getPackagesCarried().add(pck2);
					}
					child.getPackagesCarried().add(pck);

					if (!this.isNodeExists(child)) {
						child.setParent(root);
						Planet modifiedPlanet = root.getActualPlanet()
								.getCopy();
						modifiedPlanet.getPackages().remove(pck);
						child.setActualPlanet(modifiedPlanet);
						child.setActualGalaxyState(new ArrayList<Planet>());
						for (Planet planet : root.getActualGalaxyState()) {
							if (planet != root.getActualPlanet()) {
								child.getActualGalaxyState().add(planet);
							}
						}
						child.getActualGalaxyState().add(modifiedPlanet);

						root.getChildren().add(child);
						this.nodesToCheck.add(child);
					}
				}
			}

			// 3. ATUGRAS, HA AZ ELOZO LEPES NEM ATUGRAS VOLT
			if (!root.isJustJumped()) {
				for (Planet toGo : root.getActualGalaxyState()) {
					if (toGo.getIndex() != root.getActualPlanet().getIndex()) {
						Tree child = new Tree();
						child.setActualPlanet(toGo);

						for (Package pck2 : root.getPackagesCarried()) {
							child.getPackagesCarried().add(pck2);
						}

						if (!this.isDeadEnd(child)) {
							child.setActualGalaxyState(root
									.getActualGalaxyState());
							child.setJustJumped(true);
							child.setIncome(root.getIncome());
							child.setTime(root.getTime()
									+ this.distances[toGo.getIndex()][root
											.getActualPlanet().getIndex()]
									/ this.preconfiguredSpeed(root
											.getPackagesCarried().size()));
							child.setParent(root);

							root.getChildren().add(child);
						}
					}
				}
			}
		}
		return root.getChildren();
	}

	// megkeresi a legjobb levelet
	private void evaluate() {
		this.bestRatio = -1.0;
		for (Tree tree : this.allTreeNoRecursion) {
			// level
			if (tree.getChildren().size() == 0) {
				this.actualRatio = tree.getIncome() / tree.getTime();
				if (this.actualRatio > this.bestRatio) {
					this.bestRatio = this.actualRatio;
					this.bestLeaf = tree;
				}
			}
		}
	}

	// visszakeresi a legjobb levelhez tartozo kovetkezo lepest
	private void findDirectSuccessorOfRoot() {
		while (this.bestLeaf.getParent() != this.mainRoot) {
			this.bestLeaf = this.bestLeaf.getParent();
		}
	}

	// hardcoded sebesseg
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
