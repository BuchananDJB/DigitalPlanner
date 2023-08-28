package tools.savemanager;


import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class SaveCacheTest {

    private SaveCache saveCache;

    @Before
    public void before() {
        saveCache = SaveCache.getInstance();
    }

    @Test
    public void addSaveItem() {
        SaveItem mockSaveItem = mock(SaveItem.class);
        assertTrue(saveCache.addSaveItem(mockSaveItem));
        assertFalse(saveCache.addSaveItem(mockSaveItem));
    }

    @Test
    public void removeSaveItem() {
        SaveItem mockSaveItem = mock(SaveItem.class);
        assertFalse(saveCache.removeSaveItem(mockSaveItem));
        assertTrue(saveCache.addSaveItem(mockSaveItem));
        assertTrue(saveCache.removeSaveItem(mockSaveItem));
    }

    @Test
    public void saveAllItems() {
        Set<SaveItem> mockSaveItems = new HashSet<>();
        for (int i = 0; i < 1000; ++i) {
            mockSaveItems.add(mock(SaveItem.class));
        }

        mockSaveItems.forEach(saveItem -> assertTrue(saveCache.addSaveItem(saveItem)));
        saveCache.saveAllItems();
        mockSaveItems.forEach(saveItem -> verify(saveItem, times(1)).saveData());
    }

}