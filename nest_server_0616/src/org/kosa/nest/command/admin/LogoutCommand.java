package org.kosa.nest.command.admin;

import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.command.Command;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.service.ServerAdminService;

/**
 * 로그아웃 하는 클래스 <br>
 * 로그아웃을 하게 된다면 어떤 관리자가 로그아웃을 했는지 이메일로 나오게 함 <br>
 * 관리자가 존재하지 않을 때 로그아웃을 한다고 하면 관리자가 없다는 메세지를 띄움 <br>
 * 
 * @return
 * @throws AdminNotLoginException
 */
public class LogoutCommand implements Command {
    
    private LogoutCommand instance;
    
    private LogoutCommand() {
    }
    
    public LogoutCommand getInstance() {
        if(instance == null)
            instance = new LogoutCommand();
        return instance;
    }

    @Override
    public List<Object> handleRequest(String command) throws AdminNotLoginException {
        List<Object> list = new ArrayList<>();
        if(ServerAdminService.getInstance().getCurrentLoginAdmin() == null)
            throw new AdminNotLoginException("Administrator not logined!");
        else {
            String email = ServerAdminService.getInstance().getCurrentLoginAdmin().getEmail();
            ServerAdminService.getInstance().setCurrentLoginAdmin(null);
            System.out.println("logout success:" + email);
        }
        
        return list;
    }

}
