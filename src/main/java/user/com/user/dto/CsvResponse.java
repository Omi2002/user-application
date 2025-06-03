package user.com.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CsvResponse {
    private int successCount;
    private int failedCount;
}
