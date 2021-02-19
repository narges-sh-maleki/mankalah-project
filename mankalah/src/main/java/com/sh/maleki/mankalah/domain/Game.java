package com.sh.maleki.mankalah.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Game  {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36,columnDefinition = "varchar(36)",updatable = false,nullable = false)
    @Type(type="uuid-char")
    private UUID gameId;
    private Integer[] pits;
    private Player turn;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDateTime;
    @UpdateTimestamp
    private Timestamp lastModifiedDate;
    @Builder.Default
    private GameWinner gameWinner = GameWinner.UNKNOWN;

    //Optimistic Lock to avoid concurrency problem
    @Version
    private Long version;

}
