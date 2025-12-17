package com.example.demo.controller;

import com.example.demo.model.Channel;
import com.example.demo.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("channel")
public class ChannelController {
    private final ChannelService channelService;

    @GetMapping("all_channel")
    public ResponseEntity<List<Channel>> findAllChannels() {
        return ResponseEntity.ok(channelService.findALl());
    }

    @GetMapping("find_channel_by_token")
    public ResponseEntity<List<Channel>> findChannelByToken(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(channelService.findUserChannelsByToken(authHeader));
    }

    @PostMapping("create_channel")
    public ResponseEntity<Channel> createChannel(@RequestBody Channel channel, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(channelService.save(channel, authHeader));
    }
}
