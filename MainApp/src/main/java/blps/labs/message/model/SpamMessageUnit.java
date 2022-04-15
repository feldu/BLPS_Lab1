package blps.labs.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpamMessageUnit {
    long id;
    String author;
    String car;
}
