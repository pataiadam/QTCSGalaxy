package control.logic;

import model.ModelStore;
import model.Package;
import model.Planet;
import model.Tree;
import model.SpaceShip;

import java.util.ArrayList;

import control.logic.laszlo.FuckMeRight;


public class LaszloLogic implements Logic, Runnable{
	private boolean initialized = false;
	private Thread t;
	private ArrayList<Order> orderList;
	private int fullIncome = 0;
	private double fullTime = 0.0;
	private FuckMeRight fmr;

	private class Order {
		private String target;
		private int dropId;
		private int pickId;
		private int income = 0;
		private double time = 0.0;

		public Order(String target, int dropId, int pickId, int income,
				double time) {
			this.target = target;
			this.dropId = dropId;
			this.pickId = pickId;
			this.income = income;
			this.time = time;
		}
		
		public int getIncome(){
			return this.income;
		}
		
		public double getTime(){
			return this.time;
		}
		
		public int getDropId(){
			return this.dropId;
		}
		
		public int getPickId(){
			return this.pickId;
		}
		
		public String getTarget(){
			return this.target;
		}
	}

	public void addOrder(String target, int dropId, int pickId, int income,
			double time) {
		this.orderList.add(new Order(target, dropId, pickId, income, time));
	}

	public int bufferSize() {
		return this.orderList.size();
	}

	public void start() {
		System.out.println("Buffer opened...");
		this.t = new Thread(this);
		this.t.start();
		this.orderList = new ArrayList<Order>();
	}

	public void stop() {
		this.t = null;
		System.out.println("Buffer closed...");
	}

	public void run() {
		Thread thisThread = Thread.currentThread();
		this.fmr.start(this);
		/*
		while (this.t == thisThread) {
			Random rand = new Random();

			try {
				Thread.sleep(500);
				Thread.sleep(rand.nextInt(4));

				if (this.orderList.size() > 0) {
					this.fullIncome += this.orderList.get(0).getIncome();
					this.fullTime += this.orderList.get(0).getTime();
					System.out.println("fullIncome: " + this.fullIncome);
					System.out.println("fullTime: " + this.fullTime);
					this.orderList.remove(0);
				}

				
			} catch (Exception e) {
				System.out.println("sorry :(");
			}
		}
		*/
	}

	public int getFullIncome() {
		return fullIncome;
	}

	public void setFullIncome(int fullIncome) {
		this.fullIncome = fullIncome;
	}

	public double addFullTime() {
		return fullTime;
	}

	public void addFullTime(double fullTime) {
		this.fullTime = fullTime;
	}



//////////////////////////////////////////////////////////////////////////

	public void init(ModelStore modelStrore) {
		if (this.t == null){
			this.initialized = true;
			
			this.fmr = new FuckMeRight();
			Tree tree = new Tree();
			tree.setTime(0.0);
			tree.setIncome(0);
			tree.setActualGalaxyState(modelStrore.getPlanets());
			for (Planet planet : modelStrore.getPlanets()){
				if (planet.getName().equals(modelStrore.getSpaceShip().getPlanetName())){
					tree.setActualPlanet(planet);
				}
			}
			//System.out.println(tree.getActualPlanet().getName());
			fmr.setMainRoot(tree);
			fmr.setAllTreeNoRecursion(new ArrayList<Tree>());
			this.start();
		}
		
	}

	/**
	 * Lepakolja a csomago(ka)t a hajóról.
	 * 
	 * @return a leszállítani kívánt csomagok id-jei
	 */
	public int[] drop() {
	if (this.orderList.size() > 0){
		if (this.orderList.get(0).getDropId() != -1){
			int arr[] = new int[1];
			arr[0] = this.orderList.get(0).getDropId();
			this.fullIncome += this.orderList.get(0).getIncome();
					this.fullTime += this.orderList.get(0).getTime();
					System.out.println("fullIncome: " + this.fullIncome);
					System.out.println("fullTime: " + this.fullTime);
			this.orderList.remove(0);
			return  arr;
		}
		return null;
	}
		return null;
	}

	/**
	 * Felveszi a bolygóról a csomagokat
	 * 
	 * @return a felvenni kívánt csomagok id-jei
	 */
	public int[] pick() {
	if (this.orderList.size() > 0){
		if (this.orderList.get(0).getPickId() != -1){
			int arr[] = new int[1];
			arr[0] = this.orderList.get(0).getPickId();
			this.fullIncome += this.orderList.get(0).getIncome();
					this.fullTime += this.orderList.get(0).getTime();
					System.out.println("fullIncome: " + this.fullIncome);
					System.out.println("fullTime: " + this.fullTime);
			this.orderList.remove(0);
			return  arr;
		}
		return null;
	}
		return null;
	}

	/**
	 * Elindul egy adott bolygóra
	 * 
	 * @return a célbolygó neve
	 */
	public String go() {
	if (this.orderList.size() > 0){
		if (this.orderList.get(0).getTarget() != null){
		String target = this.orderList.get(0).getTarget();
			this.fullIncome += this.orderList.get(0).getIncome();
					this.fullTime += this.orderList.get(0).getTime();
					System.out.println("fullIncome: " + this.fullIncome);
					System.out.println("fullTime: " + this.fullTime);
			this.orderList.remove(0);
			return  target;
		}
		return null;
	}
		return null;
	}


}
