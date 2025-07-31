package org.kosa.nest.handlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.kosa.nest.command.Command;
import org.reflections.Reflections;

public class CommandHandlerMapping {
	private static CommandHandlerMapping instance;
	
	private CommandHandlerMapping() {
		
	}
	
	public static CommandHandlerMapping getInstance() {
		if(instance == null)
			instance = new CommandHandlerMapping();
		return instance;
	}
	
	public Command create() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Command command = null;
		
		Reflections reflections = new Reflections(Command.class.getPackageName());
		
		Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
		
		for(Class<? extends Command> clazz : classes ) {
			if(!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
				command = clazz.getDeclaredConstructor().newInstance();
			}
		}
		return command;		
	}
}
