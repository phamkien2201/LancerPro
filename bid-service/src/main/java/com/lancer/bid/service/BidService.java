package com.lancer.bid.service;

import com.lancer.bid.dto.ApiResponse;
import com.lancer.bid.dto.request.BidRequest;
import com.lancer.bid.dto.response.BidResponse;
import com.lancer.bid.dto.response.ProjectResponse;
import com.lancer.bid.dto.response.UserResponse;
import com.lancer.bid.entity.Bid;
import com.lancer.bid.entity.BidStatus;
import com.lancer.bid.exception.AppException;
import com.lancer.bid.exception.ErrorCode;
import com.lancer.bid.mapper.BidMapper;
import com.lancer.bid.repository.BidRepository;
import com.lancer.bid.repository.httpclient.ProjectClient;
import com.lancer.bid.repository.httpclient.UserClient;
import com.lancer.event.dto.NotificationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BidService {

    BidRepository bidRepository;
    BidMapper bidMapper;
    UserClient userClient;
    ProjectClient projectClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    public BidResponse createBid(BidRequest request){
        ApiResponse<UserResponse> user = userClient.getUser(request.getFreelancerId());
        if (user == null || user.getResult() == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        ApiResponse<ProjectResponse> project = projectClient.getProjectById(request.getProjectId());
        if (project == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_EXISTED);
        }

        Bid bid = bidMapper.toBid(request);
        bid.setStatus(BidStatus.PENDING);
        bid = bidRepository.save(bid);
        ProjectResponse projectResponse = project.getResult();
        if (projectResponse == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_EXISTED);
        }

        sendBidNotification(projectResponse.getClientId(), projectResponse.getEmail(), bid);
        return bidMapper.toBidResponse(bid);
    }

    private void sendBidNotification(String clientId, String clientEmail, Bid bid) {
        NotificationEvent emailNotification = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(clientEmail)
                .subject("New Bid on Your Project")
                .body("A freelancer has placed a bid on your project. Check it now!")
                .build();

        kafkaTemplate.send("bid-placed-email", emailNotification);
        log.info("Email notification sent to client: {}", clientEmail);

        // Gửi thông báo trực tiếp (cho WebSocket, push notification,...)
        NotificationEvent pushNotification = NotificationEvent.builder()
                .channel("PUSH_NOTIFICATION")
                .recipient(clientId)  // Sử dụng clientId để gửi thông báo đến hệ thống frontend
                .subject("New Bid Notification")
                .body("A freelancer has bid on your project. Open the app to review it!")
                .build();

        kafkaTemplate.send("bid-placed-push", pushNotification);
        log.info("Push notification sent to client: {}", clientId);
    }


    public Page<BidResponse> getAllBids(PageRequest pageRequest) {
        return bidRepository
                .findAll(pageRequest)
                .map(bidMapper::toBidResponse);
    }

    public BidResponse getBidById(String id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cannot found project with id "+id));
        return bidMapper.toBidResponse(bid);
    }

    public Page<BidResponse> getBidsByProjectId(String projectId, PageRequest pageRequest) {
        return bidRepository.findByProjectId(projectId, pageRequest)
                .map(bidMapper::toBidResponse);
    }


    public Page<BidResponse> getBidsByFreelancerId(String freelancerId, PageRequest pageRequest) {
        return bidRepository.findByFreelancerId(freelancerId, pageRequest)
                .map(bidMapper::toBidResponse);
    }


    public BidResponse updateBid(String bidId, BidRequest request) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        bidMapper.updateBid(bid, request);
        bid = bidRepository.save(bid);
        return bidMapper.toBidResponse(bid);
    }
    public void updateBidStatus(String bidId, String status) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found!"));

        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid bid status: " + status);
        }

        bid.setStatus(status);
        bidRepository.save(bid);

        sendBidStatusNotification(bid);
    }

    private boolean isValidStatus(String status) {
        return BidStatus.PENDING.equals(status) ||
                BidStatus.ACCEPTED.equals(status) ||
                BidStatus.REJECTED.equals(status);
    }

    private void sendBidStatusNotification(Bid bid) {
        String subject = "Your bid status has been updated";
        String body = "Your bid for project " + bid.getProjectId() + " is now " + bid.getStatus();

        NotificationEvent emailEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(bid.getEmail())
                .subject(subject)
                .body(body)
                .build();
        kafkaTemplate.send("bid-status-email", emailEvent);

        NotificationEvent pushEvent = NotificationEvent.builder()
                .channel("PUSH")
                .recipient(bid.getFreelancerId())
                .subject(subject)
                .body(body)
                .build();
        kafkaTemplate.send("bid-status-push", pushEvent);

        log.info("Notification sent for bid status change: {}", bid.getId());
    }

    public void deleteBid(String id) {
        bidRepository.findById(id).ifPresent(bidRepository::delete);
    }

}
