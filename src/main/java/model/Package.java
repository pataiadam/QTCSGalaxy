package model;

public class Package {
	private int packageId;
	private String originalPlanet;
	private String targetPlanet;
	private String text;
	private String actualPlanet;
	private int fee;

	public Package() {
	}

	public Package(int packageId, String originalPlanet, String targetPlanet,
			String text, String actualPlanet, int fee) {
		this.setPackageId(packageId);
		this.setOriginalPlanet(originalPlanet);
		this.setTargetPlanet(targetPlanet);
		this.setText(text);
		this.setActualPlanet(actualPlanet);
		this.setFee(fee);
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
}