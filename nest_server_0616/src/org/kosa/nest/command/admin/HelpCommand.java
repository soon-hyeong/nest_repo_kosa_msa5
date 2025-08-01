package org.kosa.nest.command.admin;

import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.command.AdminCommand;
import org.kosa.nest.command.Command;

public class HelpCommand extends AdminCommand {

    @Override
    public List<Object> handleRequest(String command) {
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append("---- nest 프로그램 안내 ----");
    	stringBuilder.append("nest는 로컬과 서버의 파일을 명령어 한 줄로 손쉽게 관리할 수 있는 CLI 기반 파일 관리 프로그램입니다.");
    	stringBuilder.append("로컬 모드에서는 내 컴퓨터의 파일 목록 확인 및 삭제가 가능하며,");
    	stringBuilder.append("서버 모드에서는 파일 검색, 상세 정보 확인, 다운로드를 지원합니다.");
    	stringBuilder.append("모드를 선택한 후 다음 명령어를 사용할 수 있습니다:");
    	stringBuilder.append("  list             - 로컬 파일 목록 보기");
    	stringBuilder.append("  delete <파일명>   - 로컬 파일 삭제");
    	stringBuilder.append("  delete <파일명>   - 로컬 파일 삭제");
    	stringBuilder.append("  search <키워드>   - 서버에서 파일 검색");
    	stringBuilder.append("  info <파일명>     - 서버 파일 상세 정보 보기");
    	stringBuilder.append("  download <파일명> - 서버 파일 다운로드");
    	stringBuilder.append("  exit             - 프로그램 종료");
    	stringBuilder.append("\n");		System.out.println("---- 관리자 명령어 ----");
		stringBuilder.append("  Register              - 관리자로 회원가입합니다");
		stringBuilder.append("  Login                 - 로그인합니다");
		stringBuilder.append("  Logout                - 로그아웃합니다");
		stringBuilder.append("  GetMyInformation      - 로그인한 관리자의 정보를 조회합니다");
		stringBuilder.append("  UpdateMyInformation   - 로그인한 관리자의 정보를 수정합니다");
		stringBuilder.append("  UploadFile            - 파일을 업로드합니다");
		stringBuilder.append("  DeleteFile            - 파일을 삭제합니다");
		stringBuilder.append("  FindAllList           - 모든 파일 목록을 조회합니다");
		stringBuilder.append("  Search                - 파일 이름으로 검색합니다 (파일 이름 필요)");
		stringBuilder.append("  Info                  - 파일 상세 정보를 조회합니다 (파일 이름 필요)");
		stringBuilder.append("  Delete                - 파일을 삭제합니다 (파일 이름 필요)");
		
		List<Object> list = new ArrayList<Object>();
		list.add(stringBuilder.toString());
        return list;
    }

}
