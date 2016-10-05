/*******************************************************************************
 * Copyright (c) 2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zend Technologies - initial API and implementation
 *******************************************************************************/
package zend.com.che.plugin.zdb.ide;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import zend.com.che.plugin.zdb.ide.debug.ZendDebugger;

import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.debug.DebuggerManager;

/**
 * Extension allows debug PHP applications with the use of Zend Debugger.
 *
 * @author Bartlomiej Laczkowski
 */
@Singleton
@Extension(title = "Zend Debugger", version = "0.0.1")
public class ZendDebuggerExtension {

    @Inject
    public ZendDebuggerExtension(DebuggerManager debuggerManager,
                                 ZendDebugger zendDebugger) {
        debuggerManager.registeredDebugger(ZendDebugger.ID, zendDebugger);
    }
    
}
