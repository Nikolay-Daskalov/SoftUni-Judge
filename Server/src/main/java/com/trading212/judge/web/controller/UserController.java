package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.UserRegistrationBindingModel;
import com.trading212.judge.model.dto.user.UserRegistrationDTO;
import com.trading212.judge.service.user.UserService;
import com.trading212.judge.web.exception.InvalidRequestException;
import com.trading212.judge.web.exception.ResourceExistException;
import com.trading212.judge.web.exception.UnexpectedFailureException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.trading212.judge.web.controller.UserController.Routes;

@RestController
@RequestMapping(path = Routes.BASE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(path = Routes.REGISTER)
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserRegistrationBindingModel userRegistrationBindingModel,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors() || !userRegistrationBindingModel.password().equals(userRegistrationBindingModel.confirmPassword())) {
            throw new InvalidRequestException();
        }

        boolean exist = userService.isExist(userRegistrationBindingModel.username(), userRegistrationBindingModel.email());

        if (exist) {
            throw new ResourceExistException();
        }

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                userRegistrationBindingModel.username(),
                userRegistrationBindingModel.email(),
                userRegistrationBindingModel.password());

        boolean isRegistered = userService.register(userRegistrationDTO);

        if (!isRegistered) {
            throw new UnexpectedFailureException();
        }
    }

    public static class Routes {
        public static final String BASE = "/api/users";

        public static final String REGISTER = "/register";
    }
}
