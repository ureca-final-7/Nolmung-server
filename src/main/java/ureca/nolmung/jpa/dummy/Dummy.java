package ureca.nolmung.jpa.dummy;

import jakarta.persistence.*;
import lombok.*;
import ureca.nolmung.jpa.config.BaseEntity;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dummy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private int age;
}
