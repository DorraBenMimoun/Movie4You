package org.example.springsecurity.Service;


import org.example.springsecurity.Models.CinemaEntity;
import org.example.springsecurity.Repositories.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

    public CinemaEntity addCinema(CinemaEntity cinema) {
        return cinemaRepository.save(cinema);
    }

    public List<CinemaEntity> getCinemasByName(String name) {
        return cinemaRepository.findByNameContaining(name);
    }

    public List<CinemaEntity> getCinemasByLocation(String location) {
        return cinemaRepository.findByLocation(location);
    }

    public List<CinemaEntity> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    public void deleteCinemaById(int id) {
        cinemaRepository.deleteById(id);
    }

    public Optional<CinemaEntity> getCinemaById(int id) {
        return cinemaRepository.findById(id);
    }


}
