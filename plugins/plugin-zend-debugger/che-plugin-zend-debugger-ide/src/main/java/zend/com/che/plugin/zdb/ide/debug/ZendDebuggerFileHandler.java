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
package zend.com.che.plugin.zdb.ide.debug;

import com.google.common.base.Optional;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import org.eclipse.che.api.debug.shared.model.Location;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.document.Document;
import org.eclipse.che.ide.api.editor.text.TextPosition;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.event.FileEvent;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.VirtualFile;
import org.eclipse.che.plugin.debugger.ide.debug.ActiveFileHandler;

/**
 * Responsible to open files in editor when debugger stopped at breakpoint.
 *
 * @author Bartlomiej Laczkowski
 */
public class ZendDebuggerFileHandler implements ActiveFileHandler {

	private final EditorAgent editorAgent;
	private final EventBus eventBus;
	private final AppContext appContext;

	@Inject
	public ZendDebuggerFileHandler(EditorAgent editorAgent, AppContext appContext, EventBus eventBus) {
		this.editorAgent = editorAgent;
		this.appContext = appContext;
		this.eventBus = eventBus;
	}

	@Override
	public void openFile(Location location, AsyncCallback<VirtualFile> callback) {
		VirtualFile activeFile = null;
		String activePath = null;
		final EditorPartPresenter activeEditor = editorAgent.getActiveEditor();
		if (activeEditor != null) {
			activeFile = editorAgent.getActiveEditor().getEditorInput().getFile();
			activePath = activeFile.getLocation().toString();
		}
		if ((activePath != null && !activePath.equals(location.getTarget())
				&& !activePath.equals(location.getResourcePath())) || activePath == null) {
			if (location.isExternalResource()) {
				openExternalResource(location, callback);
			} else {
				doOpenFile(location, callback);
			}
		} else {
			scrollEditorToExecutionPoint((TextEditor) activeEditor, location.getLineNumber());
			callback.onSuccess(activeFile);
		}
	}

	private void doOpenFile(final Location location, final AsyncCallback<VirtualFile> callback) {
		appContext.getWorkspaceRoot().getFile(location.getResourcePath()).then(new Operation<Optional<File>>() {
			@Override
			public void apply(Optional<File> file) throws OperationException {
				if (file.isPresent()) {
					handleActivatedFile(file.get(), callback, location.getLineNumber());
					eventBus.fireEvent(FileEvent.createOpenFileEvent(file.get()));
				} else {
					callback.onFailure(new IllegalStateException("File is undefined"));
				}
			}
		}).catchError(new Operation<PromiseError>() {
			@Override
			public void apply(PromiseError error) throws OperationException {
				callback.onFailure(error.getCause());
			}
		});
	}

	private void openExternalResource(final Location location, final AsyncCallback<VirtualFile> callback) {
		// TODO - handle external resources
	}

	public void handleActivatedFile(final VirtualFile virtualFile, final AsyncCallback<VirtualFile> callback,
			final int debugLine) {
		editorAgent.openEditor(virtualFile, new EditorAgent.OpenEditorCallback() {
			@Override
			public void onEditorOpened(EditorPartPresenter editor) {
				new Timer() {
					@Override
					public void run() {
						scrollEditorToExecutionPoint((TextEditor) editorAgent.getActiveEditor(), debugLine);
						callback.onSuccess(virtualFile);
					}
				}.schedule(300);
			}

			@Override
			public void onEditorActivated(EditorPartPresenter editor) {
				new Timer() {
					@Override
					public void run() {
						scrollEditorToExecutionPoint((TextEditor) editorAgent.getActiveEditor(), debugLine);
						callback.onSuccess(virtualFile);
					}
				}.schedule(300);
			}

			@Override
			public void onInitializationFailed() {
				callback.onFailure(null);
			}
		});
	}

	private void scrollEditorToExecutionPoint(TextEditor editor, int lineNumber) {
		Document document = editor.getDocument();

		if (document != null) {
			TextPosition newPosition = new TextPosition(lineNumber, 0);
			document.setCursorPosition(newPosition);
		}
	}

}
