package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.model.Holding;
import com.CryptoProject.CryptoInfosys.repository.HoldingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioContextServiceImpl implements PortfolioContextService {

    private final HoldingRepository holdingRepository;

    public PortfolioContextServiceImpl(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    @Override
public String buildContextForUser(String email) {

    List<Holding> holdings =
        holdingRepository.findByUser_Email(email);

    if (holdings.isEmpty()) {
        return "User has no crypto holdings.";
    }

    StringBuilder context = new StringBuilder("User portfolio:\n");

    for (Holding h : holdings) {
        context.append(
            String.format(
                "- %s (%s): %.4f units @ avg price %.2f\n",
                h.getSymbol(),
                h.getExchange(),
                h.getQuantity(),
                h.getPrice()
            )
        );
    }

    context.append("\nAnalyze risk, diversification, and trends.");

    return context.toString();
}

}
