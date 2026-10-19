package com.scc.model;

/**
 * Immutable record holding one dormitory allocation (5 attributes).
 */
public record User(
        int id,
        String studentName,
        String building,
        String roomNo,
        double monthlyRent) {
}