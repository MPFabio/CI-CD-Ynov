package io.github.pause_clope.repositories;

import io.github.pause_clope.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByNickname(String nickName);
}
