package com.sh.maleki.mankalah.service;

import com.sh.maleki.common.GameDto;
import com.sh.maleki.common.Player;
import com.sh.maleki.mankalah.domain.Game;
import com.sh.maleki.mankalah.domain.GameWinner;
import com.sh.maleki.mankalah.exceptions.BusinessException;
import com.sh.maleki.mankalah.repositories.GameRepository;
import com.sh.maleki.mankalah.web.mapper.GameMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sh.maleki.mankalah.domain.Player.PLAYER1;
import static com.sh.maleki.mankalah.domain.Player.PLAYER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GameServiceImpl.class})
@EnableConfigurationProperties
@ConfigurationProperties()

class GameServiceImplTest {

    @Autowired
    private  Environment env;

    @Autowired
    private GameServiceImpl gameService;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private GameMapper gameMapper ;

   // GameMapper localMapper = Mappers.getMapper(GameMapper.class);

    private static GameDto gameDto;
    private static Game game;

    private static  Integer kalah1 = 6 ;
    private  static Integer kalah2 = 13 ;
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

        System.out.println("**************Active profile:" + Arrays.toString(env.getActiveProfiles()) + "************" );
       // gameDto = localMapper.GameToGameDto(game);


    }

    @Test
    void createGame() {
        //given
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameMapper.GameToGameDto(game)).thenReturn(gameDto);

        //when
        GameDto game = gameService.createGame();

        //then
        assertThat(game).isNotNull();

    }


    @ParameterizedTest
    @MethodSource("provideSomePlayConsecutiveMoves")
    void makeMoveHappyTest(Integer inputPit, Integer[] expectedPits, com.sh.maleki.mankalah.domain.Player expectedPlayer) {
        //given
        UUID uuid = game.getGameId();
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));
        final ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        when(gameRepository.save(captor.capture())).thenReturn(game);

        //when
        gameService.makeMove(uuid, inputPit);

        //then
        System.out.println(captor.getValue().toString());
        //since the final result is fetched from a *MOCKED* repository,
        // we cannot assert the real output, so we capture the Game object before save operation
        // and assert that.
        assertThat(captor.getValue().getPits()).isEqualTo(expectedPits);
        assertThat(captor.getValue().getGameWinner()).isEqualTo(GameWinner.UNKNOWN);
        assertThat(captor.getValue().getTurn()).isEqualTo(expectedPlayer);

    }

   @ParameterizedTest
   @MethodSource("provideSomeWrongTurnParams")
   void makeMoveWrongTurnTest(Integer[] inputPits, com.sh.maleki.mankalah.domain.Player inputTurn, Integer inputPitId) {
        //given
       game = Game.builder()
               .gameId(UUID.randomUUID())
               .turn(inputTurn)
               .pits(inputPits)
               .gameWinner(GameWinner.UNKNOWN)
               .build();

        UUID uuid = game.getGameId();
       when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        //when
        Throwable thrown = catchThrowable(() -> {
            gameService.makeMove(uuid, inputPitId);
        });

        //then
        assertThat(thrown)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("turn");
    }

    @ParameterizedTest
    @ValueSource(ints = {6,13})
    void makeMoveKalahPitTest(int inputPit) {
        //given
        UUID uuid = game.getGameId();
        Integer pitId = inputPit;
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        //when
        Throwable thrown = catchThrowable(() -> {
            gameService.makeMove(uuid,pitId);
        });

        //then
        assertThat(thrown)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("kalah");
    }

    private static Stream<Arguments> provideSomePlayConsecutiveMoves() {
        return Stream.of(
                Arguments.of(0, new Integer[]{0,7,7,7,7,7,1, 6,6,6,6,6,6,0},PLAYER1),
                Arguments.of(1, new Integer[]{0,0,8,8,8,8,2, 7,7,6,6,6,6,0},PLAYER2),
                Arguments.of(7, new Integer[]{1,0,8,8,8,8,2, 0,8,7,7,7,7,1},PLAYER1)

        );
    }

    private static Stream<Arguments> provideSomeWrongTurnParams() {
        return Stream.of(
                Arguments.of(new Integer[]{6,6,6,6,6,6,0, 6,6,6,6,6,6,0},PLAYER1,7),
                Arguments.of(new Integer[]{0,7,7,7,7,7,1, 6,6,6,6,6,6,0},PLAYER1,8),
                Arguments.of(new Integer[]{0,0,8,8,8,8,2, 7,7,6,6,6,6,0},PLAYER2,0),
                Arguments.of(new Integer[]{1,0,8,8,8,8,2, 0,8,7,7,7,7,1},PLAYER1,12)

        );
    }



    private static String[] pitsToStringArray(Integer[] pits){
        String[] result = new String[pits.length];
        result = IntStream.range(0,pits.length)
                .mapToObj(idx-> idx + ":" + pits[idx])
                .collect(Collectors.toList()).toArray(result);
        return result;

    }

    private static void initGameObjects(){
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


}