package com.gildastema.demoqrserver.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "codes")
@NoArgsConstructor
@Getter @Setter
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    private String codePath;
    private LocalDateTime createdAt;

    public Code(String content) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

}