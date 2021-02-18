package com.sh.maleki.mankalah.domain;

import lombok.*;

import javax.persistence.Entity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
//@Entity
public class TODO_Player {
    private Integer playerId;
    private String name;

}
