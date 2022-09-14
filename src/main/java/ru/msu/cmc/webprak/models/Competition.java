package ru.msu.cmc.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Date;

@Entity
@Table(name = "competition")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Competition implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "competition_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sport_id")
    @ToString.Exclude
    @NonNull
    private Sports sport;

    @Column(nullable = false, name = "tournament")
    @NonNull
    private String tournament;

    @Column(name = "comp_date")
    private String comp_date;

    @Column(name = "venue")
    private String venue;

    @Column(name = "score")
    private String score;

}
