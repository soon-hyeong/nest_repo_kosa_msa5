package org.kosa.nest.handlerMapping;

import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.StringTokenizer;

import org.kosa.nest.command.Command;
import org.kosa.nest.command.user.UserCommand;
import org.reflections.Reflections;

public class UserCommandHandlerMapping {
	private static UserCommandHandlerMapping instance;
	
	private UserCommandHandlerMapping() {
		
	}
	
	public static UserCommandHandlerMapping getInstance() {
		if(instance == null)
			instance = new UserCommandHandlerMapping();
		return instance;
	}
	
	public Command create(String commandLine, ObjectOutputStream oos) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Command resultCommand = null;
		
		StringTokenizer st = new StringTokenizer(commandLine);
		String command = st.nextToken();
		Reflections reflections = new Reflections(Command.class.getPackageName());
		
		Set<Class<? extends UserCommand>> classes = reflections.getSubTypesOf(UserCommand.class);
		
		for(Class<? extends UserCommand> clazz : classes ) {
			if(!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
				String[] clazzGetName = clazz.getName().split("\\.");
			    if (clazzGetName[clazzGetName.length - 1].equalsIgnoreCase(command + "Command")) {
					Method getInstance = clazz.getMethod("getInstance", ObjectOutputStream.class);
					resultCommand = (UserCommand)getInstance.invoke(clazz, oos);
			    }
			}
		}
		return resultCommand;
	}
}
