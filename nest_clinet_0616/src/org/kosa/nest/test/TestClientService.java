package org.kosa.nest.test;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.common.ClientConfig;
import org.kosa.nest.model.FileVO;

public class TestClientService {
	public List<FileVO> list() {
        List<FileVO> fileList = new ArrayList<>();

        // 사용자 홈 디렉토리 하위의 nest 폴더를 기준 경로로 설정
        File folder = new File(ClientConfig.REPOPATH);

        // 해당 경로가 존재하고, 디렉토리일 경우에만 처리
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles(); // 디렉토리 내의 파일 리스트

            if (files != null) {
                for (File file : files) {
                    // 일반 파일인 경우에만 처리
                    if (file.isFile()) {
                        // 마지막 수정 시간을 기반으로 생성일시 추정
                        LocalDateTime createdAt = LocalDateTime.ofInstant(																					
                            Instant.ofEpochMilli(file.lastModified()),
                            ZoneId.systemDefault()
                        );

                        // 파일 정보를 담은 FileVO 객체 생성
                        FileVO vo = new FileVO(file.getAbsolutePath(), createdAt, file.getName());
                        fileList.add(vo); // 리스트에 추가
                    }
                }
            }
        }

        return fileList;
    }

}
