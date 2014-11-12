import java.net.MalformedURLException;
import java.net.URL;

import control.ImpRestAPI;



public class Main {

	public static void main(String[] args){
		try {
			ImpRestAPI galaxy = new ImpRestAPI(new URL(args[0]), args[1], args[2]);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
