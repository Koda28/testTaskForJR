package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayersRepository extends JpaRepository<Player, Long> {
    Player getPlayerById(Long id);

}
