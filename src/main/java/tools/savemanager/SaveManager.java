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

    public SaveManager(SaveCache saveCache, ScheduledExecutorService executorService) {
        this.saveCache = saveCache;
        this.executorService = executorService;
    }

    public void startDefaultAutoSave() {
        executorService.scheduleAtFixedRate(this::saveAllData, 1, 1, TimeUnit.MINUTES);
    }

    public void startCustomAutoSave(long initialDelay, long period, TimeUnit timeUnit) {
        executorService.scheduleAtFixedRate(this::saveAllData, initialDelay, period, timeUnit);
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

    public boolean registerSaveItem(SaveItem saveItem) {
        return saveCache.addSaveItem(saveItem);
    }

    public boolean unregisterSaveItem(SaveItem saveItem) {
        return saveCache.removeSaveItem(saveItem);
    }
}
