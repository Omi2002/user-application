package user.com.user.utils;
import user.com.user.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

public class CsvHelper {
    public static String convertToCsv(List<User> users) {
        StringBuilder sb = new StringBuilder("ID,Name,Email\n");
        for (User user : users) {
            sb.append(user.getId()).append(",")
              .append(user.getName()).append(",")
              .append(user.getEmail()).append("\n");
        }
        return sb.toString();
    }

    public static List<User> parseCSV(MultipartFile file) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine(); 
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                User user = new User();
                user.setName(fields[1]);
                user.setEmail(fields[2]);
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
