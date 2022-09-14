package ru.msu.cmc.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sports")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Sports implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "sport_id")
    private Long id;

    @Column(nullable = false, name = "sport_name")
    @NonNull
    private String sport_name;

    @Column(nullable = false, name = "is_team_sport")
    @NonNull
    private Boolean is_team_sport;

}
