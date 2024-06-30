package com.gildastema.demoqrserver.repositories;

import com.gildastema.demoqrserver.entities.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository  extends JpaRepository<Code, Integer>{
}
