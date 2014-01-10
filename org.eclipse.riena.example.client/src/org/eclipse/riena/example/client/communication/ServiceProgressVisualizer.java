/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.communication;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.riena.communication.core.progressmonitor.AbstractRemoteProgressMonitor;
import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitor;
import org.eclipse.riena.communication.core.progressmonitor.RemoteProgressMonitorEvent;
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.ui.application.ProgressVisualizerLocator;
import org.eclipse.riena.ui.core.uiprocess.ProcessInfo;
import org.eclipse.riena.ui.core.uiprocess.UICallbackDispatcher;
import org.eclipse.riena.ui.core.uiprocess.UISynchronizer;
import org.eclipse.riena.ui.ridgets.IUIProcessRidget;

/**
 * An {@link IRemoteProgressMonitor} visualizing progress inside an
 * {@link IUIProcessRidget}
 */
public class ServiceProgressVisualizer extends AbstractRemoteProgressMonitor {

	private final static double NORMALIZED_TOTAL_WORK = 10000;

	// the monitor for delegation
	private IProgressMonitor progressMonitor;

	//name of the task working on
	private String taskName;

	private final Map<CommunicationDirection, CommunicationChannel> channels = new HashMap<CommunicationDirection, CommunicationChannel>();

	enum CommunicationDirection {
		REQUEST, RESPONSE;
	}

	/**
	 * describes the progress and total work of a {@link CommunicationDirection}
	 */
	private static class CommunicationChannel {

		private int actualTotalWork = 0;
		private int actualWorkedUnits = 0;

		int getActualTotalWork() {
			return actualTotalWork;
		}

		void setActualTotalWork(final int actualTotalWork) {
			this.actualTotalWork = actualTotalWork;
		}

		int getActualWorkedUnits() {
			return actualWorkedUnits;
		}

		void setActualWorkedUnits(final int actualWorkedUnits) {
			this.actualWorkedUnits = actualWorkedUnits;
		}

		void workUnitsDone(final int bytes) {
			setActualWorkedUnits(getActualWorkedUnits() + bytes);
		}

		/*
		 * normalize means recalculate the actual work as part of
		 * "total work for direction" which is the half of total work
		 */
		int normalizeActualWorkedUnits() {
			if (actualWorkedUnits == 0) {
				return 0;
			}
			final double workMultiplier = Double.valueOf(actualWorkedUnits) / Double.valueOf(actualTotalWork);
			return (int) ((NORMALIZED_TOTAL_WORK / 2) * workMultiplier);
		}

	}

	public ServiceProgressVisualizer(final String taskname, final IProgressMonitor progressMonitor) {
		init(taskname, progressMonitor);
	}

	public ServiceProgressVisualizer(final String taskname) {
		// ui stuff goes here
		final UICallbackDispatcher callBackDispatcher = new UICallbackDispatcher(UISynchronizer.createSynchronizer());
		final ISubApplicationNode subApplicationNode = locateActiveSubApplicationNode();
		// init delegation
		callBackDispatcher.addUIMonitor(new ProgressVisualizerLocator().getProgressVisualizer(subApplicationNode));
		progressMonitor = callBackDispatcher.createThreadSwitcher();
		init(taskname, progressMonitor);
		configureProcessInfo(callBackDispatcher, subApplicationNode);
	}

	private void init(final String taskname, final IProgressMonitor progressMonitor) {
		initChannels();
		this.taskName = taskname;
		this.progressMonitor = progressMonitor;
	}

	private void initChannels() {
		// we can write and read ..
		channels.put(CommunicationDirection.REQUEST, new CommunicationChannel());
		channels.put(CommunicationDirection.RESPONSE, new CommunicationChannel());
	}

	private void configureProcessInfo(final UICallbackDispatcher callBackDispatcher,
			final ISubApplicationNode applicationNode) {
		final ProcessInfo processInfo = callBackDispatcher.getProcessInfo();
		processInfo.setNote(taskName);
		processInfo.setTitle(taskName);
		processInfo.setDialogVisible(true);
		processInfo.setContext(applicationNode);
		processInfo.addPropertyChangeListener(new RemoteServiceCancelListener());
	}

	protected ISubApplicationNode locateActiveSubApplicationNode() {
		final ApplicationNode applicationNode = (ApplicationNode) ApplicationNodeManager.getApplicationNode();
		for (final ISubApplicationNode child : applicationNode.getChildren()) {
			if (child.isActivated()) {
				return child;
			}
		}
		return null;
	}

	private static class RemoteServiceCancelListener implements PropertyChangeListener {

		public void propertyChange(final PropertyChangeEvent evt) {
			if (ProcessInfo.PROPERTY_CANCELED.equals(evt.getPropertyName())) {
				//TODO which is the best way to handle that?
			}
		}
	}

	//
	/// implement needed callbacks

	@Override
	public void start() {
		super.start();
		progressMonitor.beginTask(taskName, (int) NORMALIZED_TOTAL_WORK);
	}

	CommunicationChannel getChannel(final CommunicationDirection communicationDrection) {
		return channels.get(communicationDrection);
	}

	@Override
	public void request(final int bytes, final int totalBytes) {
		workUntisCompleted(bytes, totalBytes, getChannel(CommunicationDirection.REQUEST));
	}

	public void request(final RemoteProgressMonitorEvent event) {
		request(event.getBytesProcessed(), event.getTotalBytes());
	}

	@Override
	public void response(final int bytes, final int totalBytes) {
		workUntisCompleted(bytes, totalBytes, getChannel(CommunicationDirection.RESPONSE));
	}

	public void response(final RemoteProgressMonitorEvent event) {
		response(event.getBytesProcessed(), event.getTotalBytes());
	}

	private void workUntisCompleted(final int bytes, final int totalBytes,
			final CommunicationChannel communicationChannel) {
		communicationChannel.actualTotalWork = totalBytes;
		updateWorked(bytes, communicationChannel);
		if (transferComplete()) {
			progressMonitor.done();
			return;
		}
		progressMonitor.worked(calculateTotalNormalizedProgress());
	}

	/*
	 * are we done?
	 */
	private boolean transferComplete() {
		return calculateTotalNormalizedProgress() >= NORMALIZED_TOTAL_WORK;
	}

	private int calculateTotalNormalizedProgress() {
		int sum = 0;
		for (final CommunicationChannel channel : channels.values()) {
			sum += channel.normalizeActualWorkedUnits();
		}
		return sum;
	}

	/*
	 * update the channel
	 */
	private void updateWorked(final int bytes, final CommunicationChannel communicationChannel) {
		communicationChannel.workUnitsDone(bytes);
	}

}
