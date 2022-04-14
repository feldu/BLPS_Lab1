package blps.labs.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewMessage implements Serializable {
    private long reviewId;
    private String email;
    private String name;
    private String carModel;

}
