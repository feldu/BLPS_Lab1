package blps.labs.repository;

import blps.labs.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByCarModelAndModificationAndManufactureYearAndOwnershipDateAndMileage(@NotEmpty String carModel, @NotEmpty String modification, @NotNull Date manufactureYear, @NotNull Date ownershipDate, @NotNull @Min(value = 0) Integer mileage);
}
