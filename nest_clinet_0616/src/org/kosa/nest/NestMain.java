package org.kosa.nest;

import java.io.IOException;

public class NestMain {
    public static void main(String[] args) {
        CommandLineInterface CLI = new CommandLineInterface();
        try {
            CLI.executeProgram();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*
 * list (서버에서 받아서)다운로드 되어있는거
 * search (제목, 태그, 생성일)서버에서 키워드로 검색
 * info (제목으로)서버에 저장된 개별파일 정보 검색
 */