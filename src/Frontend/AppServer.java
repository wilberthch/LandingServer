package Frontend;
import java.sql.Timestamp;
import Backend.*;

public class AppServer {
	
	
	public void SaveLandingData(String pImageDump, String pMatixData)
	{
		LandingDoc doc = new LandingDoc(pImageDump, pMatixData, Timestamp.valueOf("04/12/2015 01:03:22"));
		
		
		
	}

}
