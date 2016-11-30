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
package org.eclipse.che.ide.command.editor;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;

/**
 * The view for {@link CommandEditor}.
 *
 * @author Artem Zatsarynnyi
 */
@ImplementedBy(CommandEditorViewImpl.class)
public interface CommandEditorView extends View<CommandEditorView.ActionDelegate> {

    /**
     * Add page for editing command.
     *
     * @param page
     *         page to add
     * @param title
     *         text that should be used as page's title
     * @param tooltip
     *         text that should be used as page's tooltip
     */
    void addPage(IsWidget page, String title, String tooltip);

    /**
     * Set whether saving command is enabled or not.
     *
     * @param enable
     *         {@code true} if command saving is enabled and {@code false} otherwise
     */
    void setSaveEnabled(boolean enable);

    /** The action delegate for this view. */
    interface ActionDelegate extends BaseActionDelegate {

        /** Called when saving command is requested. */
        void onCommandSave();
    }
}