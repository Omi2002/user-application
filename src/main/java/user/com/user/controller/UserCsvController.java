package user.com.user.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import user.com.user.dto.CsvResponse;
import user.com.user.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserCsvController {
    @Autowired
    UserCsvService userCsvService;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadCSV() throws IOException {

        String csvData = userCsvService.exportUsersToCSV();
        ByteArrayInputStream stream = new ByteArrayInputStream(csvData.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(stream));
    }

    @PostMapping("/upload")
    public ResponseEntity<CsvResponse> uploadCSV(@RequestParam("userFile") MultipartFile userFile) {
        CsvResponse result = userCsvService.importUsersFromCSV(userFile);
        return ResponseEntity.ok(result);
    }
}