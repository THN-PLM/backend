package server.thn.Common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import server.thn.Member.exception.AccessDeniedException;
import server.thn.Member.exception.AuthenticationEntryPointException;

@RestController
public class ExceptionController {

    @GetMapping("/exception/entry-point")
    public void entryPoint() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }

}
