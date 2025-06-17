package org.kosa.nest.common;

import java.io.File;

public class ServerConfig {
	String WINDOW_REPOPATH = "C:" + File.separator + "nest";
	String UNIX_LINUX_REPOPATH = File.separator + "Users" + File.separator 
			+ System.getProperty("user.name")+ File.separator +"nest";
}