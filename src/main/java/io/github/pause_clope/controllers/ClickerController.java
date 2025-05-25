package io.github.pause_clope.controllers;

import io.github.pause_clope.dto.SaveRequest;
import io.github.pause_clope.entities.UserData;
import io.github.pause_clope.services.ClickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/clicker")
public class ClickerController {

    private final ClickerService clickerService;
    private final Logger logger = LoggerFactory.getLogger(ClickerController.class);

    public ClickerController(ClickerService clickerService) {
        this.clickerService = clickerService;
    }

    @PostMapping("/{nickname}")
    public ResponseEntity<String> postClicker(@PathVariable String nickname, @RequestBody SaveRequest body) {
        clickerService.postClicker(nickname, body);
        return ResponseEntity.ok().body("Saved");
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<String> getClicker(@PathVariable String nickname) {
        Optional<UserData> data = clickerService.getByNickname(nickname);
        if (data.isPresent()) {
            logger.info("UserData found: {}", data.get());
            return ResponseEntity.ok().body(data.get().getClicks().toString());
        } else {
            logger.warn("UserData not found for nickname: {}", nickname);
            return ResponseEntity.notFound().build();
        }
    }
}
