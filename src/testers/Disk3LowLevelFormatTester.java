package testers;

import diskUtilities.DiskUnit;
import exceptions.NonExistingDiskException;

public class Disk3LowLevelFormatTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DiskUnit d = null;
		
		try {
			d = DiskUnit.mount("disk3");
		} catch (NonExistingDiskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		d.lowLevelFormat();
		d.shutdown(); 
	}


}

