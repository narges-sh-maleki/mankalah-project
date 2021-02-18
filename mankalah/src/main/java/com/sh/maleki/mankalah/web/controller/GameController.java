package com.sh.maleki.mankalah.web.controller;

import com.sh.maleki.mankalah.service.GameService;
import lombok.RequiredArgsConstructor;
import com.sh.maleki.common.GameDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("games")

public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<GameDto> createGame (){
        return new ResponseEntity<GameDto>(gameService.createGame(), HttpStatus.CREATED);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDto> getGame (@PathVariable UUID gameId){
        return new ResponseEntity<GameDto>(gameService.getGame(gameId), HttpStatus.OK);
    }

    @PutMapping("/{gameId}/pits/{pitId}")
    public ResponseEntity<GameDto> makeMove (@PathVariable UUID gameId, @PathVariable Integer pitId){
        return new ResponseEntity<GameDto>(gameService.makeMove(gameId,pitId), HttpStatus.OK);
    }





}
