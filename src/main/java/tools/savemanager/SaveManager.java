package tools.savemanager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SaveManager {

    private final SaveCache saveCache;
    private final ScheduledExecutorService executorService;

    public SaveManager() {
        this.saveCache = SaveCache.getInstance();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void startAutoSave() {
        executorService.scheduleAtFixedRate(this::saveAllData, 1, 1, TimeUnit.MINUTES);
    }

    public void saveAllData() {
        saveCache.saveAllItems();
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    public void registerSaveItem(SaveItem saveItem) {
        saveCache.addSaveItem(saveItem);
    }

    public void unregisterSaveItem(SaveItem saveItem) {
        saveCache.removeSaveItem(saveItem);
    }
}
