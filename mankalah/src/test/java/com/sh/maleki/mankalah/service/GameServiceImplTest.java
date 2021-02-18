package com.sh.maleki.mankalah.service;

import com.sh.maleki.common.GameDto;
import com.sh.maleki.common.Player;
import com.sh.maleki.mankalah.domain.Game;
import com.sh.maleki.mankalah.repositories.GameRepository;
import com.sh.maleki.mankalah.web.mapper.GameMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

import static com.sh.maleki.mankalah.domain.Player.PLAYER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GameServiceImpl.class)
//@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
class GameServiceImplTest {

    @Autowired
    Environment env;

    @Autowired
    //@InjectMocks
    GameServiceImpl gameCore;
    @MockBean
    GameRepository gameRepository;
    @MockBean
    GameMapper gameMapper;

    GameDto gameDto;
    Game game;

    @BeforeEach
    void setUp() {

        System.out.println("**************Active profile:" + Arrays.toString(env.getActiveProfiles()) );

        game = Game.builder().turn(PLAYER1).build();

        gameDto = GameDto.builder().turn(Player.PLAYER1).build();
    }

    @Test
    void createGame() {
        //given
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameMapper.GameToGameDto(any(Game.class))).thenReturn(gameDto);
        //when
        GameDto game = gameCore.createGame();
        //then
        assertThat(game).isNotNull();
        //TODO: convert Enum in DTO and Domain layer to each other
        //assertThat(game.getTurn()).isEqualTo(PLAYER);
    }
}