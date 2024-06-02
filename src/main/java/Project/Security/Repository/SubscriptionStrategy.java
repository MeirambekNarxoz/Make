package Project.Security.Repository;

import Project.Security.Entity.User;
import Project.Security.Entity.Subscribtion;

public interface SubscriptionStrategy {
    void processSubscription(User user, Subscribtion subscription);
}
