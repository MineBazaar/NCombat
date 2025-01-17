package com.notpatch.nCombat.manager;

import com.notpatch.nCombat.integration.Integration;
import com.notpatch.nCombat.integration.sub.PlaceholderApiIntegration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntegrationManager {

    private final List<Integration> integrations = new ArrayList<>();

    public IntegrationManager() {
        integrations.add(new PlaceholderApiIntegration());

    }

    public void initializeIntegrations() {
        for (Integration integration : integrations) {
            try {
                integration.initialize();

            } catch (IllegalStateException e) {
                org.bukkit.Bukkit.getLogger().warning(e.getMessage());
            }
        }
    }

    public List<Integration> getActiveIntegrations() {
        List<Integration> activeIntegrations = new ArrayList<>();
        for (Integration integration : integrations) {
            if (integration.isEnabled()) {
                activeIntegrations.add(integration);
            }
        }
        return activeIntegrations;
    }

    public boolean isIntegrationActive(Class<? extends Integration> integrationClass) {
        for (Integration integration : integrations) {
            if (integration.getClass().equals(integrationClass) && integration.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public <T extends Integration> Optional<T> getIntegration(Class<T> clazz) {
        return (Optional<T>) integrations.stream()
                .filter(integration -> integration.getClass().equals(clazz) && integration.isEnabled())
                .findFirst();
    }

}
