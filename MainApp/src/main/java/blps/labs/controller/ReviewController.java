package blps.labs.controller;

import blps.labs.dto.ReviewDTO;
import blps.labs.entity.Car;
import blps.labs.entity.RejectedUserReview;
import blps.labs.entity.Review;
import blps.labs.message.service.MessageService;
import blps.labs.service.RejectedUserReviewService;
import blps.labs.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final RejectedUserReviewService rejectedUserReviewService;
    private final MessageService messageService;

    @Autowired
    public ReviewController(ReviewService reviewService, RejectedUserReviewService rejectedUserReviewService, MessageService messageService) {
        this.reviewService = reviewService;
        this.rejectedUserReviewService = rejectedUserReviewService;
        this.messageService = messageService;
    }

    @PostMapping("/")
    public ResponseEntity<String> addReview(@RequestBody ReviewDTO reviewDTO) {
        log.info("Request to add new review");
        Review review = new Review(reviewDTO);
        Car car = new Car(reviewDTO);
        review.setCar(car);
        reviewService.saveReview(review);
        messageService.sendMessageWhenReviewAdd(review);
        log.info("Review by {} added successfully.", review.getAuthorName());
        return new ResponseEntity<>("Отзыв сохранен", HttpStatus.OK);
    }

    @PatchMapping("/approval/{id}")
    public ResponseEntity<String> changeApproval(@PathVariable(name = "id") Long id,
                                                 @RequestBody Map<String, String> payload) {
        log.info("Request to change review {} approval", id);
        String approved = payload.get("approved");
        String message = payload.get("message");
        if (approved == null)
            return new ResponseEntity<>("Не указано значение approved", HttpStatus.BAD_REQUEST);
        reviewService.changeApproval(id, Boolean.valueOf(approved), message);
        messageService.sendMessageWhenReviewChecked(id, message, Boolean.valueOf(approved));
        if (!Boolean.parseBoolean(approved))
            reviewService.deleteReviewById(id);
        log.info("Review with id {} handled.", id);
        return new ResponseEntity<>("Подтверждение отзыва изменено", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable(name = "id") Long id) {
        reviewService.deleteReviewById(id);
        log.info("Review with id {} deleted.", id);
        return new ResponseEntity<>("Отзыв удален", HttpStatus.OK);
    }

    @GetMapping("/approved/{approved}")
    public ResponseEntity<List<Review>> getReviewByApproval(@PathVariable Boolean approved) {
        List<Review> reviews = reviewService.findAllByApproved(approved);
        log.info("Getting list of reviews with {} approval.", approved);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/authorName")
    public ResponseEntity<List<Review>> getReviewByAuthorName(Authentication authentication) {
        String authorName = authentication.getName();
        List<Review> reviews = reviewService.findAllByAuthorName(authorName);
        log.info("Getting list of reviews by {}.", authorName);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<RejectedUserReview>> getRejectedUserReview(Authentication authentication) {
        String username = authentication.getName();
        List<RejectedUserReview> rejectedReviews = rejectedUserReviewService.findAllByUsername(username);
        log.info("Getting list of rejected reviews by {}.", username);
        return new ResponseEntity<>(rejectedReviews, HttpStatus.OK);
    }
}
