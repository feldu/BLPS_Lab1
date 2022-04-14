package blps.labs.service;

import blps.labs.entity.Car;
import blps.labs.entity.RejectedUserReview;
import blps.labs.entity.Review;
import blps.labs.entity.User;
import blps.labs.exception.DataNotFoundException;
import blps.labs.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
@Service
public class ReviewService {

    private final TransactionTemplate transactionTemplate;
    private final ReviewRepository reviewRepository;
    private final CarService carService;
    private final UserService userService;
    private final RejectedUserReviewService rejectedUserReviewService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, CarService carService, PlatformTransactionManager transactionManager, UserService userService, RejectedUserReviewService rejectedUserReviewService) {
        this.reviewRepository = reviewRepository;
        this.carService = carService;
        this.userService = userService;
        this.rejectedUserReviewService = rejectedUserReviewService;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void saveReview(Review review) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    Car carFromDB = carService.findCar(review.getCar());
                    review.setCar(carFromDB);
                } catch (DataNotFoundException ignored) {
                } finally {
                    reviewRepository.save(review);
                }
            }
        });
    }

    public Review findReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Review not found"));
    }

    public List<Review> findAllByApproved(boolean approved) {
        return reviewRepository.findAllByApproved(approved);
    }


    public void deleteReviewById(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> findAllByAuthorName(String authorName) {
        return reviewRepository.findAllByAuthorName(authorName);
    }

    public void changeApproval(Long id, Boolean approved, String message) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Review review = findReviewById(id);
                if (review.isApproved() == approved && approved)
                    return;
                review.setApproved(approved);
                if (review.isApproved()) {
                    saveReview(review);
                    return;
                }
                if (!approved && message != null) {
                    try {
                        User author = userService.findUserByUsername(review.getAuthorName());
                        RejectedUserReview rejectedReview = new RejectedUserReview();
                        rejectedReview.setUser(author);
                        rejectedReview.setMessage(message);
                        rejectedUserReviewService.save(rejectedReview);
                    } catch (UsernameNotFoundException ignored) {
                        log.warn("Review belongs to not auth user: alert for user not created");
                    }
                }
                reviewRepository.deleteById(review.getId());
                log.debug("{} review with changed approval {} saved in DB", review.getCar().getCarModel(), review.getAuthorName());
            }
        });
    }
}
