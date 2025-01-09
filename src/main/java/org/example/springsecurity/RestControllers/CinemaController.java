package org.example.springsecurity.RestControllers;

import jakarta.validation.Valid;

import org.example.springsecurity.DTO.CinemaDTO;
import org.example.springsecurity.Models.CinemaEntity;

import org.example.springsecurity.Repositories.CinemaRepository;
import org.example.springsecurity.Service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/cinema")
public class CinemaController {

    private final CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @PostMapping("/")
    public ResponseEntity<CinemaEntity> createCinema(@Valid @RequestBody CinemaDTO cinemaDTO) {
        CinemaEntity cinema = new CinemaEntity();
        cinema.setName(cinemaDTO.getName());
        cinema.setLocation(cinemaDTO.getLocation());
        cinema.setCapacity(cinemaDTO.getCapacity());
        cinema.setContactInfo(cinemaDTO.getContactInfo());
        return ResponseEntity.ok(cinemaService.addCinema(cinema));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CinemaEntity> getCinemaById(@PathVariable int id) {
        Optional<CinemaEntity> cinema = cinemaRepository.findById(id);
        if (!cinema.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cinema.get());
    }
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            // Retourner un message d'erreur explicite en cas de RuntimeException
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + ex.getMessage());
        }
    }


    @GetMapping("/cinema_name/{name}")
    public List<CinemaEntity> getCinema(@PathVariable String name) {
        return cinemaService.getCinemasByName(name);
    }

    @GetMapping("/cinemas_by_location/{location}")
    public List<CinemaEntity> getCinemasByLocation(@PathVariable String location) {
        return cinemaService.getCinemasByLocation(location);
    }


    @GetMapping("/all")
    public List<CinemaEntity> getAllCinemas() {
        return cinemaService.getAllCinemas();
    }

    @DeleteMapping("/{id}")
    public void deleteCinema(@PathVariable int id) {
        cinemaService.deleteCinemaById(id);
    }

    @Autowired
    CinemaRepository cinemaRepository;

    @PutMapping("/{id}")
    public ResponseEntity<String> editCinema(@PathVariable("id") Integer id, @RequestBody CinemaDTO userinput) {
        System.out.println("Received JSON: " + userinput); // Ajoutez ce log pour déboguer.

        Optional<CinemaEntity> cinema_db = cinemaRepository.findById(id);
        if (!cinema_db.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        CinemaEntity cinema = cinema_db.get();
        if (userinput.getName() != null) {
            cinema.setName(userinput.getName());
        }
        if (userinput.getLocation() != null) {
            cinema.setLocation(userinput.getLocation());
        }
        if (userinput.getCapacity() > 0) {
            cinema.setCapacity(userinput.getCapacity());
        }
        if (userinput.getContactInfo() != null) {
            cinema.setContactInfo(userinput.getContactInfo());
        }

        cinemaRepository.save(cinema);
        return ResponseEntity.ok("Cinema updated");
    }
}
