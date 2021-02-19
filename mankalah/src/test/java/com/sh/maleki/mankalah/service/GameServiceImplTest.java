package com.sh.maleki.mankalah.service;

import com.sh.maleki.common.GameDto;
import com.sh.maleki.mankalah.domain.Game;
import com.sh.maleki.mankalah.domain.GameWinner;
import com.sh.maleki.mankalah.repositories.GameRepository;
import com.sh.maleki.mankalah.web.mapper.DateMapper;
import com.sh.maleki.mankalah.web.mapper.GameMapper;
import com.sh.maleki.mankalah.web.mapper.GameMapperImpl;
import com.sh.maleki.mankalah.web.mapper.PitsMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.sh.maleki.mankalah.domain.Player.PLAYER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GameServiceImpl.class, GameMapperImpl.class, PitsMapper.class, DateMapper.class})
//@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
class GameServiceImplTest {

    @Autowired
    private  Environment env;

    @Autowired
    GameServiceImpl gameService;
    @MockBean
    GameRepository gameRepository;

    @Autowired
    GameMapper gameMapper;

    GameDto gameDto;
    Game game;

    Integer kalah1 = 6;
    Integer kalah2 = 13;
    Integer stones = 6;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void setUp() {
      //  gameMapper = Mappers.getMapper(GameMapper.class);
        System.out.println("**************Active profile:" + Arrays.toString(env.getActiveProfiles()) + "************" );


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


        gameDto = gameMapper.GameToGameDto(game);


    }

    @Test
    void createGame() {
        //given
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        //when
        GameDto game = gameService.createGame();
        //then
        assertThat(game).isNotNull();
        //TODO: convert Enum in DTO and Domain layer to each other
        //assertThat(game.getTurn()).isEqualTo(PLAYER);
    }

    @Test
    void makeMoveHappyTest() {
        //given
        UUID uuid = game.getGameId();
        Integer pitId = 1;
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));
       // when(gameRepository.save(game)).thenReturn(game);
       // Integer afterMovePits[] = new Integer[]{0,stones+1,stones+1,stones+1,stones+1,stones+1,st};

        //when
        GameDto resultGameDto = gameService.makeMove(uuid,pitId);
        //then
        assertThat(resultGameDto).isNotNull();
        System.out.println(resultGameDto.toString());
    }


}