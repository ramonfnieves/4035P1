package virtualDisk;

public class VirtualDiskBlock {

	private int blockCapacity;
	private int index;
	private byte nuevo;
	private byte [] disk;
	
	
	public VirtualDiskBlock() {
		this.blockCapacity=256;
	}
	
	public VirtualDiskBlock(int blockCapacity){
		this.blockCapacity=blockCapacity;
	}
	
	public int getCapacity(){
		return blockCapacity;
		
	}
	
	public void setElement(int index, byte nuevo) {
		this.disk[index]= nuevo;
	}
	
	public byte getElement(int index){
		return disk[index];
		
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public byte getNuevo() {
		return nuevo;
	}

	public void setNuevo(byte nuevo) {
		this.nuevo = nuevo;
	}
}
