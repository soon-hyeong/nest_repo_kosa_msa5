package org.kosa.nest.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.kosa.nest.model.FileVO;

/**
 * CommandLineInterface 클래스
 * - 사용자 입력을 받아 명령어 기반으로 기능을 수행
 * - 파일 리스트를 불러오는 명령어는 "파일 검색"으로 설정
 */
public class TestCommandLineInterface {

    private TestClientService clientService;

    public TestCommandLineInterface() {
        this.clientService = new TestClientService();
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("명령어를 입력하세요 (예: 파일 검색):");

            while (true) {
                System.out.print("> ");
                String command = reader.readLine();

                if ("파일 검색".equals(command)) {
                    List<FileVO> files = clientService.list();

                    if (files.isEmpty()) {
                        System.out.println("저장소에 파일이 존재하지 않습니다.");
                    } else {
                        System.out.println("저장소 파일 목록:");
                        for (FileVO file : files) {
                            System.out.println(file);
                        }
                    }

                } else if ("종료".equals(command)) {
                    System.out.println("프로그램을 종료합니다.");
                    break;

                } else {
                    System.out.println("알 수 없는 명령어입니다. 다시 입력해주세요.");
                }
            }

        } catch (IOException e) {
            System.out.println("입력 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new TestCommandLineInterface().run();
    }
}

