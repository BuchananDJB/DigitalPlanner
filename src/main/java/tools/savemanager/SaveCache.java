package tools.savemanager;

import java.util.HashSet;
import java.util.Set;

public class SaveCache {

    private static SaveCache instance;
    private final Set<SaveItem> saveItems;

    public static SaveCache getInstance() {
        if (SaveCache.instance == null) {
            SaveCache.instance = new SaveCache();
        }
        return instance;
    }

    private SaveCache() {
        this.saveItems = new HashSet<>();
    }

    public boolean addSaveItem(SaveItem saveItem) {
        return saveItems.add(saveItem);
    }

    public boolean removeSaveItem(SaveItem saveItem) {
        return saveItems.remove(saveItem);
    }

    public void saveAllItems() {
        saveItems.forEach(SaveItem::saveData);
    }
}
