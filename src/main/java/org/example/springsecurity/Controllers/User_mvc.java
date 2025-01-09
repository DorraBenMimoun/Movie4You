package org.example.springsecurity.Controllers;

import jakarta.servlet.http.HttpSession;
import org.example.springsecurity.JWT.JwtUtils;
import org.example.springsecurity.Models.ERole;
import org.example.springsecurity.Models.RefreshToken;
import org.example.springsecurity.Models.Role;
import org.example.springsecurity.Models.User;
import org.example.springsecurity.Repositories.RoleRepository;
import org.example.springsecurity.Repositories.UserRepository;
import org.example.springsecurity.Service.RefreshTokenService;
import org.example.springsecurity.Service.UserDetailsImpl;
import org.example.springsecurity.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class User_mvc {
    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("userForm", user);
        model.addAttribute("roles",List.of("ROLE_USER", "ROLE_ADMIN","ROLE_MODERATOR") ); // Liste des rôles disponibles
        return "/users/add_user";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("userForm") User user, @RequestParam(value = "roles", required = false) List<String> roleNames, Model model) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            model.addAttribute("error", "Nom d'utilisateur et mot de passe sont obligatoires");
            model.addAttribute("roles", roleRepository.findAll());
            return "/users/add_user";
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Le nom d'utilisateur existe déjà");
            model.addAttribute("roles", roleRepository.findAll());
            return "/users/add_user";
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "L'adresse email existe déjà");
            model.addAttribute("roles", roleRepository.findAll());
            return "/users/add_user";
        }

        user.setPassword(encoder.encode(user.getPassword()));

        // Affecter les rôles
        Set<Role> roles = new HashSet<>();
       /* if (roleNames != null) {
            roleNames.forEach(roleName -> {
                Role role = roleRepository.findByName(ERole.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Erreur : rôle introuvable."));
                roles.add(role);
            });
        }*/
        Role role=roleRepository.findByName(ERole.valueOf("ROLE_USER")).orElseThrow(() -> new RuntimeException("Erreur : rôle introuvable."));

        roles.add(role);

        user.setRoles(roles);
        userService.createUser(user);
        model.addAttribute("success", "Utilisateur enregistré avec succès !");

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    @PostMapping("/login")
    public String authenticateUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session)
    {

        System.out.println("Attempting authentication for username: " + username);

        try {
            // Authentification de l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Récupération des détails de l'utilisateur authentifié
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Optional<User> user = userRepository.findById(userDetails.getId());

            if (user.isEmpty()) {
                model.addAttribute("error", "Utilisateur introuvable.");
                return "users/login";
            }

            // Génération des tokens JWT et refreshToken
            String jwt = jwtUtils.generateJwtToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            // Récupération des rôles de l'utilisateur
            List<String> roles = user.get().getRoles().stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.toList());

            // Enregistrement des données nécessaires dans la session
            session.setAttribute("username", username);
            session.setAttribute("email",user.get().getEmail());
            session.setAttribute("jwt", jwt);
            session.setAttribute("refreshToken", refreshToken);
            session.setAttribute("userId", userDetails.getId());
            session.setAttribute("roles", roles);
            session.setAttribute("user",user);

            System.out.println("Authentication successful for user: " + username + " with roles: " + roles);

            // Redirection en fonction des rôles
            if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_USER")) {
                return "redirect:/";
            }
            else
                return "redirect:/users/login";
        }
        catch (Exception e) {
            //System.err.println("Authentication failed for username: " + username);
            //e.printStackTrace();

            // Ajout d'un message d'erreur pour la vue
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
            return "users/login";
        }
    }


    @GetMapping("/logout")
    public String logoutUser( Model model,HttpSession session) {
        RefreshToken refreshToken = (RefreshToken) session.getAttribute("refreshToken");
        Long userId= (Long) session.getAttribute("userId");
        System.out.println("refreshToken  logout"+refreshToken);
        boolean isRemoved = refreshTokenService.deleteByUserId(userId);

        if (isRemoved) {
            SecurityContextHolder.clearContext(); // Supprimer le contexte de sécurité
            model.addAttribute("message", "Vous avez été déconnecté avec succès.");
            System.out.println("token est supprimer");
            session.invalidate();

            return "redirect:/";
        } else {
            model.addAttribute("error", "Erreur lors de la déconnexion.");
            return "error/404"; // Redirection vers l'interface d'accueil

        }

    }

    @GetMapping("/all")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/users/all_users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users/all";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") long id, Model model) {
       User user = userService.getUserById(id);
        if (user == null) {
            model.addAttribute("error", "Utilisateur introuvable avec l'ID " + id);
            return "redirect:/users/all";
        }
        model.addAttribute("userForm", user);
        model.addAttribute("roles", roleRepository.findAll());
        return "/users/edit_user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("userForm") User user, @RequestParam(value = "roles", required = false) List<String> roleNames) {
        User existingUser = userService.getUserById(id);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());

            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }

            Set<Role> roles = new HashSet<>();
            if (roleNames != null) {
                roleNames.forEach(roleName -> {
                    Role role = roleRepository.findByName(ERole.valueOf(roleName))
                            .orElseThrow(() -> new RuntimeException("Erreur : rôle introuvable."));
                    roles.add(role);
                });
                existingUser.setRoles(roles);

            }

            userService.updateUser(existingUser);
        }
        return "redirect:/users/user";
    }

    @GetMapping("/user")
    public String getUserDetails(Model model,HttpSession session) {
        // Récupérer l'authentification courante
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            //String username = authentication.getName();  // Récupérer le nom de l'utilisateur connecté
            User user= userService.getUserById((Long) session.getAttribute("userId"));
            model.addAttribute("user", user);
            model.addAttribute("username", session.getAttribute("username"));  // Ajouter l'attribut au modèle
            model.addAttribute("refreshToken", session.getAttribute("refreshToken"));  // Ajouter l'attribut au modèle
            model.addAttribute("jwt",session.getAttribute("jwt"));

        } else {
            session.getAttribute("username");
            model.addAttribute("username",session.getAttribute("username") );
        }
        return "/users/userDetails";  // Nom du modèle (fichier HTML)
    }

}
