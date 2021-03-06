package diskUtilities;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidParameterException;

import exceptions.ExistingDiskException;
import exceptions.InvalidBlockException;
import exceptions.InvalidBlockNumberException;
import exceptions.NonExistingDiskException;

public class DiskUnit {

	private static final int DEFAULT_CAPACITY = 1024;  // default number of blocks 	
	private static final int DEFAULT_BLOCK_SIZE = 256; // default number of bytes per block

	private int capacity;     	// number of blocks of current disk instance
	private int blockSize; 	// size of each block of current disk instance

	// the file representing the simulated  disk, where all the disk blocks
	// are stored
	private RandomAccessFile disk;

	// the constructor -- PRIVATE
	/**
    @param name is the name of the disk
	 **/
	private DiskUnit(String name) {
		try {
			disk = new RandomAccessFile(name, "rw");
		}
		catch (IOException e) {
			System.err.println ("Unable to start the disk");
			System.exit(1);
		}
	}


	/** Simulates shutting-off the disk. Just closes the corresponding RAF. **/
	public void shutdown() {
		try {
			disk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the content of the virtual block into the disk block corresponding to block number.
	 * @param blockNum
	 * @param b
	 * @throws InvalidBlockNumberException whenever the block number received is not valid for the current disk instance.
	 * @throws InvalidBlockException whenever virtual disk does not represent a valid disk block for the current disk instance.
	 */
	public void write(int blockNum, VirtualDiskBlock b) throws InvalidBlockNumberException, InvalidBlockException {
		if(blockNum < 0 || blockNum >= this.getCapacity())
			throw new InvalidBlockNumberException("Write: Block number " + blockNum + " "
					+ "received is not valid for the current disk instance");
		
		if(b == null || b.getCapacity() != blockSize)
			throw new InvalidBlockException("Write: Does not represent a valid disk block "
					+ "for the current disk instance (null or wrong size)");
		
		try {
			disk.seek(8 + blockNum*blockSize);
			for(int i = 0; i < blockSize; i++){
				disk.writeByte(b.getElement(i));
			}
		} catch (IOException e) {
			System.err.println("read: IO Error");
		}
	}

	/**
	 * Reads a given block from the disk. The content of the specified disk block
	 * is copied as the new content of the current instance of block being referenced by of virtual disk.
	 * @param blockNum
	 * @param b
	 * @throws InvalidBlockNumberException whenever the block number received is not valid for the current disk instance.
	 * @throws InvalidBlockException whenever virtual disk does not represent a valid disk block for the current disk instance.
	 */
	public void read(int blockNum, VirtualDiskBlock b) throws InvalidBlockNumberException, InvalidBlockException{
		
		if(blockNum < 0 || blockNum >= capacity)
			throw new InvalidBlockNumberException("Read: Block number " + blockNum + " "
					+ "received is not valid for the current disk instance");
		
		if(b == null || b.getCapacity() != blockSize)
			throw new InvalidBlockException("Read: Does not represent a valid disk block "
					+ "for the current disk instance (null or wrong size)");
		try {
			disk.seek(8 + blockNum*blockSize);
			for(int i = 0; i < blockSize; i++){
				b.setElement(i, disk.readByte());
			}
		} catch (IOException e) {
			System.err.println("read: IO Error");
		}
	}
	
	/**
	 * Returns a nonnegative integer value corresponding to the number of valid 
	 * blocks (unused + used) that the current disk instance has. 
	 * @return
	 */
	public int getCapacity() { return capacity; }

	/**
	 * Returns a nonnegative integer value which corresponds to the size 
	 * (number of character elements) of a block in the current disk instance. 
	 * @return
	 */
	public int getBlockSize(){ return blockSize; }


	/**
	 * Turns on an existing disk unit whose name is given. If successful, it makes
	 * the particular disk unit available for operations suitable for a disk unit.
	 * @param name the name of the disk unit to activate
	 * @return the corresponding DiskUnit object
	 * @throws NonExistingDiskException whenever no
	 *    ¨disk¨ with the specified name is found.
	 */
	
	public static DiskUnit mount(String name)
			throws NonExistingDiskException
	{
		File file=new File(name);
		
		if (!file.exists())
			throw new NonExistingDiskException("No disk has name : " + name);

		DiskUnit dUnit = new DiskUnit(name);

		// get the capacity and the block size of the disk from the file
		// representing the disk
		
		try {
			dUnit.disk.seek(0);
			dUnit.capacity = dUnit.disk.readInt();
			dUnit.blockSize = dUnit.disk.readInt();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dUnit;     	
	}



	/***
	 * Creates a new disk unit with the given name. The disk is formatted
	 * as having default capacity (number of blocks), each of default
	 * size (number of bytes). Those values are: DEFAULT_CAPACITY and
	 * DEFAULT_BLOCK_SIZE. The created disk is left as in off mode.
	 * @param name the name of the file that is to represent the disk.
	 * @throws ExistingDiskException whenever the name attempted is
	 * already in use.
	 */
	
	public static void createDiskUnit(String name)
			throws ExistingDiskException
	{
		createDiskUnit(name, DEFAULT_CAPACITY, DEFAULT_BLOCK_SIZE);
	}

	/**
	 * Creates a new disk unit with the given name. The disk is formatted
	 * as with the specified capacity (number of blocks), each of specified
	 * size (number of bytes).  The created disk is left as in off mode.
	 * @param name the name of the file that is to represent the disk.
	 * @param capacity number of blocks in the new disk
	 * @param blockSize size per block in the new disk
	 * @throws ExistingDiskException whenever the name attempted is
	 * already in use.
	 * @throws InvalidParameterException whenever the values for capacity
	 *  or blockSize are not valid according to the specifications
	 */
	public static void createDiskUnit(String name, int capacity, int blockSize)
			throws ExistingDiskException, InvalidParameterException
	{
		File file=new File(name);
		
		if (file.exists())
			throw new ExistingDiskException("Disk name is already used: " + name);

		RandomAccessFile disk = null;
		
		if (capacity < 0 || blockSize < 0 ||
				!Utils.power2(capacity) || !Utils.power2(blockSize))
			throw new InvalidParameterException("Invalid values: " +
					" capacity = " + capacity + " block size = " +
					blockSize);
		
		// disk parameters are valid... hence create the file to represent the
		// disk unit.
		
		try {
			disk = new RandomAccessFile(name, "rw");
		}
		catch (IOException e) {
			System.err.println ("Unable to start the disk");
			System.exit(1);
		}

		reserveDiskSpace(disk, capacity, blockSize);

		// after creation, just leave it in shutdown mode - just
		// close the corresponding file
		try {
			disk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void reserveDiskSpace(RandomAccessFile disk, int capacity, int blockSize)
	{
		try {
			disk.setLength(blockSize * capacity);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// write disk parameters (number of blocks, bytes per block) in
		// block 0 of disk space
		
		try {
			disk.seek(0);
			disk.writeInt(capacity);  
			disk.writeInt(blockSize);
		} catch (IOException e) {
			e.printStackTrace();
		} 	
	}
	
	public void lowLevelFormat() {
		try {
			disk.seek(8);
			disk.write(new byte[capacity*blockSize]);
		} catch (IOException e){
			System.err.println("Format: IO Error");
		}

	}



}
