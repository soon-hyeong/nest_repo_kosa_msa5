package org.kosa.nest.command.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.command.Command;
import org.kosa.nest.common.ScannerWrapper;
import org.kosa.nest.exception.RegisterAdminFailException;
import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;

/**
 * scanner를 통해 회원 가입하는 클래스 <br>
 * 회원 가입시 이메일, 비밀번호를 입력해 회원가입 진행 <br>
 * 예외로 등록이 불가능 하다면 예외 발생한 메세지를 띄움 <br>
 * 
 * @throws RegisterAdminFailException
 */
public class RegisterCommand implements Command {
    
    private RegisterCommand instance;
    
    private RegisterCommand() {
    }
    
    public RegisterCommand getInstance() {
        if(instance == null)
            instance = new RegisterCommand();
        return instance;
    }

    @Override
    public List<Object> handleRequest(String command) throws RegisterAdminFailException {
        
        List<Object> list = null;
        
        try {
            list = new ArrayList<>();
            System.out.print("email:");
            String email = ScannerWrapper.getInstance().nextLine();
            System.out.print("password:");
            String password = ScannerWrapper.getInstance().nextLine();

            AdminVO vo = new AdminVO(email, password);
            AdminDao.getInstance().register(vo); // Id는 시스템이 제공
        } catch (SQLException e) {
            throw new RegisterAdminFailException("Register failed, Email already in use:" + e.getMessage()); // 사용자 정의
                                                                                                                // 예외 전달
        }
        return list;
    }

}
