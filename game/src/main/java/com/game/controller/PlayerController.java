package com.game.controller;

import com.game.dto.PlayerDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {
    @Autowired
    PlayerService playerService;


    @ResponseBody
    @GetMapping("/rest/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long playerId) {
        if (playerId > 0) {
            Player player = playerService.getPlayerById(playerId);
            if (player != null) {
                return ResponseEntity.ok(player);
            }
            else {
                return ResponseEntity.status(404).build();
            }
        }
        else return ResponseEntity.status(400).build();

    }
    @ResponseBody
    @PostMapping("/rest/players")
    public ResponseEntity<Player> createPlayer(@RequestBody PlayerDto player) {
        if (player != null) {
            Player newPlayer = playerService.createPlayer(player);
            if (newPlayer != null) {
                return ResponseEntity.ok(newPlayer);
            }
        }
        return ResponseEntity.status(400).build();
    }

    @ResponseBody
    @PostMapping("/rest/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long playerId, @RequestBody PlayerDto player) {
        if (playerId > 0) {
            try {
                Player playerForUpdate = playerService.updatePlayer(playerId, player);
                if (playerForUpdate != null) {
                    return ResponseEntity.ok(playerForUpdate);
                } else {
                    return ResponseEntity.status(404).build();
                }
            }
            catch (IllegalArgumentException exception) {
                return ResponseEntity.status(400).build();
            }
        }
        else return ResponseEntity.status(400).build();
    }

    @ResponseBody
    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable("id") Long playerId) {
        if (playerId > 0) {
            Player player = playerService.deletePlayerById(playerId);
            if (player != null) {
                return ResponseEntity.ok().build();
            }
            else {
                return ResponseEntity.status(404).build();
            }
        }
        else return ResponseEntity.status(400).build();
    }
    @ResponseBody
    @RequestMapping(value = "/rest/players/count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> getCount(@RequestParam(name = "name", required = false) String name,
                        @RequestParam(name = "title", required = false) String playerTitle,
                        @RequestParam(name = "race", required = false) String playerRace,
                        @RequestParam(name = "profession", required = false) String playerProfession,
                        @RequestParam(name = "after", required = false) String afterTime,
                        @RequestParam(name = "before", required = false) String beforeTime,
                        @RequestParam(name = "banned", required = false) String isBanned,
                        @RequestParam(name = "minExperience", required = false) String playerMinExperience,
                        @RequestParam(name = "maxExperience", required = false) String playerMaxExperience,
                        @RequestParam(name = "minLevel", required = false) String playerMinLevel,
                        @RequestParam(name = "maxLevel", required = false) String playerMaxLevel) {

        return ResponseEntity.ok(playerService.getCountWithFilters(name, playerTitle, playerRace, playerProfession,
                afterTime, beforeTime, isBanned, playerMinExperience, playerMaxExperience, playerMinLevel, playerMaxLevel));

    }

    @ResponseBody
    @RequestMapping(value = "/rest/players",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Player>> getPlayers(@RequestParam(name = "name", required = false) String name,
                                            @RequestParam(name = "title", required = false) String playerTitle,
                                            @RequestParam(name = "race", required = false) String playerRace,
                                            @RequestParam(name = "profession", required = false) String playerProfession,
                                            @RequestParam(name = "after", required = false) String afterTime,
                                            @RequestParam(name = "before", required = false) String beforeTime,
                                            @RequestParam(name = "banned", required = false) String isBanned,
                                            @RequestParam(name = "minExperience", required = false) String playerMinExperience,
                                            @RequestParam(name = "maxExperience", required = false) String playerMaxExperience,
                                            @RequestParam(name = "minLevel", required = false) String playerMinLevel,
                                            @RequestParam(name = "maxLevel", required = false) String playerMaxLevel,
                                              @RequestParam(name = "order", defaultValue = "ID") String order,
                                            @RequestParam(name = "pageNumber", defaultValue = "0") String pageNumber,
                                              @RequestParam(name = "pageSize", defaultValue = "3") String pageSize)
    {
        return ResponseEntity.ok(playerService.getPlayersWithFilters(name, playerTitle, playerRace, playerProfession,
                afterTime, beforeTime, isBanned, playerMinExperience, playerMaxExperience, playerMinLevel, playerMaxLevel,
                order, pageNumber, pageSize));

    }


}
