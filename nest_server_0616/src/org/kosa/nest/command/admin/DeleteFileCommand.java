package org.kosa.nest.command.admin;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.kosa.nest.command.Command;
import org.kosa.nest.common.NestConfig;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileNotDeletedInDatabase;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.service.ServerAdminService;

public class DeleteFileCommand implements Command {

    private DeleteFileCommand instance;
    
    private DeleteFileCommand() {
    }
    
    public DeleteFileCommand getInstance() {
        if(instance == null)
            instance = new DeleteFileCommand();
        return instance;
    }

    @Override
    public List<Object> handleRequest(String fileName) throws FileNotDeletedInDatabase, AdminNotLoginException, FileNotFoundException {
        if(ServerAdminService.getInstance().getCurrentLoginAdmin() == null)
            throw new AdminNotLoginException("Permission denied!");
        else {
            File file = new File(NestConfig.REPOPATH + File.separator + fileName);
            
            if(!file.exists())
                throw new FileNotFoundException("File doesn't exist in repository!");
                
            file.delete();
            try {
                FileDao.getInstance().deleteFileInfo(fileName);
            } catch(SQLException e) {
                throw new FileNotDeletedInDatabase("File information database error occured:" + e.getMessage());
            }
            System.out.println("File delete success");
        }
        return null;
    }

}
