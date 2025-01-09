package org.example.springsecurity.RestControllers;


import jakarta.servlet.http.HttpSession;
import org.example.springsecurity.Models.SignalementEntity;
import org.example.springsecurity.Repositories.SignalementRepository;
import org.example.springsecurity.Service.SignalementService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/signalements")
public class SignalementController {

    SignalementRepository signalementRepository;

    private final SignalementService signalementService;

    public SignalementController(SignalementService signalementService) {
        this.signalementService = signalementService;
    }

    @PostMapping("/")
    public ResponseEntity<SignalementEntity> createSignalement(HttpSession session, @RequestParam int reviewId, @RequestParam String raison) {
        Long userId = (Long) session.getAttribute("userId");
        SignalementEntity signalement = signalementService.createSignalement(userId, reviewId, raison);
        return ResponseEntity.ok(signalement);
    }

    @GetMapping("/")
    public ResponseEntity<List<SignalementEntity>> getAllSignalements() {
        List<SignalementEntity> signalements = signalementService.getAllSignalements();
        return ResponseEntity.ok(signalements);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignalement(@PathVariable int id) {
        signalementService.deleteSignalement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public String showEditSignalementForm(@PathVariable("id") Integer id, Model model) {
        Optional<SignalementEntity> signalementOptional = signalementRepository.findById(id);

        if (!signalementOptional.isPresent()) {
            return "error/404"; // Rediriger vers une page d'erreur si le signalement n'existe pas
        }

        model.addAttribute("signalement", signalementOptional.get());
        return "signalements/edit";
    }


    @PutMapping("/{id}")
    public String updateSignalementStatus(
            @PathVariable("id") Integer id,
            @RequestParam("status") Boolean status,
            RedirectAttributes redirectAttributes) {

        Optional<SignalementEntity> signalementOptional = signalementRepository.findById(id);

        if (!signalementOptional.isPresent()) {
            return "error/404";
        }

        SignalementEntity signalement = signalementOptional.get();
        signalement.setStatus(status);
        signalementRepository.save(signalement);

        redirectAttributes.addFlashAttribute("successMessage", "Statut mis à jour avec succès !");
        return "redirect:/signalements/all";
    }
}
