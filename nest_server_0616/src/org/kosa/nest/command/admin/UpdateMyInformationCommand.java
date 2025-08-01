package org.kosa.nest.command.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.command.AdminCommand;
import org.kosa.nest.common.ScannerWrapper;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.PasswordNotCorrectException;
import org.kosa.nest.exception.UpdateAdminInfoFailException;
import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;
import org.kosa.nest.service.ServerAdminService;

/**
 * 현재 로그인 된 관리자의 정보 수정 클래스 <br>
 * 현재 로그인이 되었는지 아닌지 확인하고 <br>
 * 현재 로그인 된 관리자에 대한 정보를 보여주고 <br>
 * email과 password를 둘 다 수정 가능하게 구현하고 <br>
 * 만약 둘 다 입력을 안했다면 원래 가지고 있던 관리자 정보로 두게 함 <br>
 * 
 * @return currentLoginAdmin
 * @throws AdminNotLoginException 
 * @throws PasswordNotCorrectException 
 * @throws UpdateAdminInfoFailException 
 */
public class UpdateMyInformationCommand extends AdminCommand {
    
    private static UpdateMyInformationCommand instance;
    
    private UpdateMyInformationCommand() {
    }
    
    public static UpdateMyInformationCommand getInstance() {
        if(instance == null)
            instance = new UpdateMyInformationCommand();
        return instance;
    }

    @Override
    public List<Object> handleRequest(String command) throws UpdateAdminInfoFailException, PasswordNotCorrectException, AdminNotLoginException {
        if(ServerAdminService.getInstance().getCurrentLoginAdmin() == null)
            throw new AdminNotLoginException("Permission denied!");
        else {
            
            System.out.print("enter password:");
            String password = ScannerWrapper.getInstance().nextLine();
            
            if(!ServerAdminService.getInstance().getCurrentLoginAdmin().getPassword().equals(password)) {
                throw new PasswordNotCorrectException("password not correct");
            }
            System.out.print("new email:");
            String newEmail = ScannerWrapper.getInstance().nextLine();
            
            System.out.print("new password:");
            String newPassword = ScannerWrapper.getInstance().nextLine();

            List<Object> list = new ArrayList<>();
            AdminVO updatedAdmin = new AdminVO(
                    ServerAdminService.getInstance().getCurrentLoginAdmin().getId(),
                    newEmail,
                    newPassword
                    );
            // DAO에 updateAdminInfo 존재
            try {
                AdminDao.getInstance().updateAdminInfo(updatedAdmin);
                list.add(updatedAdmin);
            } catch(SQLException e){
                throw new UpdateAdminInfoFailException("Update Admin Information failed:" + e.getMessage());
            }
            ServerAdminService.getInstance().setCurrentLoginAdmin(updatedAdmin);
            System.out.println("Update Admin Information success");
            return (List<Object>)list;
        }
    }

}
