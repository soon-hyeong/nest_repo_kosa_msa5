package org.kosa.nest.command.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.command.AdminCommand;
import org.kosa.nest.common.NestConfig;
import org.kosa.nest.common.ScannerWrapper;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.UploadFileFailException;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.service.ServerAdminService;
/**
 * nest Server의 파일 저장소에 파일을 저장하는 메서드 사용자에게 파일의 정보를 입력받고<br>
 * 지정되 저장소에 파일을 업로드한다.
 * 
 * @return
 * @throws IOException
 * @throws AdminNotLoginException
 * @throws UploadFileFailException
 */
public class UploadFileCommand extends AdminCommand {
    
    private static UploadFileCommand instance;
    
    private UploadFileCommand() {
    }
    
    public static UploadFileCommand getInstance() {
        if(instance == null)
            instance = new UploadFileCommand();
        return instance;
    }

    @Override
    public List<Object> handleRequest(String command) throws AdminNotLoginException, UploadFileFailException, IOException {

        if(ServerAdminService.getInstance().getCurrentLoginAdmin() == null)
            throw new AdminNotLoginException("Permission denied!");
        else {
            FileVO inputFileInfo = getFileInformation();

            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            
            File outputFile = null;
            try {               
                File nestServerDir = new File(NestConfig.REPOPATH);
                if(!nestServerDir.isDirectory())
                    nestServerDir.mkdirs();

                File inputFile = new File(inputFileInfo.getFileLocation());
                String outputFileAddress = NestConfig.REPOPATH + File.separator + inputFile.getName();
                outputFile = new File(outputFileAddress);
                // 파일 입출력
                bis = new BufferedInputStream(new FileInputStream(inputFile), 8192);
                bos = new BufferedOutputStream(new FileOutputStream(outputFile), 8192);

                int data = bis.read();
                while (data != -1) {
                    bos.write(data);
                    data = bis.read();
                }
                inputFileInfo.setFileLocation(outputFileAddress);
                FileDao.getInstance().createFileInfo(inputFileInfo);
                System.out.println("File upload success");
            } catch(IOException e) {
                throw new UploadFileFailException("File upload failed:" + e.getMessage());          
            } catch(SQLException e) {
                outputFile.delete();
                throw new UploadFileFailException("File upload failed:" + e.getMessage());          
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        }
        return new ArrayList<Object>();
    }
    
    /**
     * 파일 업로드에 필요한 정보들을 입력받아 fileVO를 생성하고 반환하는 클래스. uploadFile() 메서드에서 사용한다.
     * 
     * @return
     * @throws AdminNotLoginException
     */
    private FileVO getFileInformation() throws AdminNotLoginException {
        System.out.print("file address:");
        String fileAddress = ScannerWrapper.getInstance().nextLine();

        System.out.print("tag:");
        String fileTag = ScannerWrapper.getInstance().nextLine();
        System.out.print("description:");
        String fileDescription = ScannerWrapper.getInstance().nextLine();
        File inputFile = new File(fileAddress);
        LocalDateTime lastModifed = LocalDateTime.ofInstant(Instant.ofEpochMilli(inputFile.lastModified()),
                ZoneId.systemDefault());

        FileVO fileVO = new FileVO(fileAddress, lastModifed, ServerAdminService.getInstance().getCurrentLoginAdmin().getId(), inputFile.getName(), fileTag,
                fileDescription);

        return fileVO;
    }
}
