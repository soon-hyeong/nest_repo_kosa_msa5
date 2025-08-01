package org.kosa.nest.handlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.kosa.nest.command.Command;
import org.reflections.Reflections;

public class AdminCommandHandlerMapping {
	private static AdminCommandHandlerMapping instance;
	
	private AdminCommandHandlerMapping() {
		
	}
	
	public static AdminCommandHandlerMapping getInstance() {
		if(instance == null)
			instance = new AdminCommandHandlerMapping();
		return instance;
	}
	
	public Command create(String command) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Command resultCommand = null;
		
		System.out.println(command);
		Reflections reflections = new Reflections(Command.class.getPackageName());
		
		Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
		
		for(Class<? extends Command> clazz : classes ) {
			if(!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
				System.out.println("clazz:" + clazz.getName());
				String[] clazzGetName = clazz.getName().split("\\.");
			    if (clazzGetName[clazzGetName.length - 1].equalsIgnoreCase(command + "Command")) {
					resultCommand = clazz.getDeclaredConstructor().newInstance();
			    }
			}
		}
		return resultCommand;		
	}
}
