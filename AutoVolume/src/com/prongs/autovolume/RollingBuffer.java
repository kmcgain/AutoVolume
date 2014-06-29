package com.prongs.autovolume;

import java.nio.Buffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;

public class RollingBuffer {
	short[] buffer;
	int bufferSize;
	int chunkSize;
	boolean isEmpty = true;
	
	int head;
	int tail;
	
	public RollingBuffer(int bufferSize, int chunkSize) {
		if (chunkSize > bufferSize) {
			throw new InvalidParameterException();
		}
		this.bufferSize = bufferSize;
		this.chunkSize = chunkSize;
		this.buffer = new short[bufferSize];
		head = 0;
		tail = 0;
	}
	
	public int AddData(short[] inputData, int dataToAdd) throws InvalidParameterException {
		if (dataToAdd > buffer.length || dataToAdd > bufferSize) {
			throw new InvalidParameterException();
		}
		int indexToTake = dataToAdd - (dataToAdd % chunkSize);
		
		for (int i = 0; i < indexToTake; i++) {
			if (!isEmpty && tail == head) {
				head += chunkSize;
				while (head >= bufferSize) {
					head = head - bufferSize;
				}
			}			
			buffer[tail++] = inputData[i];
			if (tail == bufferSize) {
				tail = 0;
			}			
		}
		
		isEmpty = !(indexToTake > 0);		
		return indexToTake;
	}
	
	public int GetData(short[] outputData, int dataToTake)
	{
		if (isEmpty) {
			return 0;
		}
		
		int indexToTake = dataToTake - (dataToTake % chunkSize);
		
		int cursor = head;
		boolean isFirstCheck = true;
		int taken = 0;
		while ((cursor != tail || isFirstCheck) && taken < indexToTake)
		{
			isFirstCheck = false;
			outputData[taken++] = buffer[cursor++];
			if (cursor == bufferSize) {
				cursor = 0;
			}
		}
		
		return taken;
	}
	
}
