package org.kosa.nest.command.admin;

import java.util.List;

import org.kosa.nest.command.Command;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.service.ServerAdminService;

/**
 * AdminVO에 있는 관리자의 정보 확인 클래스 <br>
 * AdminVO에 있는 관리자에 대한 정보를 가져올때 <br>
 * 관리자 정보를 가져옴 <br>
 * 
 * @return 관리자 정보
 * @throws AdminNotLoginException
 */
public class GetMyInformationCommand implements Command {
    
    private GetMyInformationCommand instance;
    
    private GetMyInformationCommand() {
    }
    
    public GetMyInformationCommand getInstance() {
        if(instance == null)
            instance = new GetMyInformationCommand();
        return instance;
    }

    @Override
    public List<Object> handleRequest(String command) throws AdminNotLoginException {
        if(ServerAdminService.getInstance().getCurrentLoginAdmin() == null)
            throw new AdminNotLoginException("Permission denied!");
        else {
            return (List<Object>) ServerAdminService.getInstance().getCurrentLoginAdmin();
        }
    }

}
