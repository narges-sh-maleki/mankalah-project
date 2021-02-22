package com.sh.maleki.mankalah.web.controller;

import com.sh.maleki.common.GameDto;
import com.sh.maleki.common.Player;
import com.sh.maleki.mankalah.domain.Game;
import com.sh.maleki.mankalah.domain.GameWinner;
import com.sh.maleki.mankalah.service.GameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sh.maleki.mankalah.domain.Player.PLAYER1;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    Environment env;
    @Autowired
    MockMvc mockMvc;

    private static GameDto gameDto;
    private static Game game;

    private static Integer kalah1 = 6;
    private static Integer kalah2 = 13;
    private static Integer stones = 6;

    @BeforeAll
    static void beforeAll() {
        System.out.println("**************kalah1:" + kalah1);
        System.out.println("**************kalah2:" + kalah2);
        System.out.println("**************stones:" + stones);

        initGameObjects();
    }

    @BeforeEach
    void setUp() {

        System.out.println("**************Active profile:" + Arrays.toString(env.getActiveProfiles()) + "************");
        // gameDto = localMapper.GameToGameDto(game);


    }

    @Test
    void createGame() throws Exception {
        //given
        when(gameService.createGame()).thenReturn(gameDto);
        //when & then
        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON) )
                .andExpect(jsonPath("$.gameId").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getGame() {
    }

    @Test
    void makeMove() throws Exception {
        //given
        when(gameService.makeMove(any(UUID.class),anyInt())).thenReturn(gameDto);

        //when & then
        mockMvc.perform(put("/games/{gameId}/pits/{pitId}", game.getGameId(), 0))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect( jsonPath("$.gameId").value(gameDto.getGameId().toString()));
    }

    private static void initGameObjects() {
        //initializing the pits
        Integer[] pits = new Integer[kalah2 + 1];
        Arrays.fill(pits, stones);
        pits[kalah1] = 0;
        pits[kalah2] = 0;

        game = Game.builder()
                .gameId(UUID.randomUUID())
                .turn(PLAYER1)
                .pits(pits)
                .gameWinner(GameWinner.UNKNOWN)
                .build();


        gameDto = GameDto.builder()
                .gameId(UUID.randomUUID())
                .turn(Player.PLAYER1)
                .pits(pitsToStringArray(pits))
                .gameWinner(com.sh.maleki.common.GameWinner.UNKNOWN)
                .build();


    }

    private static String[] pitsToStringArray(Integer[] pits) {
        String[] result = new String[pits.length];
        result = IntStream.range(0, pits.length)
                .mapToObj(idx -> idx + ":" + pits[idx])
                .collect(Collectors.toList()).toArray(result);
        return result;

    }
}