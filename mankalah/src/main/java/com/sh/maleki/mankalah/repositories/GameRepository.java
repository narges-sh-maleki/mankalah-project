package com.sh.maleki.mankalah.repositories;

import com.sh.maleki.mankalah.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {

}
