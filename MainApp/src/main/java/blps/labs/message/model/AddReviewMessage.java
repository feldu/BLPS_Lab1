package blps.labs.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AddReviewMessage implements Serializable {
    private long reviewId;
    private String email;
    private String name;
    private String carModel;

}
