package Project.Security.dto;

import Project.Security.Entity.Films;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto {
    private Long id;
    private String title;
    private String director;
    private LocalDate release_date;
    private String descrip;
    private List<String> genres;
    private byte[] imageData;
    private byte[] videoData;
    public void setReleaseDate(LocalDate release_date) {
        this.release_date = release_date;
    }
}