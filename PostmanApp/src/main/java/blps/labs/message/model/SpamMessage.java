package blps.labs.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpamMessage {
    List<String> subscribersEmails;
    List<SpamMessageUnit> spam;
}
