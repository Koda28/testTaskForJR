package com.game.service;

import com.game.dto.PlayerDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.List;

public interface PlayerService {
    Player getPlayerById(Long Id);

    List<Player> getAllPlayers();

    Player createPlayer(PlayerDto player);

    Player updatePlayer(Long playerId, PlayerDto player);

    Player deletePlayerById(Long playerId);

    int getCountWithFilters(String playerName, String playerTitle, String playerRace, String playerProfession, String afterTime, String beforeTime, String isBanned, String playerMinExperience, String playerMaxExperience, String playerMinLevel, String playerMaxLevel);

    List<Player> getPlayersWithFilters(String name, String playerTitle, String playerRace, String playerProfession, String afterTime, String beforeTime, String isBanned, String playerMinExperience, String playerMaxExperience, String playerMinLevel, String playerMaxLevel, String order, String pageNumber, String pageSize);
}
