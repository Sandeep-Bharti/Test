/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.view.swing.map;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import org.freeplane.core.ui.IUserInputListenerFactory;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.nodelocation.LocationModel;

/**
 * @author Dimitry Polivaev
 */
public class NodeMotionListenerView extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isMouseEntered;
	final private NodeView movedView;

	public NodeMotionListenerView(final NodeView view) {
		super();
		movedView = view;
		final IUserInputListenerFactory userInputListenerFactory = view.getMap().getModeController()
		    .getUserInputListenerFactory();
		addMouseListener(userInputListenerFactory.getNodeMotionListener());
		addMouseMotionListener(userInputListenerFactory.getNodeMotionListener());
		this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		final String helpMsg = TextUtils.getText("node_location_help");
		this.setToolTipText(helpMsg);
	}

	public NodeView getMovedView() {
		return movedView;
	}

	public boolean isMouseEntered() {
		return isMouseEntered;
	}

	@Override
	public void paintComponent(final Graphics g) {
		if (!isMouseEntered()) 
			return;
		final Graphics2D g2 = (Graphics2D) g;
		final Object renderingHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		final MapView parent = (MapView) getParent();
		parent.getModeController().getController().getViewController().setEdgesRenderingHint(g2);
		final Color color = g2.getColor();
		if (movedView.isFree()) {
			g2.setColor(Color.BLUE);
			g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
		}
		else if (LocationModel.getModel(movedView.getModel()).getHGap() <= 0) {
			g2.setColor(Color.RED);
			g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
		}
		g2.setColor(Color.BLACK);
		g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setColor(color);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingHint);
	}

	public void setMouseEntered() {
		isMouseEntered = true;
		repaint();
	}

	public void setMouseExited() {
		isMouseEntered = false;
		repaint();
	}
}