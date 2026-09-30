package com.pkbob.roll4scape;

import java.awt.Dimension;
import java.awt.Graphics2D;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class Roll4ScapeTaskOverlay extends OverlayPanel
{
    private ActiveTask activeTask;

    public Roll4ScapeTaskOverlay()
    {
        setPosition(OverlayPosition.TOP_LEFT);
    }

    public void setActiveTask(ActiveTask activeTask)
    {
        this.activeTask = activeTask;
    }

    public void clearActiveTask()
    {
        this.activeTask = null;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (activeTask == null)
        {
            return null;
        }

        panelComponent.getChildren().clear();

        panelComponent.getChildren().add(
                TitleComponent.builder()
                        .text("Roll4Scape")
                        .build()
        );

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left(activeTask.getTargetName())
                        .build()
        );

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Progress")
                        .right(activeTask.getProgressText())
                        .build()
        );

        return super.render(graphics);
    }
}