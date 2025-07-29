package org.kosa.nest;

import org.kosa.nest.commandHandler.ThreadHandler;

public class NestServerMain {
	public static void main(String[] args) {
		ThreadHandler threadHandler = new ThreadHandler();
		threadHandler.executeProgram();
	}
}
