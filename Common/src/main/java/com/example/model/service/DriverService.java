package com.example.model.service;
import com.example.model.Repository.DriverRepository;
import com.example.model.dto.DriverDto;
import com.example.model.dto.UserDTO;
import com.example.model.model.Driver;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<DriverDto> searchByName(String name) {
        List<Driver> drivers = driverRepository.findByName(name);
        return drivers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public List<DriverDto> searchByNationalId(String nationalId) {
        List<Driver> drivers = driverRepository.findByNationalId(nationalId);
        return drivers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DriverDto convertToDTO(Driver driver) {
        DriverDto driverDTO = new DriverDto();
        driverDTO.setId(driver.getId());
        driverDTO.setYearsOfExperience(driver.getYearsOfExperience());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(driver.getUser().getId());
        userDTO.setFirstName(driver.getUser().getFirstName());
        userDTO.setLastName(driver.getUser().getLastName());
        userDTO.setFamilyName(driver.getUser().getFamilyName());
        userDTO.setEmail(driver.getUser().getEmail());
        userDTO.setPhoneNumber(driver.getUser().getPhoneNumber());
        userDTO.setNationalid(driver.getUser().getNationalid());
        driverDTO.setUser(userDTO);
        return driverDTO;
    }

    public Driver getDriverById(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));
    }

}
