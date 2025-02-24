package com.lancer.project.service;

import com.lancer.project.dto.ApiResponse;
import com.lancer.project.dto.request.BidRequest;
import com.lancer.project.dto.request.ProjectRequest;
import com.lancer.project.dto.response.BidResponse;
import com.lancer.project.dto.response.ProjectResponse;
import com.lancer.project.dto.response.UserResponse;
import com.lancer.project.entity.Bid;
import com.lancer.project.entity.Project;
import com.lancer.project.entity.ProjectStatus;
import com.lancer.project.exception.AppException;
import com.lancer.project.exception.ErrorCode;
import com.lancer.project.mapper.BidMapper;
import com.lancer.project.mapper.ProjectMapper;
import com.lancer.project.repository.BidRepository;
import com.lancer.project.repository.ProjectRepository;
import com.lancer.project.repository.httpclient.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BidService {

    BidRepository bidRepository;
    BidMapper bidMapper;
    UserClient userClient;
    ProjectRepository projectRepository;

    public BidResponse createBid(BidRequest request){
        ApiResponse<UserResponse> user = userClient.getUser(request.getFreelancerId());
        if (user == null || user.getResult() == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        ProjectResponse project = projectRepository.findProjectById(request.getProjectId());
        if (project == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_EXISTED);
        }

        if (request.getPrice().compareTo(project.getBudgetMin()) < 0 ||
                request.getPrice().compareTo(project.getBudgetMax()) > 0) {
            throw new AppException(ErrorCode.BID_PRICE_OUT_OF_RANGE);
        }

        Bid bid = bidMapper.toBid(request);
        bid.setStatus(ProjectStatus.PENDING);
        bid = bidRepository.save(bid);
        return bidMapper.toBidResponse(bid);
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
    }

    private boolean isValidStatus(String status) {
        return ProjectStatus.PENDING.equals(status) ||
                ProjectStatus.ACCEPTED.equals(status) ||
                ProjectStatus.REJECTED.equals(status);
    }

    public void deleteBid(String id) {
        bidRepository.findById(id).ifPresent(bidRepository::delete);
    }

}
