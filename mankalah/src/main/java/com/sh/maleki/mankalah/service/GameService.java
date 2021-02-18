package com.sh.maleki.mankalah.service;


import com.sh.maleki.mankalah.web.model.GameDto;

import java.util.UUID;

public interface GameService {
    GameDto getGame(UUID gameId);
    GameDto createGame();
    GameDto makeMove(UUID gameId, Integer pitId);


}
