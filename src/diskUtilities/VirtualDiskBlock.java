package diskUtilities;

public class VirtualDiskBlock {
	/**
	 * @author RNA
	 *
	 */
	private byte [] elements;
	
	
	public VirtualDiskBlock() {
		elements = new byte[256];
	}
	
	/**
	 * @param blockCapacity
	 */
	public VirtualDiskBlock(int blockCapacity){
		elements = new byte[blockCapacity];
	}
	
	/**
	 * @return length
	 */
	public int getCapacity(){
		return elements.length;
		
	}
	
	/**
	 * @param index
	 * @param nuevo
	 */
	public void setElement(int index, byte nuevo) {
		this.elements[index]= nuevo;
	}
	
	/**
	 * @return element at index
	 */
	public byte getElement(int index){
		return elements[index];
		
	}

}
