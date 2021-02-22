package com.sh.maleki.mankalah.web.controller;

import com.sh.maleki.common.GameDto;
import com.sh.maleki.common.Player;
import com.sh.maleki.mankalah.domain.Game;
import com.sh.maleki.mankalah.domain.GameWinner;
import com.sh.maleki.mankalah.repositories.GameRepository;
import com.sh.maleki.mankalah.service.GameService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sh.maleki.mankalah.domain.Player.PLAYER1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerIT {

    @Autowired
    private GameService gameService;

    @Autowired
    TestRestTemplate testRestTemplate;

    private static GameDto gameDto;
    private static Game game;


    private static Integer kalah1 = 6;
    private static Integer kalah2 = 13;
    private static Integer stones = 6;

    @DisplayName("IT: Create Game Test")
    @Test
    void createGameTest() {
        //given

        //when
        GameDto resultGameDto = testRestTemplate.postForObject("/games",null,GameDto.class);

        //then
        System.out.println(resultGameDto);
        assertThat(resultGameDto).isNotNull();

    }

    @DisplayName("IT: Mave Move Test")
    @Test
    void makeMoveTest() {
        //given
        GameDto gameDto  =  gameService.createGame();
        //when
        ResponseEntity<GameDto>  resultResp =  testRestTemplate
        .exchange("/games/{gameId}/pits/{pitId}"
                , HttpMethod.PUT
                ,null
                ,GameDto.class
                ,gameDto.getGameId()
                ,1);

        GameDto resultGameDto = resultResp.getBody();

        //then
       System.out.println(resultGameDto);
       assertThat(resultGameDto).isNotNull();
       assertThat(resultResp.getStatusCode()).isEqualTo(HttpStatus.OK);

    }



}
