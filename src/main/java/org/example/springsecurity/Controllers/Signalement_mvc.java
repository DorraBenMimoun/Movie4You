package org.example.springsecurity.Controllers;

import jakarta.servlet.http.HttpSession;

import org.example.springsecurity.Models.ReviewEntity;
import org.example.springsecurity.Models.SignalementEntity;
import org.example.springsecurity.Models.User;
import org.example.springsecurity.Repositories.SignalementRepository;
import org.example.springsecurity.Repositories.UserRepository;
import org.example.springsecurity.Service.ReviewService;
import org.example.springsecurity.Service.SignalementService;
import org.example.springsecurity.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/signalements")
public class Signalement_mvc {

    @Autowired
    private SignalementService signalementService;

    @Autowired
    private SignalementRepository signalementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/all")
    public String getAllSignalements(Model model) {
        List<SignalementEntity> signalements = signalementService.getAllSignalements();
        model.addAttribute("signalements", signalements);
        return "signalement/list";
    }

    @GetMapping("/create")
    public String showCreateSignalementForm(Model model) {
        model.addAttribute("signalement", new SignalementEntity());
        List<User> users=userDetailsServiceImpl.getAllUsers();
        model.addAttribute("users", users);

        List<ReviewEntity> reviews= reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);

        return "signalement/create";
    }

    @PostMapping("/create")
    public String createSignalement(
            @ModelAttribute SignalementEntity signalement,
            @RequestParam String raison,
            @RequestParam int reviewId,
            HttpSession session,
            Model model
    ) {

        Long userIdSession = (Long) session.getAttribute("userId");

        System.out.println("raison : "+raison);
        System.out.println("reviewId : "+reviewId);
        System.out.println("userIdSession : "+userIdSession);
        try {
            SignalementEntity newSignalement = signalementService.createSignalement(userIdSession, reviewId, raison);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error/404";
        }
    }

    @GetMapping("/{id}")
    public String showSignalementDetails(@PathVariable("id") Integer id, Model model) {
        Optional<SignalementEntity> signalementOptional = signalementRepository.findById(id);
        if (signalementOptional.isPresent()) {
            model.addAttribute("signalement", signalementOptional.get());
            return "signalement/details";
        }
        model.addAttribute("error", "Signalement not found!");
        return "error/404";
    }



    @GetMapping("/edit/{id}")
    public String showEditSignalementForm(@PathVariable("id") Integer id, Model model) {
        Optional<SignalementEntity> signalement = signalementRepository.findById(id);
        if (!signalement.isPresent()) {
            return "error/404";
        }
        model.addAttribute("signalement", signalement.get());
        return "signalement/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSignalement(
            @PathVariable("id") Integer id,
            @ModelAttribute SignalementEntity signalementInput
    ) {
        Optional<SignalementEntity> optionalSignalement = signalementRepository.findById(id);
        if (!optionalSignalement.isPresent()) {
            return "error/404";
        }

        SignalementEntity existingSignalement = optionalSignalement.get();
        if (signalementInput.getStatus() != null) {
            existingSignalement.setStatus(signalementInput.getStatus());
        }

        signalementRepository.save(existingSignalement);
        return "redirect:/signalement/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteSignalement(@PathVariable("id") Integer id, Model model) {
        if (!signalementRepository.existsById(id)) {
            model.addAttribute("error", "Signalement not found!");
            return "error/404";
        }

        signalementService.deleteSignalement(id);
        return "redirect:/signalements/all";
    }
}
