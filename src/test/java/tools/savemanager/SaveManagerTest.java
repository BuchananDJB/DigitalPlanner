package tools.savemanager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class SaveManagerTest {

    private SaveManager saveManager;

    @Mock private SaveCache mockSaveCache;
    @Mock private ScheduledExecutorService mockExecutorService;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
        saveManager = new SaveManager(mockSaveCache, mockExecutorService);
    }

    @Test
    public void startDefaultAutoSave() {
        saveManager.startDefaultAutoSave();
        verify(mockExecutorService, times(1)).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));

        saveManager.shutdown();
    }

    @Test
    public void startCustomAutoSave() {
        long initialDelay = 1;
        long period = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        saveManager.startCustomAutoSave(initialDelay, period, timeUnit);
        verify(mockExecutorService, times(1)).scheduleAtFixedRate(any(), eq(initialDelay), eq(period), eq(timeUnit));

        saveManager.shutdown();
    }

    @Test
    public void saveAllData() {
        Set<SaveItem> mockSaveItems = new HashSet<>();
        int numSaveItems = 1000;
        for (int i = 0; i < numSaveItems; ++i) {
            mockSaveItems.add(mock(SaveItem.class));
        }

        mockSaveItems.forEach(saveItem -> saveManager.registerSaveItem(saveItem));
        verify(mockSaveCache, times(numSaveItems)).addSaveItem(any(SaveItem.class));

        saveManager.saveAllData();
        verify(mockSaveCache, times(1)).saveAllItems();
    }

    @Test
    public void shutdown() throws InterruptedException {
        saveManager.startDefaultAutoSave();
        verify(mockExecutorService, times(1)).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));

        when(mockExecutorService.awaitTermination(anyLong(), any(TimeUnit.class))).thenReturn(true);
        saveManager.shutdown();
        verify(mockExecutorService, times(1)).shutdown();
        verify(mockExecutorService, times(1)).awaitTermination(anyLong(), any(TimeUnit.class));
        verify(mockExecutorService, times(0)).shutdownNow();
    }

    @Test
    public void shutdown_awaitTermination() throws InterruptedException {
        saveManager.startDefaultAutoSave();
        verify(mockExecutorService, times(1)).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));

        when(mockExecutorService.awaitTermination(anyLong(), any(TimeUnit.class))).thenReturn(false);
        saveManager.shutdown();
        verify(mockExecutorService, times(1)).shutdown();
        verify(mockExecutorService, times(1)).awaitTermination(anyLong(), any(TimeUnit.class));
        verify(mockExecutorService, times(1)).shutdownNow();
    }

    @Test
    public void shutdown_exceptionThrown() throws InterruptedException {
        saveManager.startDefaultAutoSave();
        verify(mockExecutorService, times(1)).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));

        when(mockExecutorService.awaitTermination(anyLong(), any(TimeUnit.class))).thenThrow(new InterruptedException());
        saveManager.shutdown();
        verify(mockExecutorService, times(1)).shutdown();
        verify(mockExecutorService, times(1)).awaitTermination(anyLong(), any(TimeUnit.class));
        verify(mockExecutorService, times(1)).shutdownNow();
    }

    @Test
    public void registerSaveItem() {
        SaveItem mockSaveItem = mock(SaveItem.class);
        when(mockSaveCache.addSaveItem(mockSaveItem)).thenReturn(true);
        assertTrue(saveManager.registerSaveItem(mockSaveItem));
        verify(mockSaveCache, times(1)).addSaveItem(mockSaveItem);

        when(mockSaveCache.addSaveItem(mockSaveItem)).thenReturn(false);
        assertFalse(saveManager.registerSaveItem(mockSaveItem));
        verify(mockSaveCache, times(2)).addSaveItem(mockSaveItem);
    }

    @Test
    public void unregisterSaveItem() {
        SaveItem mockSaveItem = mock(SaveItem.class);

        when(mockSaveCache.removeSaveItem(mockSaveItem)).thenReturn(false);
        assertFalse(saveManager.unregisterSaveItem(mockSaveItem));
        verify(mockSaveCache, times(1)).removeSaveItem(mockSaveItem);

        when(mockSaveCache.addSaveItem(mockSaveItem)).thenReturn(true);
        assertTrue(saveManager.registerSaveItem(mockSaveItem));
        verify(mockSaveCache, times(1)).addSaveItem(mockSaveItem);

        when(mockSaveCache.removeSaveItem(mockSaveItem)).thenReturn(true);
        assertTrue(saveManager.unregisterSaveItem(mockSaveItem));
        verify(mockSaveCache, times(2)).removeSaveItem(mockSaveItem);
    }
}