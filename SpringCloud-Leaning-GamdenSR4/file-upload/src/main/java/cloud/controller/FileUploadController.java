package cloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 *  上传附件controller
 */
@Controller
public class FileUploadController {

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
   public String handleFileUpload(@RequestParam(value = "file",required = true)MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        File saveFile = new File(file.getOriginalFilename());
        FileCopyUtils.copy(bytes,saveFile);
        return saveFile.getAbsolutePath();
   }
}
