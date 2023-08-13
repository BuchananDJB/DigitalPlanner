package gui.planner.components.notes;

import tools.DataTools;
import tools.savemanager.SaveItem;
import tools.savemanager.SaveManager;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

public class NotesTextArea extends JTextArea implements SaveItem {

    private final static String NOTES_FILENAME = "notes.txt";

    private UndoManager undoManager;
    private final String directoryPath;

    // TODO: Add scroll functionality

    public NotesTextArea(String directoryPath) {
        super();
        this.directoryPath = directoryPath;
        String notesTextContent = DataTools.readFileAsString(directoryPath + "/" + NOTES_FILENAME);
        this.setText(notesTextContent);
        setupUndoRedo();
        setupRightClickMenu();
        registerTextAreaSaveItem();
    }

    private void registerTextAreaSaveItem() {
        SaveManager saveManager = new SaveManager();
        saveManager.registerSaveItem(this);
    }

    private void setupUndoRedo() {
        undoManager = new UndoManager();
        getDocument().addUndoableEditListener(undoManager);

        // Undo
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
        getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        // Redo
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
        getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });
    }

    private void setupRightClickMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        // Cut
        JMenuItem cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutItem.setText("Cut (Ctrl + X)");
        popupMenu.add(cutItem);

        // Copy
        JMenuItem copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyItem.setText("Copy (Ctrl + C)");
        popupMenu.add(copyItem);

        // Paste
        JMenuItem pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteItem.setText("Paste (Ctrl + V)");
        popupMenu.add(pasteItem);

        popupMenu.addSeparator();

        // Undo
        JMenuItem undoItem = new JMenuItem("Undo (Ctrl + Z)");
        undoItem.addActionListener(e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        });
        popupMenu.add(undoItem);

        // Redo
        JMenuItem redoItem = new JMenuItem("Redo (Ctrl + Y)");
        redoItem.addActionListener(e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        });
        popupMenu.add(redoItem);

        // Add right-click mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Dimension menuSize = popupMenu.getSize();
                    Dimension notesTextAreaSize = NotesTextArea.this.getSize();

                    int xPopup = e.getX();
                    int yPopup = e.getY();

                    if (((xPopup + menuSize.width) >= notesTextAreaSize.width)) {
                        xPopup -= menuSize.width;
                    }

                    if (((yPopup + menuSize.height) >= notesTextAreaSize.height)) {
                        yPopup -= menuSize.height;
                    }

                    popupMenu.show(e.getComponent(), xPopup, yPopup);
                }
            }
        });

        setComponentPopupMenu(popupMenu);
    }

    @Override
    public void saveData() {
        String currentText = this.getText();
        if (DataTools.isNullEmptyBlankString(currentText)) {
            DataTools.deleteFile(directoryPath + "/" + NOTES_FILENAME);
            return;
        }
        DataTools.writeStringToFile(currentText, directoryPath + "/" + NOTES_FILENAME);
    }
}
