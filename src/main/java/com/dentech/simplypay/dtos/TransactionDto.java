package com.dentech.simplypay.dtos;

import java.math.BigDecimal;

public record TransactionDto (BigDecimal value, Long sendId, Long receiverId) {
}
