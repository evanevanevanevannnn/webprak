package ru.msu.cmc.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Date;

@Entity
@Table(name = "sportsman")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Sportsman implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "person_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_team")
    @ToString.Exclude
    private Team current_team;

    @Column(nullable = false, name = "name")
    @NonNull
    private String name;

    @Column(name = "birth_date")
    private String birth_date;

}
