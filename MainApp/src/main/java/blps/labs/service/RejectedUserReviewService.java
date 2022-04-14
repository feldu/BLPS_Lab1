package blps.labs.service;

import blps.labs.entity.RejectedUserReview;
import blps.labs.entity.Review;
import blps.labs.entity.User;
import blps.labs.repository.RejectedUserReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RejectedUserReviewService {
    private final RejectedUserReviewRepository rejectedUserReviewRepository;
    private final UserService userService;

    @Autowired
    public RejectedUserReviewService(RejectedUserReviewRepository rejectedUserReviewRepository, UserService userService) {
        this.rejectedUserReviewRepository = rejectedUserReviewRepository;
        this.userService = userService;
    }

    public void save(RejectedUserReview review) {
        rejectedUserReviewRepository.save(review);
    }

    public List<RejectedUserReview> findAllByUsername(String username) {
        return rejectedUserReviewRepository.findAllByUser_Username(username);
    }

    public RejectedUserReview createRejectedReview(Review review, String message) {
        try {
            User author = userService.findUserByUsername(review.getAuthorName());
            RejectedUserReview rejectedReview = new RejectedUserReview();
            rejectedReview.setUser(author);
            rejectedReview.setMessage(message);
            return rejectedReview;
        } catch (UsernameNotFoundException ignored) {
            log.warn("Review belongs to not auth user: alert for user not created");
            return null;
        }
    }
}
