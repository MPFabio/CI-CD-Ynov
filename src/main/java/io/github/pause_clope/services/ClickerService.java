package io.github.pause_clope.services;

import io.github.pause_clope.dto.SaveRequest;
import io.github.pause_clope.entities.UserData;
import io.github.pause_clope.repositories.UserDataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClickerService {
    private final UserDataRepository userDataRepository;

    public ClickerService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public void postClicker(String nickname, SaveRequest body) {
        userDataRepository.findByNickname(nickname).ifPresentOrElse(
                userData -> {
                    userData.setClicks(body.getClicks());
                    userDataRepository.save(userData);
                },
                () -> userDataRepository.save(new UserData(nickname, body.getClicks()))
        );
    }

    public Optional<UserData> getByNickname(String nickname) {
        return userDataRepository.findByNickname(nickname);
    }
}
