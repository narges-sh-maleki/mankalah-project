package com.sh.maleki.mankalah.web.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameDto {


    private UUID gameId;
    private String[] pits;
    private Player turn;
    private GameWinner gameWinner;
    @JsonFormat(shape =   JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime createdDateTime;
    @JsonFormat(shape =   JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime lastModifiedDate;

}
