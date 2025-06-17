package org.kosa.nest.common;

import java.io.File;

public interface ServerConfig {
    String REPOPATH = System.getProperty("user.home") +  File.separator + "nestServer";
}