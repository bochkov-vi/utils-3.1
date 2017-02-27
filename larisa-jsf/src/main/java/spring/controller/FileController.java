package spring.controller;

import larisa.entity.File;
import larisa.repository.FileRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by home on 28.02.17.
 */

@Controller
public class FileController {

    @Autowired
    FileRepository fileRepository;

    public FileController() {

    }

    @RequestMapping("file/{id}")
    @ResponseBody
    public void showFile(HttpServletResponse response, @PathVariable("id") Integer id) throws IOException {
        File file = fileRepository.findOne(id);
        response.setContentType(file.getType());
        IOUtils.write(file.getData(), response.getOutputStream());
    }
}
