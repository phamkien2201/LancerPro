package com.lancer.profile.service;

import com.lancer.profile.dto.request.PortfolioRequest;
import com.lancer.profile.dto.request.PortfolioUpdateRequest;
import com.lancer.profile.dto.request.WorkProfileUpdateRequest;
import com.lancer.profile.dto.response.PortfolioResponse;
import com.lancer.profile.dto.response.WorkProfileResponse;
import com.lancer.profile.entity.Portfolio;
import com.lancer.profile.entity.WorkProfile;
import com.lancer.profile.exception.AppException;
import com.lancer.profile.exception.ErrorCode;
import com.lancer.profile.mapper.PortfolioMapper;
import com.lancer.profile.mapper.WorkProfileMapper;
import com.lancer.profile.repository.PortfolioRepository;
import com.lancer.profile.repository.WorkProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PortfolioService {
    PortfolioRepository portfolioRepository;
    PortfolioMapper portfolioMapper;

    public PortfolioResponse createPortfolio(PortfolioRequest request) {
        Portfolio portfolio = portfolioMapper.toPortfolio(request);
        portfolio = portfolioRepository.save(portfolio);
        return portfolioMapper.toPortfolioResponse(portfolio);
    }

    public PortfolioResponse getPortfolio(String userId) {
        return portfolioMapper.toPortfolioResponse(portfolioRepository.findByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PortfolioResponse> getAllPortfolio() {
        var profiles = portfolioRepository.findAll();
        return profiles.stream().map(portfolioMapper::toPortfolioResponse).toList();
    }

    public PortfolioResponse updatePortfolio(String userId, PortfolioUpdateRequest request) {
        Portfolio portfolio = portfolioRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        portfolioMapper.updatePortfolio(portfolio, request);
        return portfolioMapper.toPortfolioResponse(portfolioRepository.save(portfolio));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deletePortfolio(String id) {
        portfolioRepository.findById(id).ifPresent(portfolioRepository::delete);
    }
}
