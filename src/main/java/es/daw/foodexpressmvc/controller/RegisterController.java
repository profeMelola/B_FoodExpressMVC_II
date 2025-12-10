package es.daw.foodexpressmvc.controller;

import es.daw.foodexpressmvc.dto.UserRegisterDTO;
import es.daw.foodexpressmvc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "register";
    }

    @PostMapping
    public String register(
            @Valid @ModelAttribute("userRegisterDTO") UserRegisterDTO dto,
            BindingResult bindingResult,
            Model model
    ){
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Registrar usuario
        userService.register(dto);


        // Redirige a login indicando que se ha registrado correctamente
        return "redirect:/login?registered";

    }



}
