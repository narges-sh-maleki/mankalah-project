package com.sh.maleki.mankalah.web.mapper;

import com.sh.maleki.common.GameDto;
import com.sh.maleki.mankalah.domain.Game;
import org.mapstruct.Mapper;

@Mapper(uses ={ PitsMapper.class, DateMapper.class})

public interface GameMapper {

    GameDto GameToGameDto(Game game);

}
