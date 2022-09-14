package ru.msu.cmc.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Date;

@Entity
@Table(name = "team_participance")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Team_participance implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "team_part_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    @ToString.Exclude
    @NonNull
    private Sportsman person;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    @ToString.Exclude
    @NonNull
    private Team team;

    @Column(nullable = false, name = "part_start")
    @NonNull
    private String part_start;

    @Column(nullable = false, name = "part_finish")
    @NonNull
    private String part_finish;

}
