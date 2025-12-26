package com.CryptoProject.CryptoInfosys.mapper;

import com.CryptoProject.CryptoInfosys.dto.TradeDTO;
import com.CryptoProject.CryptoInfosys.model.Trade;

public class TradeMapper {

    public static TradeDTO toDTO(Trade trade) {
        TradeDTO dto = new TradeDTO();
        dto.id = trade.getId();
        dto.assetSymbol = trade.getAssetSymbol();
        dto.side = trade.getSide().name();
        dto.quantity = trade.getQuantity();
        dto.price = trade.getPrice();
        dto.fee = trade.getFee();
        dto.executedAt = trade.getExecutedAt();
        return dto;
    }
}
