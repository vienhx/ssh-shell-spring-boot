/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fonimus.ssh.shell;

import com.github.fonimus.ssh.shell.auth.SshAuthentication;
import com.github.fonimus.ssh.shell.postprocess.PostProcessorObject;
import lombok.Getter;
import lombok.Setter;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

import java.util.List;

/**
 * Ssh context to hold terminal, exit callback and thread per thread
 */
@Getter
public class SshContext {

    private SshShellRunnable sshShellRunnable;

    private Terminal terminal;

    private LineReader lineReader;

    private SshAuthentication authentication;

    @Setter
    private List<PostProcessorObject> postProcessorsList;

    public SshContext() {
    }

    /**
     * Constructor
     *
     * @param sshShellRunnable ssh runnable
     * @param terminal         ssh terminal
     * @param lineReader       ssh line reader
     * @param authentication   (optional) spring authentication objects
     */
    public SshContext(SshShellRunnable sshShellRunnable, Terminal terminal, LineReader lineReader,
                      SshAuthentication authentication) {
        this.sshShellRunnable = sshShellRunnable;
        this.terminal = terminal;
        this.lineReader = lineReader;
        this.authentication = authentication;
    }

    /**
     * Check if current prompt is the one started with application
     *
     * @return if local prompt or not
     */
    public boolean isLocalPrompt() {
        return sshShellRunnable == null;
    }
}
