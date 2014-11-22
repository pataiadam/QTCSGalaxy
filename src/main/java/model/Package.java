package model;

public class Package {
	private int packageId;
	private String originalPlanet;
	private String targetPlanet;
	private String text;
	private String actualPlanet;
	private int fee;
	// ne kelljen stringeken vegigmenni osszehasonlitasnal
	private int targetPlanetIndex;
	private int originalPlanetIndex;
	private double targetPlanetDistance;

	public Package() {
	}

	public Package(int packageId, String originalPlanet, String targetPlanet,
			String text, String actualPlanet, int fee) {
		this.packageId = packageId;
		this.originalPlanet = originalPlanet;
		this.targetPlanet = targetPlanet;
		this.text = text;
		this.actualPlanet = actualPlanet;
		this.fee = fee;
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public String getOriginalPlanet() {
		return originalPlanet;
	}

	public void setOriginalPlanet(String originalPlanet) {
		this.originalPlanet = originalPlanet;
	}

	public String getTargetPlanet() {
		return targetPlanet;
	}

	public void setTargetPlanet(String targetPlanet) {
		this.targetPlanet = targetPlanet;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getActualPlanet() {
		return actualPlanet;
	}

	public void setActualPlanet(String actualPlanet) {
		this.actualPlanet = actualPlanet;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getTargetPlanetIndex() {
		return targetPlanetIndex;
	}

	public void setTargetPlanetIndex(int targetPlanetIndex) {
		this.targetPlanetIndex = targetPlanetIndex;
	}

	public int getOriginalPlanetIndex() {
		return originalPlanetIndex;
	}

	public void setOriginalPlanetIndex(int originalPlanetIndex) {
		this.originalPlanetIndex = originalPlanetIndex;
	}

	public double getTargetPlanetDistance() {
		return targetPlanetDistance;
	}

	public void setTargetPlanetDistance(double targetPlanetDistance) {
		this.targetPlanetDistance = targetPlanetDistance;
	}
}