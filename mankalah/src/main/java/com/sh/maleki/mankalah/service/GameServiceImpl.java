package com.sh.maleki.mankalah.service;

import com.sh.maleki.mankalah.domain.Game;
import com.sh.maleki.mankalah.domain.GameWinner;
import com.sh.maleki.mankalah.domain.Player;
import com.sh.maleki.mankalah.exceptions.BusinessException;
import com.sh.maleki.mankalah.exceptions.ExceptionCodes;
import com.sh.maleki.mankalah.repositories.GameRepository;
import com.sh.maleki.mankalah.web.mapper.GameMapper;
import com.sh.maleki.mankalah.web.model.GameDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
@Setter
@ConfigurationProperties(prefix = "game.properties")
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private Integer kalah1;
    private Integer kalah2;
    private Integer stones;


    @Override
    public GameDto getGame(UUID gameId) {
        return gameMapper.GameToGameDto(gameRepository.findById(gameId)
                .orElseThrow(() -> {
                    throw new BusinessException(ExceptionCodes.GAME_NOT_FOUND ,"Game Id: " + gameId.toString());
                }));
    }

    @Transactional
    @Override
    public GameDto createGame() {
        Integer[] pits = new Integer[kalah2 + 1];
        Arrays.fill(pits, stones);
        pits[kalah1] = 0;
        pits[kalah2] = 0;

        return gameMapper.GameToGameDto(gameRepository.save(Game.builder()
                .turn(Player.PLAYER1)
                .pits(pits)
                .build()));

    }

    @Transactional
    @Override
    public GameDto makeMove(UUID gameId, Integer pitId) {

        Game game = gameRepository.findById(gameId).orElseThrow(() -> {
            throw new BusinessException(ExceptionCodes.GAME_NOT_FOUND , "Game Id: " + gameId.toString());
        });

        Player turn = game.getTurn();

        //to keep the origin safe from extra modification
        //Integer[] pits = Arrays.copyOf(game.getPits(),game.getPits().length)  ;

        Integer[] pits = game.getPits();
        if (!game.getGameWinner().equals(GameWinner.UNKNOWN))
            throw new BusinessException(ExceptionCodes.INVALID_GAME, "Game Winner: " + game.getGameWinner().toString());
        if (!isCurrPlayerPit(turn, pitId))
            throw new BusinessException(ExceptionCodes.WRONG_TURN,
                    Arrays.asList("Pit Id: " + pitId.toString(), "Turn: " + game.getTurn()));
        if (isEmptyPit(pitId, pits))
            throw new BusinessException(ExceptionCodes.EMPTY_PIT,"Pit Id: " + pitId.toString());
        if (isKalahPit(pitId))
            throw new BusinessException(ExceptionCodes.KALAH_PIT,"Pit Id: " + pitId.toString());

        log.debug("****************Before Move: " + Arrays.toString(pits));
        log.debug("****************Current Turn: " + turn);

        Integer stones = pits[pitId];

        //pick the  stones
        pits[pitId] = 0;

        //dropping the stones
        Integer currPit = pitId;
        for (int i = 0; i < stones; i++) {
            Integer nextValidPit = findNextValidPit(turn, currPit);
            //drop one stone
            pits[nextValidPit]++;
            currPit = nextValidPit;

        }

        //findNextTurn
        game.setTurn(findNextTurn(turn, pits, currPit));

        //setGameStatus();
        if (isGameOver(pits))
            game.setGameWinner(findWinner(pits));

        log.debug("*****************After Move: " + Arrays.toString(pits));
        log.debug("*****************Next Turn: " + game.getTurn());


        Game savedGame = gameRepository.save(game);
        return gameMapper.GameToGameDto(savedGame);
    }

    private Boolean isGameOver(Integer[] pits) {
        boolean firstSideEmpty = IntStream.range(0, kalah1 - 1).allMatch(idx -> pits[idx].equals(0));
        boolean secondSideEmpty = IntStream.range(kalah1 + 1, kalah2 - 1).allMatch(idx -> pits[idx].equals(0));
        return firstSideEmpty || secondSideEmpty;

    }

    private GameWinner findWinner(Integer[] pits) {
        if (pits[kalah1] > pits[kalah2])
            return GameWinner.PLAYER1;

        if (pits[kalah1] < pits[kalah2])
            return GameWinner.PLAYER2;

        if (pits[kalah1].equals(pits[kalah2]))
            return GameWinner.DRAW;

        return GameWinner.UNKNOWN;

    }

    private Player findNextTurn(Player currTurn, Integer[] pits, Integer lastPit) {
        boolean isCurrPlayerTurn = false;

        if (isKalahPit(lastPit))
            isCurrPlayerTurn = true;
        else {
            //own side?
            if (isCurrPlayerPit(currTurn, lastPit)) {
                isCurrPlayerTurn = true;
                lastOwnEmptyPitRule(currTurn, lastPit, pits);
            }
        }
        return (isCurrPlayerTurn) ? currTurn : opponentPlayer(currTurn);
    }

    private Player opponentPlayer(Player currTurn) {
        if (currTurn == Player.PLAYER1)
            return Player.PLAYER2;
        else
            return Player.PLAYER1;
    }

    private Integer findOppositePit(Integer pitId) {
        if (pitId.equals(kalah1) || pitId.equals(kalah2))
            return -1;//kalah pit

        return ((kalah1 * 2) - pitId);

    }

    private void lastOwnEmptyPitRule(Player currTurn, Integer pitId, Integer[] pits) {
        boolean isCurrPlayerTurn = isCurrPlayerPit(currTurn, pitId);
        if (pitId.equals(kalah1) || pitId.equals(kalah2))
            return;
        if (isCurrPlayerTurn && (pits[pitId].equals(1))) {
            Integer kalahId = (currTurn == Player.PLAYER1) ? kalah1 : kalah2;
            pits[kalahId] = pits[pitId] + pits[findOppositePit(pitId)];
            pits[pitId] = 0;
            pits[findOppositePit(pitId)] = 0;
        }
    }

    private Integer findNextValidPit(Player turn, Integer pitId) {
        Integer nextValidPit;
        nextValidPit = pitId + 1;
        Integer opponentKalahId = (turn == Player.PLAYER1) ? kalah2 : kalah1;
        nextValidPit = (nextValidPit.equals(opponentKalahId)) ? nextValidPit + 1 : nextValidPit;
        nextValidPit = (nextValidPit > kalah2) ? 0 : nextValidPit;
        return nextValidPit;
    }


    private boolean isCurrPlayerPit(Player curTurn, Integer pitId) {
        if (curTurn == Player.PLAYER1)
            return (0 <= pitId && pitId <= kalah1);
        else
            return ((kalah1 + 1) <= pitId && pitId <= kalah2);

    }

    private boolean isEmptyPit(Integer pitId, Integer[] pits) {

        return (pits[pitId].equals(0));

    }

    private boolean isKalahPit(Integer pitId) {

        return (pitId.equals(kalah1) || pitId.equals(kalah2));

    }


}
