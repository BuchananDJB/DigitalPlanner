package GUI.planner.components.notes;

import GUI.planner.DigitalPlanner;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

public class NotesTextArea extends JTextArea {

    private UndoManager undoManager;
    private boolean firstRightClick;

    public NotesTextArea() {
        this("");
    }

    public NotesTextArea(String notesTextContent) {
        super();
        this.setText(notesTextContent);
        this.firstRightClick = true;
        setupUndoRedo();
        setupRightClickMenu();
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

        // This whole MenuListener is a band-aid solution for a bug I can't seem to find a reliable fix for
        // If the popup menu is drawn outside the bounds of the main window, visual glitching occurs.
        // I have performed calculations which correctly position the popup menu so this doesn't happen,
        // but for some reason it fails to correctly position the popup menu on the first right click.
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    DigitalPlanner digitalPlanner = (DigitalPlanner) SwingUtilities.getAncestorOfClass(DigitalPlanner.class, NotesTextArea.this);
                    if (digitalPlanner != null && firstRightClick) {
                        digitalPlanner.setSize(digitalPlanner.getWidth(), digitalPlanner.getHeight() + 1);
                        firstRightClick = false;
                    }
                });
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        // Add right-click mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
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

}