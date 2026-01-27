package com.CryptoProject.CryptoInfosys.mapper;

import com.CryptoProject.CryptoInfosys.dto.TradeDTO;
import com.CryptoProject.CryptoInfosys.model.Trade;

public class TradeMapper {

    public static TradeDTO toDTO(Trade trade) {
        TradeDTO dto = new TradeDTO();
        dto.setId(trade.getId());
        dto.setAssetSymbol(trade.getAssetSymbol());
        dto.setSide(trade.getSide());
        dto.setQuantity(trade.getQuantity());
        dto.setPrice(trade.getPrice());
        dto.setFee(trade.getFee());
        dto.setExecutedAt(trade.getExecutedAt());
        return dto;
    }
}
