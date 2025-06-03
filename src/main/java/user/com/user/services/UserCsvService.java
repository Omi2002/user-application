package user.com.user.services;

import user.com.user.model.User;
import user.com.user.repository.UserRepository;
import user.com.user.dto.CsvResponse;
import user.com.user.utils.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserCsvService {
    @Autowired
    UserRepository userRepository;

    public String exportUsersToCSV() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            System.out.println(user.getName());
        }
        return CsvHelper.convertToCsv(users);
    }

    public CsvResponse importUsersFromCSV(MultipartFile userFile) {
        List<User> users = CsvHelper.parseCSV(userFile);
        int success = 0;
        int failed = 0;

        for (User user : users) {
            try {
                userRepository.save(user);
                success++;
            } catch (Exception e) {
                failed++;
            }
        }

        return new CsvResponse(success, failed);
    }
}
