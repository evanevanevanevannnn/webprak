package ru.msu.cmc.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "seats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Seats implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "seats_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "competition_id")
    @ToString.Exclude
    @NonNull
    private Competition competition;

    @Column(nullable = false, name = "seats_type")
    @NonNull
    private String type;

    @Column(nullable = false, name = "seats_amount")
    @NonNull
    private Long amount;

    @Column(nullable = false, name = "seats_price")
    @NonNull
    private Long price;

}
