package diskUtilities;

public class Utils {
	public static boolean power2(int capacity) {
		return (Math.log(capacity)/Math.log(2))%1 == 0;
	}

}