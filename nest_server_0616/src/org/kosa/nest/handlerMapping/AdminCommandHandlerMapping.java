package org.kosa.nest.handlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.kosa.nest.command.AdminCommand;
import org.kosa.nest.command.Command;
import org.reflections.Reflections;

public class AdminCommandHandlerMapping {
    private static AdminCommandHandlerMapping instance;

    private AdminCommandHandlerMapping() {

    }

    public static AdminCommandHandlerMapping getInstance() {
        if (instance == null)
            instance = new AdminCommandHandlerMapping();
        return instance;
    }

    public Command create(String command) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        Command resultCommand = null;

        Reflections reflections = new Reflections(Command.class.getPackageName());

        Set<Class<? extends AdminCommand>> classes = reflections.getSubTypesOf(AdminCommand.class);

        for (Class<? extends AdminCommand> clazz : classes) {
            if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                String[] clazzGetName = clazz.getName().split("\\.");
                if (clazzGetName[clazzGetName.length - 1].equalsIgnoreCase(command + "Command")) {
                    Method getInstance = clazz.getMethod("getInstance");
                    resultCommand = (AdminCommand)getInstance.invoke(clazz);
                }
            }
        }
        return resultCommand;
    }
}
