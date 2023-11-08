package seaung.uoscafeteriamenu.api.korea.holiday.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private T response;
}
