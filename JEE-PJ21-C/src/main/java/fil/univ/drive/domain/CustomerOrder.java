package fil.univ.drive.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class CustomerOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<OrderEntry> orderEntries;
	private LocalDate dateOrdered;

	public CustomerOrder(List<OrderEntry> orderEntries, LocalDate dateOrdered) {
		this.orderEntries = orderEntries;
		this.dateOrdered = dateOrdered;
	}
}
