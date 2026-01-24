package com.example.demo.service;

import com.example.demo.dto.FruvoCoinResponse;
import com.example.demo.entity.FruvoCoin;
import com.example.demo.entity.User;
import com.example.demo.repository.FruvoCoinRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FruvoCoinService {

    private final FruvoCoinRepository coinRepository;
    private final UserRepository userRepository;

    public FruvoCoinService(FruvoCoinRepository coinRepository, UserRepository userRepository) {
        this.coinRepository = coinRepository;
        this.userRepository = userRepository;
    }

    public FruvoCoinResponse getCoins(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FruvoCoin fruvoCoin = coinRepository.findByUser(user)
                .orElseGet(() -> {
                    FruvoCoin newCoin = new FruvoCoin(user);
                    return coinRepository.save(newCoin);
                });

        FruvoCoinResponse response = new FruvoCoinResponse();
        response.setCoins(fruvoCoin.getCoins());
        response.setScore(fruvoCoin.getScore());
        return response;
    }

    public FruvoCoinResponse deductCoins(Long userId, Integer amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FruvoCoin fruvoCoin = coinRepository.findByUser(user)
                .orElseGet(() -> {
                    FruvoCoin newCoin = new FruvoCoin(user);
                    return coinRepository.save(newCoin);
                });

        if (fruvoCoin.getCoins() < amount) {
            throw new RuntimeException("Insufficient Fruvo coins. Required: " + amount + ", Available: " + fruvoCoin.getCoins());
        }

        fruvoCoin.setCoins(fruvoCoin.getCoins() - amount);
        fruvoCoin = coinRepository.save(fruvoCoin);

        FruvoCoinResponse response = new FruvoCoinResponse();
        response.setCoins(fruvoCoin.getCoins());
        response.setScore(fruvoCoin.getScore());
        response.setMessage(amount + " Fruvo coins deducted successfully");
        return response;
    }

    public boolean hasEnoughCoins(Long userId, Integer amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FruvoCoin fruvoCoin = coinRepository.findByUser(user)
                .orElseGet(() -> {
                    FruvoCoin newCoin = new FruvoCoin(user);
                    return coinRepository.save(newCoin);
                });

        return fruvoCoin.getCoins() >= amount;
    }

    public FruvoCoinResponse addCoins(Long userId, Integer amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FruvoCoin fruvoCoin = coinRepository.findByUser(user)
                .orElseGet(() -> {
                    FruvoCoin newCoin = new FruvoCoin(user);
                    return coinRepository.save(newCoin);
                });

        fruvoCoin.setCoins(fruvoCoin.getCoins() + amount);
        fruvoCoin = coinRepository.save(fruvoCoin);

        FruvoCoinResponse response = new FruvoCoinResponse();
        response.setCoins(fruvoCoin.getCoins());
        response.setScore(fruvoCoin.getScore());
        response.setMessage(amount + " Fruvo coins added successfully");
        return response;
    }

    public FruvoCoinResponse addScore(Long userId, Integer points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FruvoCoin fruvoCoin = coinRepository.findByUser(user)
                .orElseGet(() -> {
                    FruvoCoin newCoin = new FruvoCoin(user);
                    return coinRepository.save(newCoin);
                });

        fruvoCoin.setScore(fruvoCoin.getScore() + points);
        fruvoCoin = coinRepository.save(fruvoCoin);

        FruvoCoinResponse response = new FruvoCoinResponse();
        response.setCoins(fruvoCoin.getCoins());
        response.setScore(fruvoCoin.getScore());
        response.setMessage(points + " points added successfully");
        return response;
    }
}
