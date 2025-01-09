package org.example.springsecurity.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CinemaDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotNull(message = "Capacity cannot be null")
    private Integer capacity;

    private String contactInfo;
}
