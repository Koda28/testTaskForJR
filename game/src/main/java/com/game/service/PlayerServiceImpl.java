package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.dto.PlayerDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayersRepository playersRepository;



    @Override
    public Player getPlayerById(Long playerId) {
        return playersRepository.getPlayerById(playerId);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playersRepository.findAll();
    }

    @Override
    public Player createPlayer(PlayerDto player) {
        if(player.isAllFieldsNotNull() && player.isBirthdayValid() && player.isExperienceValid()
                && player.isNameAndTitleValid()) {
            if(player.getBanned() == null) {
                player.setBanned(false);
            }
            Player newPlayer = new Player();
            newPlayer.setBanned(player.getBanned());
            newPlayer.setBirthday(player.getBirthday());
            newPlayer.setExperience(player.getExperience());
            newPlayer.setName(player.getName());
            newPlayer.setTitle(player.getTitle());
            newPlayer.setRace(player.getRace());
            newPlayer.setProfession(player.getProfession());
            double newLevel;
            newLevel = (Math.sqrt(2500 + (200 * newPlayer.getExperience())) - 50) / 100;
            newPlayer.setLevel((int) newLevel);
            double toNextLevel = 50 * (newPlayer.getLevel() + 1) * (newPlayer.getLevel() + 2) - newPlayer.getExperience();
            newPlayer.setUntilNextLevel((int) toNextLevel);
            playersRepository.save(newPlayer);
            return newPlayer;
        }
        else {
            return null;
        }
    }

    @Override
    public Player updatePlayer(Long playerId, PlayerDto player) {
        Player playerForUpdate = playersRepository.getPlayerById(playerId);
        if (playerForUpdate != null) {
            if (player.getBanned() != null) {
                playerForUpdate.setBanned(player.getBanned());
            }
            if (player.getBirthday() != null) {
                if (player.isBirthdayValid()) {
                    playerForUpdate.setBirthday(player.getBirthday());
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
            if (player.getName() != null) {
                playerForUpdate.setName(player.getName());
            }
            if (player.getTitle() != null) {
                playerForUpdate.setTitle(player.getTitle());
            }
            if (player.getProfession() != null) {
                playerForUpdate.setProfession(player.getProfession());
            }
            if (player.getRace() != null) {
                playerForUpdate.setRace(player.getRace());
            }
            if (player.getExperience() != null) {
                if (player.isExperienceValid()) {
                    playerForUpdate.setExperience(player.getExperience());
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
            double newLevel;
            newLevel = (Math.sqrt(2500 + (200 * playerForUpdate.getExperience())) - 50) / 100;
            playerForUpdate.setLevel((int) newLevel);
            double toNextLevel = 50 * (playerForUpdate.getLevel() + 1) * (playerForUpdate.getLevel() + 2) - playerForUpdate.getExperience();
            playerForUpdate.setUntilNextLevel((int) toNextLevel);
            playersRepository.save(playerForUpdate);
            return playerForUpdate;
        }
        else {
            return null;
        }
    }

    @Override
    public Player deletePlayerById(Long playerId) {
        Player playerForDelete = playersRepository.getPlayerById(playerId);
        if (playerForDelete != null) {
            playersRepository.delete(playerForDelete);
            return playerForDelete;
        }
        else {
            return null;
        }
    }

    @Override
    public int getCountWithFilters(String playerName, String playerTitle, String playerRace, String playerProfession,
                                   String afterTime, String beforeTime, String isBanned,
                                   String playerMinExperience, String playerMaxExperience,
                                   String playerMinLevel, String playerMaxLevel) {
        List<Player> players = playersRepository.findAll();
        int countRightPlayers = 0;
        for(Player player : players) {
            if (player != null) {
                if (isPlayerMatchToFilter(player, playerName, playerTitle, playerRace, playerProfession, afterTime,
                        beforeTime, isBanned, playerMinExperience, playerMaxExperience,
                        playerMinLevel, playerMaxLevel)) {
                    countRightPlayers++;
                }
            }
        }
        return countRightPlayers;
    }

    @Override
    public List<Player> getPlayersWithFilters(String name, String playerTitle, String playerRace,
                                              String playerProfession, String afterTime, String beforeTime,
                                              String isBanned, String playerMinExperience, String playerMaxExperience,
                                              String playerMinLevel, String playerMaxLevel, String order,
                                              String pageNumber, String pageSize) {

        int sizeInEveryPage = Integer.parseInt(pageSize);
        int numberNeededPage = Integer.parseInt(pageNumber);
        List<Player> filteredPlayers = new ArrayList<>();
        List<Player> allPlayers = playersRepository.findAll();
        for(Player player : allPlayers) {
            if (player != null) {
                if (isPlayerMatchToFilter(player, name, playerTitle, playerRace, playerProfession, afterTime,
                        beforeTime, isBanned, playerMinExperience, playerMaxExperience,
                        playerMinLevel, playerMaxLevel)) {
                    filteredPlayers.add(player);
                }
            }
        }
        List<Player> finalPlayers = new ArrayList<>();
        for (int i = numberNeededPage * sizeInEveryPage; i < (numberNeededPage + 1) * sizeInEveryPage
                && i < filteredPlayers.size(); i++) {
            finalPlayers.add(filteredPlayers.get(i));
        }
        Collections.sort(finalPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                PlayerOrder playerOrder = PlayerOrder.valueOf(order);
                if (playerOrder == PlayerOrder.ID) {
                    return (int) (o1.getId() - o2.getId());
                }
                else if (playerOrder == PlayerOrder.BIRTHDAY) {
                    return o1.getBirthday().compareTo(o2.getBirthday());
                }
                else if (playerOrder == PlayerOrder.EXPERIENCE) {
                    return (o1.getExperience() - o2.getExperience());
                }
                else if (playerOrder == PlayerOrder.LEVEL) {
                    return (o1.getLevel() - o2.getLevel());
                }
                else if (playerOrder == PlayerOrder.NAME) {
                    return (o1.getName().compareTo(o2.getName()));
                }
                return 0;
            }
        });
        return finalPlayers;
    }

    private boolean isPlayerMatchToFilter(Player player, String name, String playerTitle, String playerRace, String playerProfession,
                                          String afterTime, String beforeTime, String isBanned,
                                          String playerMinExperience, String playerMaxExperience,
                                          String playerMinLevel, String playerMaxLevel) {
        if (name != null) {
            if (!player.getName().contains(name)) {
                return false;
            }
        }
        if (playerTitle != null) {
            if (!player.getTitle().contains(playerTitle)) {
                return false;
            }
        }
        if (playerRace != null) {
            if (!player.getRace().equals(Race.valueOf(playerRace))) {
                return false;
            }
        }

        if(playerProfession != null) {
            if (!player.getProfession().equals(Profession.valueOf(playerProfession))) {
                return false;
            }
        }
        if(isBanned != null) {
            if (!player.getBanned() == Boolean.parseBoolean(isBanned)) {
                return false;
            }
        }
        if (afterTime != null) {
            if (!player.getBirthday().after(new Date(Long.parseLong(afterTime)))) {
                return false;
            }
        }

        if (beforeTime != null) {
            if (!player.getBirthday().before(new Date(Long.parseLong(beforeTime)))) {
                return false;
            }
        }

        if (playerMinExperience != null) {
            if (!(player.getExperience() >= Integer.parseInt(playerMinExperience))) {
                return false;
            }
        }

        if (playerMaxExperience != null) {
            if (!(player.getExperience() <= Integer.parseInt(playerMaxExperience))) {
                return false;
            }
        }

        if (playerMinLevel != null) {
            if (!(player.getLevel() >= Integer.parseInt(playerMinLevel))) {
                return false;
            }
        }

        if (playerMaxLevel != null) {
            if (!(player.getLevel() <= Integer.parseInt(playerMaxLevel))) {
                return false;
            }
        }
        return true;
    }

}
