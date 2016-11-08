/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.api.machine.execagent;

import com.google.inject.Singleton;

import org.eclipse.che.api.machine.shared.dto.execagent.event.ProcessDiedEventWithPidDto;
import org.eclipse.che.ide.util.loging.Log;

/**
 * Handles event fired by exec agent when process died
 *
 * @author Dmitry Kuleshov
 */
@Singleton
public class ProcessDiedEventHandler extends AbstractExecAgentEventHandler<ProcessDiedEventWithPidDto, Void>{

    protected ProcessDiedEventHandler() {
        super(ProcessDiedEventWithPidDto.class, Void.class);
    }

    @Override
    public void handleNotification(ProcessDiedEventWithPidDto params) {
        Log.debug(getClass(), "Handling process died event. Params: " + params);
        handle(params);
    }
}
