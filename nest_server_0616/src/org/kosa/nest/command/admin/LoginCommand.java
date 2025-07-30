package org.kosa.nest.command.admin;

import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.command.Command;
import org.kosa.nest.common.ScannerWrapper;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;
import org.kosa.nest.service.ServerAdminService;

/**
 * scanner를 통한 로그인 클래스 <br>
 * 로그인 시에 아이디와 비밀번호를 입력하면 로그인, 하지만 비밀번호가 일치하지 않거나 이메일이 틀릴경우 <br>
 * 로그인 실패 메세지를 띄움<br>
 * 로그인 성공시에 ID가 아닌 email을 넣은 이유는 여러 사용자가 테스트 하기에 편리하고, 이메일처럼 <br>
 * 유니크한 정보를 기록에 남겨 Id로 구분이 불가한걸 이메일로 확인이 가능하게 함.<br>
 * 
 * @throws LoginException
 */

public class LoginCommand implements Command {
    
    private LoginCommand instance;
    
    private LoginCommand() {
    }
    
    public LoginCommand getInstance() {
        if(instance == null)
            instance = new LoginCommand();
        return instance;
    }

    @Override
    public List<Object> handleRequest(String command) throws LoginException {

        AdminVO admin = null;
        List<Object> list = null;
        try {
            list = new ArrayList<>();
            System.out.print("email:");
            String email =  ScannerWrapper.getInstance().nextLine();

            System.out.print("password:");
            String password =  ScannerWrapper.getInstance().nextLine();

            admin = AdminDao.getInstance().getAdminInfo(email);

            if (!admin.getPassword().equals(password))
                throw new LoginException("Invalid email or password");
            // 로그인 성공 시 나오는 메세지
            ServerAdminService.getInstance().setCurrentLoginAdmin(admin);
            System.out.println("login success:" + ServerAdminService.getInstance().getCurrentLoginAdmin().getEmail());
        } catch (Exception e) {
            throw new LoginException("login failed: " + e.getMessage());
        }
        return list;
    }
}
