package com.bench.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bench.ws.dto.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByOrderByTimestampAsc();
}