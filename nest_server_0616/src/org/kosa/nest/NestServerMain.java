package org.kosa.nest;

import org.kosa.nest.TheadHandler.ThreadHandler;

public class NestServerMain {
	public static void main(String[] args) {
		ThreadHandler threadHandler = ThreadHandler.getInstance();
		threadHandler.executeProgram();
	}
}
