package com.example.demo.service;

import com.example.demo.model.Channel;
import com.example.demo.repository.ChannelRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final UserService userService;
    private final JwtService jwtService;
    public List<Channel> findALl() {
        return channelRepository.findAll();
    }

    public Channel save(Channel channel, String authHeader) {
        String token = jwtService.getTokenFromAuthHeader(authHeader);
        channel.setUserId(userService.getUserIdByToken(token));
        return channelRepository.save(channel);
    }

    public List<Channel> findUserChannelsByToken(String authHeader) {
        Long userId = userService.getUserIdByToken(jwtService.getTokenFromAuthHeader(authHeader));
        return channelRepository.findByUserId(userId);
    }
}
