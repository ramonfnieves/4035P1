package diskUtilities;

public class Utils {
	
	
	public static boolean power2(int capacity) {
		
		if(capacity <0){
			
            throw new IllegalArgumentException("number: " + capacity);
        }
		
        return ((capacity & (capacity -1)) == 0);		
	}

}