package model;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import control.api.RestAPI;

public class ModelStore {

	private ArrayList<Planet> planets;
	private SpaceShip spaceShip;

	public void refreshModel(RestAPI galaxy) {
		planets = setPlanets(galaxy.getGalaxy());
		spaceShip = setSpaceShip(galaxy.whereIs());
	}

	private ArrayList<Planet> setPlanets(String galaxy) {
		ArrayList<Planet> planetsList = new ArrayList<Planet>();

		Object obj = JSONValue.parse(galaxy);
		JSONObject all = (JSONObject) obj;
		JSONArray planets = (JSONArray) all.get("planets");

		for (Object pl : planets) {
			Planet planetObj = new Planet();
			planetObj.setName((String) ((JSONObject) pl).get("name"));

			planetObj.setX(Double.parseDouble(((JSONObject) pl).get("x")
					.toString()));
			planetObj.setY(Double.parseDouble(((JSONObject) pl).get("y")
					.toString()));
			ArrayList<Package> packageList = new ArrayList<Package>();
			for (Object pa : (JSONArray) ((JSONObject) pl).get("packages")) {
				Package packageObj = new Package();
				packageObj.setPackageId(Integer.parseInt(((JSONObject) pa).get(
						"packageId").toString()));
				packageObj.setOriginalPlanet((String) ((JSONObject) pa)
						.get("originalPlanet"));
				packageObj.setTargetPlanet((String) ((JSONObject) pa)
						.get("targetPlanet"));
				packageObj.setText((String) ((JSONObject) pa).get("text"));
				packageObj.setActualPlanet((String) ((JSONObject) pa)
						.get("actualPlanet"));
				packageObj.setFee(Integer.parseInt(((JSONObject) pa).get("fee")
						.toString()));
				packageList.add(packageObj);
			}
			planetObj.setPackages(packageList);
			planetsList.add(planetObj);
		}

		return planetsList;
	}

	private SpaceShip setSpaceShip(String string) {
		SpaceShip s = new SpaceShip();
		Object obj = JSONValue.parse(string);
		JSONObject all = (JSONObject) obj;

		s.setUserName((String) (all.get("userName")));
		s.setPlanetName((String) (all.get("planetName")));
		s.setTargetPlanetName((String) (all.get("targetPlanetName")));
		if (all.get("arriveAfterMs") != null) {
			s.setArriveAfterMs(Integer.parseInt(all.get("arriveAfterMs")
					.toString()));
		} else {
			s.setArriveAfterMs(Integer.parseInt("-1"));
		}
		ArrayList<Package> packageList = new ArrayList<Package>();
		JSONArray packages = (JSONArray) all.get("packages");
		for (Object pa : packages) {
			Package packageObj = new Package();
			packageObj.setPackageId(Integer.parseInt(((JSONObject) pa).get(
					"packageId").toString()));
			packageObj.setOriginalPlanet((String) ((JSONObject) pa)
					.get("originalPlanet"));
			packageObj.setTargetPlanet((String) ((JSONObject) pa)
					.get("targetPlanet"));
			packageObj.setText((String) ((JSONObject) pa).get("text"));
			packageObj.setActualPlanet((String) ((JSONObject) pa)
					.get("actualPlanet"));
			packageObj.setFee(Integer.parseInt(((JSONObject) pa).get("fee")
					.toString()));
			packageList.add(packageObj);
		}
		s.setPackages(packageList);
		return s;
	}

	public ArrayList<Planet> getPlanets() {
		return planets;
	}

	public SpaceShip getSpaceShip() {
		return spaceShip;
	}
}
