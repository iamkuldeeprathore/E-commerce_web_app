package org.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImp implements  FileService{

    @Override
    public String uplaodImage(String path, MultipartFile file) throws IOException {
        // get the file of the original file
        String originalFileName= file.getOriginalFilename();
        //upload to the server with unique name
        String randomId= UUID.randomUUID().toString();
        //eg -> mat.jpg -->123 -->123.jpg
        String fileName= randomId.concat(originalFileName.
                substring(originalFileName.lastIndexOf('.')));

        String filePath= path+ File.separator+fileName;//this just add "/".

        //check if the path exit and create

        File folder= new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));
        return  fileName;
    }
}
