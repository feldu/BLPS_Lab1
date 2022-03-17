package blps.labs.service;

import blps.labs.entity.Car;
import blps.labs.exception.DataNotFoundException;
import blps.labs.repository.CarRepository;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car findCar(Car car) {
        return carRepository.findByCarModelAndModificationAndManufactureYearAndOwnershipDateAndMileage(car.getCarModel(), car.getModification(), car.getManufactureYear(), car.getOwnershipDate(), car.getMileage()).orElseThrow(() -> new DataNotFoundException("Car not found"));
    }
}
