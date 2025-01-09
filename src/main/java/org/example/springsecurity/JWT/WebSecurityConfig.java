package org.example.springsecurity.JWT;


import org.example.springsecurity.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(
		prePostEnabled = true)
//Active la sécurité au niveau des méthodes dans votre application.
//	prePostEnabled = true : Permet l'utilisation des annotations
//	@PreAuthorize et @PostAuthorize pour sécuriser les méthodes en fonction des rôles ou des permissions.
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	//Crée une instance de AuthTokenFilter, un filtre personnalisé qui
	// intercepte les requêtes pour valider les tokens JWT.
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {

		return new AuthTokenFilter();
	}
	//DaoAuthenticationProvider : Permet de lier une implémentation personnalisée
	// de UserDetailsService (ici, userDetailsService) et un encodeur
	// de mot de passe (ici, BCryptPasswordEncoder).
	//Utilité :
	//Chargement des utilisateurs depuis une base de données.
	//Vérification des mots de passe.
	@Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }
	//AuthenticationManager : Gère le processus d'authentification
	// (utilisé pour valider les utilisateurs).
	//Configuration via le composant AuthenticationConfiguration.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
			throws Exception {
		return authConfig.getAuthenticationManager();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests().requestMatchers("/api/auth/**").permitAll()
			.requestMatchers("/**").permitAll()
        .requestMatchers("/admin/**").hasRole("admin")
        .anyRequest().authenticated();
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(),
			UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
