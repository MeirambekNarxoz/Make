package Project.Security.Service;

import Project.Security.Entity.Subscribtion;
import Project.Security.Repository.SubscriptionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionFactory {
    @Autowired
    private BasicSubscriptionStrategy basicSubscriptionStrategy;
    @Autowired
    private StandardSubscriptionStrategy standardSubscriptionStrategy;
    @Autowired
    private PremiumSubscriptionStrategy premiumSubscriptionStrategy;
    @Autowired
    private OrdinarySubscriptionStrategy ordinarySubscriptionStrategy;

    public SubscriptionStrategy getStrategy(Subscribtion subscription) {
        switch (subscription.getName().toUpperCase()) {
            case "BASE":
                return basicSubscriptionStrategy;
            case "STANDARD":
                return standardSubscriptionStrategy;
            case "PREMIUM":
                return premiumSubscriptionStrategy;
            case "ORDINARY":
                return ordinarySubscriptionStrategy;
            default:
                throw new IllegalArgumentException("Unknown subscription type: " + subscription.getName());
        }
    }
}
