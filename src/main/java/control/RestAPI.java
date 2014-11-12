package control;
import org.json.simple.JSONArray;


public interface RestAPI {
	public String ping();
	public JSONArray getGalaxy();
	public JSONArray go(String planet);
	public JSONArray wherels();
	public JSONArray pickPackage(int packageId);
	public JSONArray dropPackage(int packageId);
}
