package com.pkbob.roll4scape;

import java.awt.Dimension;
import java.awt.Graphics2D;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Perspective;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class Roll4ScapeCompletionOverlay extends Overlay
{
    private static final long DISPLAY_TIME_MS = 3000;

    private final Client client;

    private String completionText = null;
    private long hideAt = 0;

    public Roll4ScapeCompletionOverlay(Client client)
    {
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void showCompletion(
            int rxpAwarded,
            int currentStreak)
    {
        completionText =
                "TASK COMPLETE!  +" +
                        rxpAwarded +
                        " RXP  |  Streak: " +
                        currentStreak;

        hideAt =
                System.currentTimeMillis() +
                        DISPLAY_TIME_MS;
    }

    public void clear()
    {
        completionText = null;
        hideAt = 0;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (completionText == null)
        {
            return null;
        }

        if (System.currentTimeMillis() >= hideAt)
        {
            clear();
            return null;
        }

        Player player = client.getLocalPlayer();

        if (player == null)
        {
            return null;
        }

        LocalPoint localPoint =
                player.getLocalLocation();

        Point canvasPoint =
                Perspective.localToCanvas(
                        client,
                        localPoint,
                        client.getPlane(),
                        player.getLogicalHeight() + 40
                );

        if (canvasPoint == null)
        {
            return null;
        }

        int textWidth =
                graphics.getFontMetrics()
                        .stringWidth(completionText);

        graphics.drawString(
                completionText,
                canvasPoint.getX() - (textWidth / 2),
                canvasPoint.getY()
        );

        return null;
    }
}