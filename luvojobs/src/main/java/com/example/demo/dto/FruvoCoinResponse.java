package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Fruvo coin response DTO")
public class FruvoCoinResponse {

    @Schema(description = "Current coins balance")
    private Integer coins;

    @Schema(description = "User score")
    private Integer score;

    @Schema(description = "Success message")
    private String message;

    // Getters and Setters
    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
