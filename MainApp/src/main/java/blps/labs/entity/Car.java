package blps.labs.entity;

import blps.labs.dto.ReviewDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity(name = "car")
@NoArgsConstructor
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty
    private String carModel;

    @NotEmpty
    private String modification;

    @NotNull
    @DateTimeFormat
    private Date manufactureYear;

    @NotNull
    @DateTimeFormat
    private Date ownershipDate;

    @NotNull
    @Min(value = 0)
    private Integer mileage;

    public Car(ReviewDTO reviewDTO) {
        this.carModel = reviewDTO.getCarModel();
        this.modification = reviewDTO.getModification();
        this.manufactureYear = reviewDTO.getManufactureYear();
        this.ownershipDate = reviewDTO.getOwnershipDate();
        this.mileage = reviewDTO.getMileage();
    }
}
